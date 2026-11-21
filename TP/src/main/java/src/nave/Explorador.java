package src.nave;

import src.ElementosDelUniverso.ColisionException;
import src.ElementosDelUniverso.Colisionable;

public class Explorador extends Nave{
    /**Contrato
     *Crea una nave de tipo exploradora con un combustible y energia inicial
     */
    public Explorador(){
        super(60,80);
    }

    /**
     * Primer paso del Double Dispatch: le indica al colisionado que lo enfrenta una nave Exploradora
     */
    @Override
    public void interactuar(Colisionable colisionado) throws ColisionException {
        colisionado.enfrentadoPorExplorador();
    }

    @Override
    public void enfrentadoPorAtaque() throws ColisionException {
        perderCombate("Ataque");
    }

    @Override
    public void enfrentadoPorCarguero(){
        ganarCombate("Carguero");
    }

    @Override
    public void enfrentadoPorExplorador(){
        pactarPaz("Explorador");
    }

}