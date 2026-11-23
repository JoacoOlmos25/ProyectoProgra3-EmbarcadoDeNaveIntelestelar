package src.Misiones;

import src.destinoytablero.Posicion;

import java.util.List;

/**
 * Recibe los avisos de una misión en curso que no pasan por el Tablero
 * (ruta calculada y encuentros con elementos del universo).
 * Lo implementa el controlador, de modo que la misión no depende de Swing.
 * Los métodos se invocan desde el hilo de la misión: quien los implemente debe
 * delegar las actualizaciones visuales con SwingUtilities.invokeLater(...).
 */
public interface ObservadorMision {

    /**
     * Avisa que la misión calculó su ruta.
     * Pre: ruta != null
     * Post: no modifica el estado de la misión
     * @param ruta ruta planificada, inmodificable
     */
    void rutaPlanificada(List<Posicion> ruta);

    /**
     * Avisa que la nave interactuó con un elemento del universo y comunica el resultado obtenido
     * (daño, recarga, resultado de combate o colisión crítica).
     * Pre: titulo != null && detalle != null
     * Post: no modifica el estado de la misión
     * @param titulo título breve del encuentro
     * @param detalle descripción del encuentro y su efecto efectivo
     */
    void interaccionDetectada(String titulo, String detalle);
}