package src.main;

import java.util.ArrayList;
import javax.swing.SwingUtilities;

import src.AC.SistemaEstelar;
import src.controlador.ControladorPrincipal;
import src.destinoytablero.ElementoDelUniverso;
import src.destinoytablero.Tablero;
import src.vista.VentanaPrincipal;

public class MainAplicacion {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // 1. Instanciar el Tablero inicial (Modelo del Universo)
                ArrayList<ElementoDelUniverso> elementos = new ArrayList<>();
                for (int i = 0; i < 14; i++) {
                    // Reemplazar por subclases concretas de ElementoDelUniverso cuando estén definidas
                    elementos.add(new ElementoDelUniverso() {});
                }

                Tablero tablero = new Tablero(10, 10, elementos);
                SistemaEstelar modelo = new SistemaEstelar(tablero);
                VentanaPrincipal vista = new VentanaPrincipal();
                new ControladorPrincipal(vista, modelo);
                vista.mostrar();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}