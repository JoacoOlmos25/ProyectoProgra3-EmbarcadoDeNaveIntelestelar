package src.nave;

import src.ElementosDelUniverso.ColisionException;
import src.ElementosDelUniverso.Colisionable;

public class Carguero extends Nave{
    /**Contrato
     *Crea una nave de tipo carguero con un combustible y energia inicial
     */

    public Carguero(){
        super(100,60);
    }

    /**
     * Primer paso del Double Dispatch: le indica al colisionado que lo enfrenta una nave Carguero
     */
    @Override
    public void interactuar(Colisionable colisionado) throws ColisionException {
        colisionado.enfrentadoPorCarguero();
    }

    @Override
    public void enfrentadoPorAtaque(){
        ganarCombate("Ataque");
    }

    @Override
    public void enfrentadoPorCarguero(){
        pactarPaz("Carguero");
    }

    @Override
    public void enfrentadoPorExplorador() throws ColisionException {
        perderCombate("Explorador");
    }

}