package src.concurrencia;

import javax.swing.SwingUtilities;
import src.concurrencia.controlador.ControladorVisor;
import src.concurrencia.modelo.PlataformaDeCarga;
import src.concurrencia.vista.VentanaVisorUniverso;

public class MainPruebaVisor {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlataformaDeCarga plataforma = new PlataformaDeCarga(5); // Capacidad: 5 baterías
            VentanaVisorUniverso vista = new VentanaVisorUniverso(plataforma);
            new ControladorVisor(plataforma, vista);
            vista.mostrar();
        });
    }
}