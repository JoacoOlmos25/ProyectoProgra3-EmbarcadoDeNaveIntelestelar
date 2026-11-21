package src.vista;

import java.awt.event.ActionListener;
import java.util.List;
import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;

public interface IVistaPrincipal {
    // ActionCommands para el ControladorPrincipal (MVC)
    String CMD_CREAR_NAVE = "CREAR_NAVE";
    String CMD_SELECCIONAR_NAVE = "SELECCIONAR_NAVE";
    String CMD_AGREGAR_TRIPULANTE = "AGREGAR_TRIPULANTE";
    String CMD_INICIAR_MISION = "INICIAR_MISION";
    String CMD_MANTENIMIENTO = "REALIZAR_MANTENIMIENTO";
    String CMD_ABRIR_CONCURRENCIA = "ABRIR_CONCURRENCIA";

    // Estados visuales del indicador de turno (desacoplado de java.awt.Color)
    String TURNO_ESPERA = "ESPERA";
    String TURNO_NAVE = "NAVE";
    String TURNO_UNIVERSO = "UNIVERSO";

    void setActionListener(ActionListener controlador);

    // Lectura de selecciones del usuario (la Vista solo expone datos planos)
    String getTipoNaveACrear();
    int getIndiceNaveSeleccionada();
    String getMisionSeleccionada();
    String getCargoTripulante();
    String getOrigenTripulante();
    String getIdentTripulante();
    String getAntiguedadTripulante();

    // Actualizaciones visuales invocadas por el Controlador o el Observer
    void vincularTablero(Tablero tablero);
    void actualizarPuntoOrigen(Posicion origen);
    void actualizarRutaPredefinida(List<Posicion> ruta);
    void actualizarIndicadorTurno(String descripcionTurno, String tipoTurno);
    void setControlesOperacionHabilitados(boolean habilitado);

    void actualizarFlota(List<String> nombresNaves, int indiceSeleccionado);
    void actualizarRecursos(int combustible, int energia, int desgaste, boolean motorDisponible, boolean reqMantenimiento);
    void actualizarListadoTripulantes(List<String> detalleTripulantesConSalario);
    void agregarEntradaBitacora(String texto);
    void agregarInformeHistorial(String resumenInforme);

    // Popups
    void mostrarPopupColision(String titulo, String detalle, int duracionMs);
    void mostrarPopupInformeMision(String tituloMision, String contenidoInforme);
    void mostrarMensajeError(String mensaje);

    void mostrar();
    void cerrar();
}