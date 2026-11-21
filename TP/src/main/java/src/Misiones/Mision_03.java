package src.Misiones;

import src.AC.Asistente;
import src.destinoytablero.Destino;
import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;

//Retorno seguro a una zona designada
public class Mision_03 extends Mision{

    private static final int REQ_COMBUSTIBLE = 4;
    private static final int REQ_ENERGIA = 0;
    private static final int REQ_DESGASTE = 4;

    public Mision_03(Asistente ac, Destino destino, Tablero tablero) {
        super(REQ_COMBUSTIBLE, REQ_ENERGIA, REQ_DESGASTE, ac, destino, tablero);
    }

    /**
     * El objetivo de esta misión es la base de origen, no una posición al azar
     * Postcondiciones: la base de origen queda establecida como objetivo del tablero
     * @return posición de la base de origen
     */
    @Override
    protected Posicion definirObjetivo() {
        Posicion base = tablero.getPosicionBase();
        tablero.establecerObjetivo(base);
        return base;
    }

    /**
     * Reacondiciona la nave al regresar a la base
     * Precondiciones: la nave se encuentra en la base de origen
     * Postcondiciones: combustible y energía al 100% y desgaste en 0; queda registrado en la bitácora y en el informe
     */
    @Override
    protected void evaluarResultado() {
        ac.reacondicionarNave();
        ac.registrarEvento("Retorno a base", "Recarga total de combustible y energía, y mantenimiento general (desgaste 0)");
        agregarAlInforme("Reacondicionamiento: tanques al 100% y desgaste reiniciado a 0");
    }

    @Override
    protected String getNombre() {
        return "M-03 Retorno a punto seguro";
    }
}