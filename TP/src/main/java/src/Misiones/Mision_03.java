package src.Misiones;

import src.AC.Asistente;
import src.destinoytablero.Destino;
import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;

/**
 * Misión M-03: Retorno seguro a la base de origen para reacondicionamiento total.
 */
public class Mision_03 extends Mision {

    private static final int REQ_COMBUSTIBLE = 4;
    private static final int REQ_ENERGIA = 0;
    private static final int REQ_DESGASTE = 4;

    /**
     * Crea una misión M-03 con sus costos base de despegue.
     * Precondiciones: ac != null, destino != null, tablero != null
     * Postcondiciones: misión inicializada lista para preparar
     */
    public Mision_03(Asistente ac, Destino destino, Tablero tablero) {
        super(REQ_COMBUSTIBLE, REQ_ENERGIA, REQ_DESGASTE, ac, destino, tablero);
    }

    /**
     * El objetivo de esta misión es la base de origen, no una posición al azar.
     * Postcondiciones: la base de origen queda establecida como objetivo del tablero
     * @return posición de la base de origen, NotNull
     */
    @Override
    protected Posicion definirObjetivo() {
        Posicion base = tablero.getPosicionBase();
        tablero.establecerObjetivo(base);
        assert base != null && base.equals(tablero.getObjetivo())
                : "Postcondición violada: la base no quedó establecida como objetivo del tablero";
        return base;
    }

    /**
     * Reacondiciona la nave al regresar a la base.
     * Precondiciones: la nave se encuentra en la base de origen
     * Postcondiciones: combustible y energía al 100% y desgaste en 0; queda registrado en la bitácora y en el informe
     */
    @Override
    protected void evaluarResultado() {
        assert objetivo != null && tablero.getPosicionNave().equals(objetivo)
                : "Precondición violada: la nave debe encontrarse en la base para reacondicionarse";

        ac.reacondicionarNave();

        assert ac.getCombustibleNave() == 100 && ac.getEnergiaNave() == 100 && ac.getDesgasteNave() == 0
                : "Postcondición violada: la nave no quedó completamente reacondicionada";
        ac.registrarEvento("Retorno a base", "Recarga total de combustible y energía, y mantenimiento general (desgaste 0)");
        agregarAlInforme("Reacondicionamiento: tanques al 100% y desgaste reiniciado a 0");
    }

    @Override
    protected String getNombre() {
        return "M-03 Retorno a punto seguro";
    }
}