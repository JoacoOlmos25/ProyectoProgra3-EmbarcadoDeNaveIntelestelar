package src.nave;

import src.ElementosDelUniverso.ColisionException;
import src.ElementosDelUniverso.Colisionable;

public class Ataque extends Nave {

    /**
     * Contrato:
     * Crea una nave de tipo Ataque con sus reservas iniciales.
     * Precondición: ninguna.
     * Postcondición: combustible == 80, energia == 100, desgaste == 0 y motor Warp disponible.
     */
    public Ataque() {
        super(80, 100);
    }

    /**
     * Contrato:
     * Primer paso del Double Dispatch: notifica al colisionado que lo enfrenta una nave de Ataque.
     * Precondición: colisionado != null
     * Postcondición: se resuelve el combate según el tipo real del colisionado.
     * @throws ColisionException si la nave colisionada resulta derrotada
     */
    @Override
    public void interactuar(Colisionable colisionado) throws ColisionException {
        assert colisionado != null : "El objeto colisionado no puede ser nulo";
        colisionado.enfrentadoPorAtaque();
    }

    /**
     * Contrato:
     * Segundo paso del Double Dispatch: Ataque vs. Ataque pactan paz.
     * Postcondición: se registra el pacto de paz en la bitácora.
     */
    @Override
    public void enfrentadoPorAtaque() {
        pactarPaz("Ataque");
    }

    /**
     * Contrato:
     * Segundo paso del Double Dispatch: Ataque pierde frente a Carguero.
     * Postcondición: se registra la derrota en la bitácora y se lanza ColisionException.
     */
    @Override
    public void enfrentadoPorCarguero() throws ColisionException {
        perderCombate("Carguero");
    }

    /**
     * Contrato:
     * Segundo paso del Double Dispatch: Ataque vence a Explorador.
     * Postcondición: se registra la victoria en la bitácora.
     */
    @Override
    public void enfrentadoPorExplorador() {
        ganarCombate("Explorador");
    }
}