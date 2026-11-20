package src.ElementosDelUniverso;

public class Asteroide implements ElementoDelUniverso {

    private static final int DANIO = 20;

    public void interactuar(Colisionable colisionado) throws ColisionException {
        colisionado.recibirDanio(Asteroide.DANIO);
    }
}
