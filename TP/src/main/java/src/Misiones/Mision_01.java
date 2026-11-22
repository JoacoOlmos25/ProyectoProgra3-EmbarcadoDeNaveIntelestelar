package src.Misiones;
//Intercepción y asistencia

import src.AC.Asistente;
import src.destinoytablero.Destino;
import src.destinoytablero.Tablero;
import src.nave.RecursosInsuficientesException;

import java.util.Random;

public class Mision_01 extends Mision {

    private static final int REQ_COMBUSTIBLE = 4;
    private static final int REQ_ENERGIA = 5;
    private static final int REQ_DESGASTE = 4;
    /** Máximo de cada recurso que puede solicitar la nave aliada. */
    private static final int MAX_AUXILIO = 50;

    private final Random azar = new Random();
    private int combustibleTransferido = 0;
    private int energiaTransferida = 0;

    public Mision_01(Asistente ac, Destino destino, Tablero tablero) {
        super(REQ_COMBUSTIBLE, REQ_ENERGIA, REQ_DESGASTE, ac, destino, tablero);
    }

    /**
     * Transfiere a la nave aliada los recursos de auxilio solicitados
     * Precondiciones: la nave se encuentra en la coordenada de la nave aliada (objetivo)
     * Postcondiciones: la nave aliada solicita entre 0 y MAX_AUXILIO de combustible y entre 0 y MAX_AUXILIO de energía,
     *                  que se descuentan de la nave; queda registrado en la bitácora y en el informe
     * @throws MisionException si la nave no cuenta con los recursos solicitados (no se transfiere nada)
     */
    @Override
    protected void evaluarResultado() throws MisionException {
        int combustibleSolicitado = azar.nextInt(MAX_AUXILIO + 1);
        int energiaSolicitada = azar.nextInt(MAX_AUXILIO + 1);

        try {
            ac.transferirRecursos(combustibleSolicitado, energiaSolicitada);
        } catch (RecursosInsuficientesException e) {
            throw new MisionException("No se pudo asistir a la nave aliada (solicitó " + combustibleSolicitado
                    + " de combustible y " + energiaSolicitada + " de energía)");
        }

        this.combustibleTransferido = combustibleSolicitado;
        this.energiaTransferida = energiaSolicitada;
        ac.registrarEvento("Asistencia completada", "Se transfirieron " + combustibleTransferido
                + " de combustible y " + energiaTransferida + " de energía a la nave aliada");
        agregarAlInforme("Auxilio transferido -> Combustible: " + combustibleTransferido + " | Energía: " + energiaTransferida);
    }

    @Override
    protected String getNombre() {
        return "M-01 Intercepción y asistencia";
    }

    public int getCombustibleTransferido() {
        return combustibleTransferido;
    }

    public int getEnergiaTransferida() {
        return energiaTransferida;
    }
}