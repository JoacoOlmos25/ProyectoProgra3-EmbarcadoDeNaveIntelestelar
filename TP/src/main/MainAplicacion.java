package src.main;

import java.util.ArrayList;
import javax.swing.SwingUtilities;

import src.AC.SistemaEstelar;
import src.ElementosDelUniverso.Asteroide;
import src.ElementosDelUniverso.ElementoDelUniverso;
import src.ElementosDelUniverso.EstacionDeCarga;
import src.controlador.ControladorPrincipal;
import src.destinoytablero.Tablero;
import src.nave.NaveFactory;
import src.vista.VentanaPrincipal;

public class MainAplicacion {
    /* Cantidad de filas del tablero inicial. */
    private static final int FILAS = 10;
    /* Cantidad de columnas del tablero inicial. */
    private static final int COLUMNAS = 10;
    /* Cantidad de asteroides que se distribuyen en el tablero inicial. */
    private static final int CANT_ASTEROIDES = 8;
    /* Cantidad de estaciones de carga que se distribuyen en el tablero inicial. */
    private static final int CANT_ESTACIONES = 3;
    /** Tipos de naves enemigas que se distribuyen en el tablero inicial (una de cada uno). */
    private static final String[] TIPOS_NAVES_ENEMIGAS = {"Ataque", "Explorador", "Carguero"};
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Tablero tablero = new Tablero(FILAS, COLUMNAS, crearElementosIniciales());
                SistemaEstelar modelo = new SistemaEstelar(tablero);
                VentanaPrincipal vista = new VentanaPrincipal();
                new ControladorPrincipal(vista, modelo);
                vista.mostrar();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    /**
     * Crea la lista de elementos concretos del universo que se distribuirán
     * en el tablero inicial.
     * <p><b>Precondiciones:</b> {@code CANT_ASTEROIDES >= 0},
     * {@code CANT_ESTACIONES >= 0} y la cantidad total de elementos no supera
     * {@code FILAS * COLUMNAS / 4 * 3}, que es el límite del {@code Tablero}.</p>
     * <p><b>Postcondiciones:</b> devuelve una lista nueva con
     * {@code CANT_ASTEROIDES} instancias de {@link Asteroide},
     * {@code CANT_ESTACIONES} instancias de {@link EstacionDeCarga} y una nave
     * enemiga por cada tipo de {@code TIPOS_NAVES_ENEMIGAS}, creada mediante
     * {@link NaveFactory}. No contiene elementos nulos.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     * @return lista de elementos a ubicar en el tablero
     */
    private static ArrayList<ElementoDelUniverso> crearElementosIniciales() {
        int total = CANT_ASTEROIDES + CANT_ESTACIONES + TIPOS_NAVES_ENEMIGAS.length;
        assert CANT_ASTEROIDES >= 0 && CANT_ESTACIONES >= 0 : "Las cantidades de elementos no pueden ser negativas";
        assert total <= (FILAS * COLUMNAS / 4) * 3 : "La cantidad de elementos supera el límite del tablero";

        ArrayList<ElementoDelUniverso> elementos = new ArrayList<>();
        for (int i = 0; i < CANT_ASTEROIDES; i++) {
            elementos.add(new Asteroide());
        }
        for (int i = 0; i < CANT_ESTACIONES; i++) {
            elementos.add(new EstacionDeCarga());
        }
        NaveFactory fabrica = new NaveFactory();
        for (String tipo : TIPOS_NAVES_ENEMIGAS) {
            elementos.add(fabrica.crearNave(tipo));
        }

        assert elementos.size() == total : "Postcondición violada al crear los elementos iniciales";
        return elementos;
    }
}