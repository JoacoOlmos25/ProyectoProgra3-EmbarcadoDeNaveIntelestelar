package src.Misiones;

import src.AC.Asistente;
import src.destinoytablero.Destino;
import src.destinoytablero.Tablero;

import java.util.Random;

/**
 * Misión M-02: Recolección de gemas en un punto de interés.
 */
public class Mision_02 extends Mision {

    private static final int REQ_COMBUSTIBLE = 4;
    private static final int REQ_ENERGIA = 10;
    private static final int REQ_DESGASTE = 4;
    private static final int MIN_GEMAS = 20;
    private static final int MAX_GEMAS = 100;

    private final Random azar = new Random();
    private int gemasRecolectadas = 0;

    /**
     * Crea una misión M-02 con sus costos base de despegue.
     * Precondiciones: ac != null, destino != null, tablero != null
     * Postcondiciones: misión inicializada con gemas recolectadas en 0
     */
    public Mision_02(Asistente ac, Destino destino, Tablero tablero) {
        super(REQ_COMBUSTIBLE, REQ_ENERGIA, REQ_DESGASTE, ac, destino, tablero);
    }

    /**
     * Extrae la recompensa del punto de interés.
     * Precondiciones: la nave se encuentra en el objetivo
     * Postcondiciones: MIN_GEMAS <= gemasRecolectadas <= MAX_GEMAS; queda registrado en la bitácora y en el informe
     */
    @Override
    protected void evaluarResultado() {
        assert objetivo != null && tablero.getPosicionNave().equals(objetivo)
                : "Precondición violada: la nave debe encontrarse en el objetivo para recolectar gemas";

        this.gemasRecolectadas = MIN_GEMAS + azar.nextInt(MAX_GEMAS - MIN_GEMAS + 1);

        assert gemasRecolectadas >= MIN_GEMAS && gemasRecolectadas <= MAX_GEMAS : "Recompensa de gemas fuera de rango";
        ac.registrarEvento("Recolección completada", "Se extrajeron " + gemasRecolectadas + " gemas");
        agregarAlInforme("Gemas recolectadas: " + gemasRecolectadas);
    }

    @Override
    protected String getNombre() {
        return "M-02 Recolección";
    }

    public int getGemasRecolectadas() {
        return gemasRecolectadas;
    }
}