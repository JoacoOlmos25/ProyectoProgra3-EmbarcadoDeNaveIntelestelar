package src.nave;

import src.ElementosDelUniverso.ColisionException;
import src.ElementosDelUniverso.Colisionable;

public class Ataque extends Nave {
    /**Contrato
     *Crea una nave de tipo ataque con un combustible y energia inicial
     */

    public Ataque() {
        super(80,100);
    }

    /**
     * Primer paso del Double Dispatch: le indica al colisionado que lo enfrenta una nave de Ataque
     */
    @Override
    public void interactuar(Colisionable colisionado) throws ColisionException{
        colisionado.enfrentadoPorAtaque();
    }

    @Override
    public void enfrentadoPorAtaque(){
        pactarPaz("Ataque");
    }

    @Override
    public void enfrentadoPorCarguero() throws ColisionException{
        perderCombate("Carguero");
    }

    @Override
    public void enfrentadoPorExplorador(){
        ganarCombate("Explorador");
    }

}