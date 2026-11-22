package src.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import src.AC.Asistente;
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
 * Además implementa Observador sobre el Tablero para sincronizar automáticamente
 * la telemetría de la nave y el indicador de turno en cada paso de la simulación,
 * y ObservadorMision para recibir la ruta planificada y los encuentros de la misión en curso.
 */
public class ControladorPrincipal implements ActionListener, Observador, ObservadorMision {

    /** Duración en pantalla del popup de colisión. */
    private static final int DURACION_POPUP_MS = 2200;

    private final IVistaPrincipal vista;
    private final SistemaEstelar modelo;

    /** Lo modifica el hilo de la misión y lo lee el hilo de Swing. */
    private volatile boolean misionEnCurso = false;
    private VentanaVisorUniverso ventanaConcurrencia;

    private int eventosBitacoraSincronizados = 0;

    public ControladorPrincipal(IVistaPrincipal vista, SistemaEstelar modelo) {
        this.vista = vista;
        this.modelo = modelo;

        this.vista.setActionListener(this);
        Tablero tablero = this.modelo.getTablero();
        this.vista.vincularTablero(tablero);
        this.vista.actualizarPuntoOrigen(tablero.getPosicionNave());
        this.vista.actualizarIndicadorTurno("ESTADO DE TURNO: EN ESPERA DE ÓRDENES", IVistaPrincipal.TURNO_ESPERA);

        // El controlador también observa el Tablero para refrescar recursos e indicador de turno
        // en cada tick emitido por Mision (nave) o por Destino (universo).
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
            // Cualquier otra actualización durante la misión corresponde al turno dinámico de Destino
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
        vista.mostrarPopupColision(titulo, detalle, DURACION_POPUP_MS);
        vista.agregarEntradaBitacora("[ENCUENTRO] " + detalle);
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
            String etiqueta = modelo.crearNave(tipo);
            vista.actualizarFlota(modelo.getNombresFlota(), modelo.getIndiceActivo());
            refrescarVistaNaveActiva();
            refrescarTableroNaveActiva();
            vista.agregarEntradaBitacora("[SISTEMA] " + etiqueta + " creada y seleccionada como activa.");
        } catch (IllegalArgumentException ex) {
            vista.mostrarMensajeError("Error al crear nave: " + ex.getMessage());
        }
    }

    private void manejarSeleccionarNave() {
        int indice = vista.getIndiceNaveSeleccionada();
        if (modelo.seleccionarNave(indice)) {
            refrescarVistaNaveActiva();
            refrescarTableroNaveActiva();
            vista.agregarEntradaBitacora("--- Cambio de nave activa a: " + modelo.getNombreNaveActiva() + " ---");
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
            String resumenAlta = modelo.agregarTripulante(cargo, origen, antiguedad, ident);
            refrescarVistaNaveActiva();
            vista.agregarEntradaBitacora("[TRIPULACIÓN] Asignado " + resumenAlta);
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
            vista.agregarEntradaBitacora("[MANTENIMIENTO] " + modelo.getNombreNaveActiva()
                    + " restaurada al 0% de desgaste.");
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
        vista.actualizarRutaPredefinida(Collections.emptyList()); // la ruta nueva llega por rutaPlanificada(...)
        modelo.registrarEventoActivo("Inicio de Misión", misionElegida);
        vista.agregarEntradaBitacora("[MISIÓN] Iniciando " + misionElegida + " con " + nombreNave);

        // Nunca llamar a realizarMision() acá directamente: el Thread.sleep congelaría Swing
        ejecutarMisionEnHilo(misionElegida, mision::realizarMision,
                () -> "Nave: " + nombreNave + "\n" + mision.getInformeFinal());
    }

    /**
     * Obtiene el código de misión ("M-01") a partir de la etiqueta del combo ("M-01: Intercepción y Asistencia").
     */
    private String extraerCodigoMision(String etiqueta) {
        if (etiqueta == null) {
            return "";
        }
        int separador = etiqueta.indexOf(':');
        return separador >= 0 ? etiqueta.substring(0, separador) : etiqueta;
    }

    /**
     * Ejecuta mision.realizarMision() sin congelar Swing
     * (debido a los Thread.sleep(int) internos del bucle de turnos).
     */
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
        sincronizarEventosMotorBitacora(ac);
    }
    /**
     * Sincroniza el punto de origen y la ruta del tablero con la nave activa (sin ruta planificada)
     */
    private void refrescarTableroNaveActiva() {
        vista.actualizarPuntoOrigen(modelo.getTablero().getPosicionNave());
        vista.actualizarRutaPredefinida(Collections.emptyList());
    }

    private void sincronizarEventosMotorBitacora(Asistente ac) {
        List<src.AC.Evento> eventos = ac.getEventosBitacora();
        while (eventosBitacoraSincronizados < eventos.size()) {
            src.AC.Evento ev = eventos.get(eventosBitacoraSincronizados);
            String titulo = ev.getTitulo();
            String msj = ev.getMsj();

            if (titulo.contains("Motor Warp") || titulo.contains("Salto")) {
                vista.agregarEntradaBitacora("[MOTOR WARP] " + titulo + ": " + msj);
            }
            eventosBitacoraSincronizados++;
        }
    }
}