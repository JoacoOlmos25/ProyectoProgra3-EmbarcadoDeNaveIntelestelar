package src.nave;

import src.ElementosDelUniverso.ColisionException;
import src.ElementosDelUniverso.Colisionable;

public class Carguero extends Nave {

    /**
     * Contrato:
     * Crea una nave de tipo Carguero con sus reservas iniciales.
     * Precondición: ninguna.
     * Postcondición: combustible == 100, energia == 60, desgaste == 0 y motor Warp disponible.
     */
    public Carguero() {
        super(100, 60);
    }

    /**
     * Contrato:
     * Primer paso del Double Dispatch: notifica al colisionado que lo enfrenta un Carguero.
     * Precondición: colisionado != null
     * Postcondición: se resuelve el combate según el tipo real del colisionado.
     * @throws ColisionException si la nave colisionada resulta derrotada
     */
    @Override
    public void interactuar(Colisionable colisionado) throws ColisionException {
        assert colisionado != null : "El objeto colisionado no puede ser nulo";
        colisionado.enfrentadoPorCarguero();
    }

    /**
     * Contrato:
     * Segundo paso del Double Dispatch: Carguero vence a Ataque.
     * Postcondición: se registra la victoria en la bitácora.
     */
    @Override
    public void enfrentadoPorAtaque() {
        ganarCombate("Ataque");
    }

    /**
     * Contrato:
     * Segundo paso del Double Dispatch: Carguero vs. Carguero pactan paz.
     * Postcondición: se registra el pacto de paz en la bitácora.
     */
    @Override
    public void enfrentadoPorCarguero() {
        pactarPaz("Carguero");
    }

    /**
     * Contrato:
     * Segundo paso del Double Dispatch: Carguero pierde frente a Explorador.
     * Postcondición: se registra la derrota en la bitácora y se lanza ColisionException.
     */
    @Override
    public void enfrentadoPorExplorador() throws ColisionException {
        perderCombate("Explorador");
    }
}