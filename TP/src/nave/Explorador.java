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

    @Override
    public void interactuar(Colisionable colisionado) throws ColisionException {
        colisionado.enfrentadoPorExplorador();
    }

    @Override
    public void enfrentadoPorAtaque() throws ColisionException {
        //Pierde
    }

    @Override
    public void enfrentadoPorCarguero(){
        //Gana
    }

    @Override
    public void enfrentadoPorExplorador(){
        //Pacto de paz
    }

}
