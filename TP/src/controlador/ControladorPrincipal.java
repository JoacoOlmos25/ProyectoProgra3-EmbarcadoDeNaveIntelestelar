package src.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.Timer;

import src.AC.Asistente;
import src.AC.SistemaEstelar;
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
 * la telemetría de la nave y el indicador de turno en cada paso de la simulación.
 */
public class ControladorPrincipal implements ActionListener, Observador {

    private final IVistaPrincipal vista;
    private final SistemaEstelar modelo;

    private boolean misionEnCurso = false;
    private VentanaVisorUniverso ventanaConcurrencia;

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
            vista.agregarEntradaBitacora("[SISTEMA] " + etiqueta + " creada y seleccionada como activa.");
        } catch (IllegalArgumentException ex) {
            vista.mostrarMensajeError("Error al crear nave: " + ex.getMessage());
        }
    }

    private void manejarSeleccionarNave() {
        int indice = vista.getIndiceNaveSeleccionada();
        if (modelo.seleccionarNave(indice)) {
            refrescarVistaNaveActiva();
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
        Tablero tablero = modelo.getTablero();
        Posicion origen = tablero.getPosicionNave();

        List<Posicion> ruta;
        try {
            ruta = modelo.prepararRutaMision();
        } catch (IllegalStateException ex) {
            vista.mostrarMensajeError(ex.getMessage());
            return;
        }

        Posicion destino = tablero.getObjetivo();
        vista.actualizarPuntoOrigen(origen);
        vista.actualizarRutaPredefinida(ruta);
        modelo.registrarEventoActivo("Inicio de Misión", misionElegida + " hacia (" + destino.getX() + "," + destino.getY() + ")");
        vista.agregarEntradaBitacora("[MISIÓN] Iniciando " + misionElegida + " | Ruta planificada: " + ruta.size() + " pasos.");

        // ---------------------------------------------------------------------
        // PUNTO DE ENGANCHE PARA MISIONES DEFINITIVAS (ver docs/GUIA_INTEGRACION_EQUIPO.md)
        // Reemplazar ejecutarRecorridoTemporal(...) por ejecutarMisionEnHilo(...)
        // cuando Mision_01/02/03 + Destino estén integrados en SistemaEstelar.
        // ---------------------------------------------------------------------
        ejecutarRecorridoTemporal(misionElegida, origen, destino, ruta);
    }

    /**
     * Plantilla lista para ejecutar mision.realizarMision() sin congelar Swing
     * (debido a los Thread.sleep(int) internos del bucle de turnos).
     */
    @SuppressWarnings("unused")
    private void ejecutarMisionEnHilo(String misionElegida, Runnable ejecucionMision, java.util.function.Supplier<String> proveedorInforme) {
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

    private void ejecutarRecorridoTemporal(String misionElegida, Posicion origen, Posicion destino, List<Posicion> ruta) {
        Tablero tablero = modelo.getTablero();
        misionEnCurso = true;
        vista.setControlesOperacionHabilitados(false);

        final int[] paso = {1};
        final int[] encuentros = {0};
        final boolean[] esTurnoNave = {true};

        Timer timerMision = new Timer(550, null);
        timerMision.addActionListener(ev -> {
            if (paso[0] < ruta.size()) {
                if (esTurnoNave[0]) {
                    Posicion siguiente = ruta.get(paso[0]++);
                    tablero.moverNave(siguiente);

                    if (tablero.inspeccionarPosicion() != null) {
                        encuentros[0]++;
                        String tipoElem = tablero.inspeccionarPosicion().getClass().getSimpleName();
                        modelo.registrarEventoActivo("Encuentro", "Interacción con " + tipoElem
                                + " en (" + siguiente.getX() + "," + siguiente.getY() + ")");
                        vista.mostrarPopupColision(
                                "INTERACCIÓN DETECTADA",
                                "La nave encontró [" + tipoElem + "] en (" + siguiente.getX() + ", " + siguiente.getY() + ")",
                                2200
                        );
                    }
                } else {
                    // Simula visualmente la alternancia de turno hasta conectar Destino.avanzarTurno()
                    vista.actualizarIndicadorTurno(
                            "TURNO ACTIVO: UNIVERSO DINÁMICO (DESTINO)",
                            IVistaPrincipal.TURNO_UNIVERSO
                    );
                }
                esTurnoNave[0] = !esTurnoNave[0];
            } else {
                timerMision.stop();
                misionEnCurso = false;
                vista.setControlesOperacionHabilitados(true);
                vista.actualizarIndicadorTurno("ESTADO DE TURNO: EN ESPERA DE ÓRDENES", IVistaPrincipal.TURNO_ESPERA);

                String informe = modelo.generarResumenInforme(misionElegida, origen, destino, encuentros[0]);
                modelo.registrarEventoActivo("Fin de Misión", misionElegida + " finalizada.");
                vista.agregarInformeHistorial(informe);
                vista.mostrarPopupInformeMision(misionElegida, informe);
                refrescarVistaNaveActiva();
            }
        });
        timerMision.start();
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
    }
}