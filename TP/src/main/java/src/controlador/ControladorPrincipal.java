package src.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import src.AC.Asistente;
import src.AC.Evento;
import src.AC.SistemaEstelar;
import src.Misiones.Mision;
import src.Misiones.ObservadorMision;
import src.Tripulacion.AntiguedadNegativa;
import src.concurrencia.controlador.ControladorVisor;
import src.concurrencia.modelo.PlataformaDeCarga;
import src.concurrencia.vista.VentanaVisorUniverso;
import src.destinoytablero.Observador;
import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;
import src.nave.MantenimientoException;
import src.nave.NoexisteTripulanteException;
import src.vista.IVistaPrincipal;

/**
 * Controlador principal de la interfaz de comando (MVC).
 * Implementa Observador sobre el Tablero para sincronizar automáticamente
 * la telemetría de la nave, la bitácora del modelo y el indicador de turno en cada paso,
 * y ObservadorMision para recibir la ruta planificada y los encuentros con su resultado.
 */
public class ControladorPrincipal implements ActionListener, Observador, ObservadorMision {

    /** Duración en pantalla del popup de colisión (ajustada para facilitar la lectura). */
    private static final int DURACION_POPUP_MS = 3200;

    private final IVistaPrincipal vista;
    private final SistemaEstelar modelo;

    /** Lo modifica el hilo de la misión y lo lee el hilo de Swing. */
    private volatile boolean misionEnCurso = false;
    private VentanaVisorUniverso ventanaConcurrencia;

    /** Cantidad de eventos ya volcados a la vista por cada Asistente/Nave de la flota. */
    private final Map<Asistente, Integer> eventosSincronizadosPorNave = new IdentityHashMap<>();

    public ControladorPrincipal(IVistaPrincipal vista, SistemaEstelar modelo) {
        this.vista = vista;
        this.modelo = modelo;

        this.vista.setActionListener(this);
        Tablero tablero = this.modelo.getTablero();
        this.vista.vincularTablero(tablero);
        this.vista.actualizarPuntoOrigen(tablero.getPosicionNave());
        this.vista.actualizarIndicadorTurno("ESTADO DE TURNO: EN ESPERA DE ÓRDENES", IVistaPrincipal.TURNO_ESPERA);

        tablero.agregarObservador(this);
    }

    // =========================================================================
    // OBSERVADOR DEL TABLERO (Refresco automático por turno)
    // =========================================================================

    @Override
    public void actualizar(String mensaje) {
        refrescarVistaNaveActiva();

        if (!misionEnCurso || mensaje == null) {
            return;
        }

        String lower = mensaje.toLowerCase();
        if (lower.contains("nave se movi")) {
            vista.actualizarIndicadorTurno(
                    "TURNO ACTIVO: NAVE (" + modelo.getNombreNaveActiva() + ")",
                    IVistaPrincipal.TURNO_NAVE
            );
        } else if (!lower.contains("objetivo")) {
            vista.actualizarIndicadorTurno(
                    "TURNO ACTIVO: UNIVERSO DINÁMICO (DESTINO)",
                    IVistaPrincipal.TURNO_UNIVERSO
            );
        }
    }

    // =========================================================================
    // OBSERVADOR DE LA MISIÓN (se invoca desde el hilo de la misión)
    // =========================================================================

    @Override
    public void rutaPlanificada(List<Posicion> ruta) {
        vista.actualizarRutaPredefinida(ruta);
        vista.agregarEntradaBitacora("[MISIÓN] Ruta planificada: " + ruta.size() + " pasos.");
    }

    @Override
    public void interaccionDetectada(String titulo, String detalle) {
        // Sincroniza inmediatamente la telemetría y los eventos que acaba de registrar el modelo
        refrescarVistaNaveActiva();
        vista.mostrarPopupColision(titulo, detalle, DURACION_POPUP_MS);
    }

