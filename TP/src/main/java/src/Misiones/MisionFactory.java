package src.Misiones;

import src.AC.Asistente;
import src.destinoytablero.Destino;
import src.destinoytablero.Tablero;

public class MisionFactory {

    /**Contrato
     *Instancia la misión según cuál se requiera
     *Precondicion
     *codigo debe ser "M-01", "M-02" o "M-03" (sin distinguir mayúsculas ni espacios)
     *ac, destino y tablero no nulos
     *Postcondicion
     *Devuelve la misión creada, lista para realizarse
     */
    public Mision crearMision(String codigo, Asistente ac, Destino destino, Tablero tablero){
        assert codigo != null : "El código de misión no puede ser nulo";

        switch (codigo.trim().toUpperCase()){
            case "M-01": return new Mision_01(ac, destino, tablero);
            case "M-02": return new Mision_02(ac, destino, tablero);
            case "M-03": return new Mision_03(ac, destino, tablero);
            default:
                throw new IllegalArgumentException("Misión desconocida: " + codigo);
        }
    }
}