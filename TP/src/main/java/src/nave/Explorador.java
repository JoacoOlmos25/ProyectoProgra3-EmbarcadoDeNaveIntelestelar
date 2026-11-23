package src.nave;

import src.ElementosDelUniverso.ColisionException;
import src.ElementosDelUniverso.Colisionable;

public class Explorador extends Nave {

    /**
     * Contrato:
     * Crea una nave de tipo Explorador con sus reservas iniciales.
     * Precondición: ninguna.
     * Postcondición: combustible == 60, energia == 80, desgaste == 0 y motor Warp disponible.
     */
    public Explorador() {
        super(60, 80);
    }

    /**
     * Contrato:
     * Primer paso del Double Dispatch: notifica al colisionado que lo enfrenta un Explorador.
     * Precondición: colisionado != null
     * Postcondición: se resuelve el combate según el tipo real del colisionado.
     * @throws ColisionException si la nave colisionada resulta derrotada
     */
    @Override
    public void interactuar(Colisionable colisionado) throws ColisionException {
        assert colisionado != null : "El objeto colisionado no puede ser nulo";
        colisionado.enfrentadoPorExplorador();
    }

    /**
     * Contrato:
     * Segundo paso del Double Dispatch: Explorador pierde frente a Ataque.
     * Postcondición: se registra la derrota en la bitácora y se lanza ColisionException.
     */
    @Override
    public void enfrentadoPorAtaque() throws ColisionException {
        perderCombate("Ataque");
    }

    /**
     * Contrato:
     * Segundo paso del Double Dispatch: Explorador vence a Carguero.
     * Postcondición: se registra la victoria en la bitácora.
     */
    @Override
    public void enfrentadoPorCarguero() {
        ganarCombate("Carguero");
    }

    /**
     * Contrato:
     * Segundo paso del Double Dispatch: Explorador vs. Explorador pactan paz.
     * Postcondición: se registra el pacto de paz en la bitácora.
     */
    @Override
    public void enfrentadoPorExplorador() {
        pactarPaz("Explorador");
    }
}