    // =========================================================================
    // MANEJO DE GESTOS DE USUARIO (MVC - ActionListener)
    // =========================================================================

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        if (comando == null) {
            return;
        }

        switch (comando) {
            case IVistaPrincipal.CMD_CREAR_NAVE:
                manejarCrearNave();
                break;
            case IVistaPrincipal.CMD_SELECCIONAR_NAVE:
                manejarSeleccionarNave();
                break;
            case IVistaPrincipal.CMD_AGREGAR_TRIPULANTE:
                manejarAgregarTripulante();
                break;
            case IVistaPrincipal.CMD_MANTENIMIENTO:
                manejarMantenimiento();
                break;
            case IVistaPrincipal.CMD_INICIAR_MISION:
                manejarIniciarMision();
                break;
            case IVistaPrincipal.CMD_ABRIR_CONCURRENCIA:
                abrirVisorConcurrencia();
                break;
            default:
                break;
        }
    }

    private void manejarCrearNave() {
        String tipo = vista.getTipoNaveACrear();
        if (tipo == null || tipo.isBlank()) {
            vista.mostrarMensajeError("Debe seleccionar un tipo de nave.");
            return;
        }

        try {
            modelo.crearNave(tipo);
            vista.actualizarFlota(modelo.getNombresFlota(), modelo.getIndiceActivo());
            refrescarVistaNaveActiva();
            refrescarTableroNaveActiva();
        } catch (IllegalArgumentException ex) {
            vista.mostrarMensajeError("Error al crear nave: " + ex.getMessage());
        }
    }

    private void manejarSeleccionarNave() {
        int indice = vista.getIndiceNaveSeleccionada();
        if (modelo.seleccionarNave(indice)) {
            vista.agregarEntradaBitacora("--- Cambio de nave activa a: " + modelo.getNombreNaveActiva() + " ---");
            refrescarVistaNaveActiva();
            refrescarTableroNaveActiva();
        }
    }

    private void manejarAgregarTripulante() {
        String ident = vista.getIdentTripulante();
        String antiguedadStr = vista.getAntiguedadTripulante();
        String cargo = vista.getCargoTripulante();
        String origen = vista.getOrigenTripulante();

        if (ident == null || ident.isBlank()) {
            vista.mostrarMensajeError("La identidad/nombre del tripulante no puede estar vacía.");
            return;
        }

        int antiguedad;
        try {
            antiguedad = Integer.parseInt(antiguedadStr);
        } catch (NumberFormatException ex) {
            vista.mostrarMensajeError("La antigüedad debe ser un número entero válido.");
            return;
        }

        try {
            modelo.agregarTripulante(cargo, origen, antiguedad, ident);
            refrescarVistaNaveActiva();
        } catch (AntiguedadNegativa ex) {
            vista.mostrarMensajeError("La antigüedad no puede ser negativa.");
        } catch (IllegalStateException | NoexisteTripulanteException | IllegalArgumentException ex) {
            vista.mostrarMensajeError(ex.getMessage());
        }
    }

    private void manejarMantenimiento() {
        try {
            modelo.realizarMantenimiento();
            refrescarVistaNaveActiva();
        } catch (IllegalStateException | MantenimientoException ex) {
            vista.mostrarMensajeError(ex.getMessage());
        }
    }

    private void manejarIniciarMision() {
        if (misionEnCurso) {
            vista.mostrarMensajeError("Ya hay una misión en ejecución.");
            return;
        }

        String misionElegida = vista.getMisionSeleccionada();
        Mision mision;
        try {
            mision = modelo.crearMision(extraerCodigoMision(misionElegida));
        } catch (IllegalStateException | IllegalArgumentException ex) {
            vista.mostrarMensajeError(ex.getMessage());
            return;
        }

        mision.setObservador(this);
        String nombreNave = modelo.getNombreNaveActiva();

        vista.actualizarPuntoOrigen(modelo.getTablero().getPosicionNave());
        vista.actualizarRutaPredefinida(Collections.emptyList());
        modelo.registrarEventoActivo("Inicio de Misión", misionElegida + " con " + nombreNave);
        refrescarVistaNaveActiva();

        ejecutarMisionEnHilo(misionElegida, mision::realizarMision,
                () -> "Nave: " + nombreNave + "\n" + mision.getInformeFinal());
    }

    private String extraerCodigoMision(String etiqueta) {
        if (etiqueta == null) {
            return "";
        }
        int separador = etiqueta.indexOf(':');
        return separador >= 0 ? etiqueta.substring(0, separador) : etiqueta;
    }

    private void ejecutarMisionEnHilo(String misionElegida, Runnable ejecucionMision, Supplier<String> proveedorInforme) {
        misionEnCurso = true;
        vista.setControlesOperacionHabilitados(false);
        vista.actualizarIndicadorTurno("INICIANDO MISIÓN: " + misionElegida, IVistaPrincipal.TURNO_NAVE);

        Thread hiloMision = new Thread(() -> {
            try {
                ejecucionMision.run();
                String informe = proveedorInforme.get();
                vista.agregarInformeHistorial(informe);
                vista.mostrarPopupInformeMision(misionElegida, informe);
            } catch (Exception ex) {
                vista.mostrarMensajeError("La misión se interrumpió: " + ex.getMessage());
            } finally {
                misionEnCurso = false;
                vista.setControlesOperacionHabilitados(true);
                vista.actualizarIndicadorTurno("ESTADO DE TURNO: EN ESPERA DE ÓRDENES", IVistaPrincipal.TURNO_ESPERA);
                refrescarVistaNaveActiva();
            }
        }, "Hilo-Ejecucion-Mision");
        hiloMision.setDaemon(true);
        hiloMision.start();
    }

    private void abrirVisorConcurrencia() {
        if (ventanaConcurrencia != null && ventanaConcurrencia.isDisplayable()) {
            ventanaConcurrencia.toFront();
            return;
        }

        PlataformaDeCarga plataforma = new PlataformaDeCarga(5);
        ventanaConcurrencia = new VentanaVisorUniverso(plataforma);
        new ControladorVisor(plataforma, ventanaConcurrencia);
        ventanaConcurrencia.mostrar();

        vista.agregarEntradaBitacora("[CONCURRENCIA] Visor del Universo Paralelo abierto.");
    }

    private void refrescarVistaNaveActiva() {
        Asistente ac = modelo.getAsistenteActivo();
        if (ac == null) {
            return;
        }
        vista.actualizarRecursos(
                ac.getCombustibleNave(),
                ac.getEnergiaNave(),
                ac.getDesgasteNave(),
                ac.consultaMotorDisp(),
                ac.consultaReqMantenimiento()
        );
        vista.actualizarListadoTripulantes(modelo.getResumenTripulacionActiva());
        sincronizarBitacora(ac);
    }

    private void refrescarTableroNaveActiva() {
        vista.actualizarPuntoOrigen(modelo.getTablero().getPosicionNave());
        vista.actualizarRutaPredefinida(Collections.emptyList());
    }

    /**
     * Vuelca en la vista todos los eventos nuevos registrados en la Bitácora real de la nave activa,
     * manteniendo un índice independiente por nave para evitar duplicados o pérdidas al alternar flota.
     */
    private synchronized void sincronizarBitacora(Asistente ac) {
        List<Evento> eventos = ac.getEventosBitacora();
        int yaSincronizados = eventosSincronizadosPorNave.getOrDefault(ac, 0);

        while (yaSincronizados < eventos.size()) {
            Evento ev = eventos.get(yaSincronizados);
            vista.agregarEntradaBitacora("[" + ev.getTitulo().toUpperCase() + "] " + ev.getMsj());
            yaSincronizados++;
        }
        eventosSincronizadosPorNave.put(ac, yaSincronizados);
    }
}