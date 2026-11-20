package src.nave;

import src.ElementosDelUniverso.ColisionException;
import src.ElementosDelUniverso.Colisionable;

public class Ataque extends Nave {
    /**Contrato
     *Crea una nave de tipo exploradora con un combustible y energia inicial
     */

    public Ataque() {
        super(80,100);
    }

    @Override
    public void interactuar(Colisionable colisionado) throws ColisionException{
        colisionado.enfrentadoPorAtaque();
    }

    @Override
    public void enfrentadoPorAtaque(){
        //Pacto de paz
    }

    @Override
    public void enfrentadoPorCarguero() throws ColisionException{
        //Pierde
    }

    @Override
    public void enfrentadoPorExplorador(){
        //Gana
    }

}
