package src.nave;

import src.ElementosDelUniverso.ColisionException;
import src.ElementosDelUniverso.Colisionable;

public class Carguero extends Nave{
    /**Contrato
     *Crea una nave de tipo exploradora con un combustible y energia inicial
     */

    public Carguero(){
        super(100,60);
    }

    @Override
    public void interactuar(Colisionable colisionado) throws ColisionException {
        colisionado.enfrentadoPorCarguero();
    }

    @Override
    public void enfrentadoPorAtaque(){
        //Gana
    }

    @Override
    public void enfrentadoPorCarguero(){
        //Pacto de paz
    }

    @Override
    public void enfrentadoPorExplorador() throws ColisionException {
        //Pierde
    }

}
