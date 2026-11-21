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
     * Avisa que la misión calculó su ruta
     * Pre: ruta no nula
     * Post: no modifica la misión
     * @param ruta ruta planificada, inmodificable
     */
    void rutaPlanificada(List<Posicion> ruta);

    /**
     * Avisa que la nave encontró un elemento del universo y va a interactuar con él
     * Pre: titulo y detalle no nulos
     * Post: no modifica la misión
     * @param titulo título breve del encuentro
     * @param detalle descripción del encuentro (elemento y posición)
     */
    void interaccionDetectada(String titulo, String detalle);
}