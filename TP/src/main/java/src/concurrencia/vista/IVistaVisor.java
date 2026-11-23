package src.concurrencia.vista;
import java.awt.event.ActionListener;

public interface IVistaVisor {
    String CMD_CREAR_GENERADORA = "CREAR_GENERADORA";
    String CMD_CREAR_NAVE = "CREAR_NAVE";
    String CMD_DETENER = "DETENER_SIMULACION";

    void setActionListener(ActionListener controlador);
    void mostrar();
    void cerrar();
}
