package src.Misiones;

import src.AC.Asistente;
import src.destinoytablero.Destino;
import src.destinoytablero.Tablero;

/**
 * Fábrica responsable de instanciar las subclases concretas de Mision (Patrón Factory).
 */
public class MisionFactory {

    /**
     * Contrato:
     * Instancia la misión según el código requerido.
     * Precondicion:
     * - codigo != null && !codigo.isBlank() ("M-01", "M-02" o "M-03", sin distinguir mayúsculas ni espacios)
     * - ac != null, destino != null y tablero != null
     * Postcondicion:
     * - Devuelve una instancia no nula de Mision lista para ejecutarse.
     * @throws IllegalArgumentException si el código de misión es desconocido
     */
    public Mision crearMision(String codigo, Asistente ac, Destino destino, Tablero tablero) {
        assert codigo != null && !codigo.isBlank() : "El código de misión no puede ser nulo ni vacío";
        assert ac != null : "El asistente no puede ser nulo";
        assert destino != null : "El destino no puede ser nulo";
        assert tablero != null : "El tablero no puede ser nulo";

        Mision creada;
        switch (codigo.trim().toUpperCase()) {
            case "M-01":
                creada = new Mision_01(ac, destino, tablero);
                break;
            case "M-02":
                creada = new Mision_02(ac, destino, tablero);
                break;
            case "M-03":
                creada = new Mision_03(ac, destino, tablero);
                break;
            default:
                throw new IllegalArgumentException("Misión desconocida: " + codigo);
        }

        assert creada != null : "Postcondición violada: la fábrica devolvió una misión nula";
        return creada;
    }
}