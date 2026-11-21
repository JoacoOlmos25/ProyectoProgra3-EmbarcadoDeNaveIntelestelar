package src.ElementosDelUniverso;

public class EstacionDeCarga implements ElementoDelUniverso {

    private static final int COMBUSTIBLE_OTORGADO = 30;
    private static final int ENERGIA_OTORGADA = 30;

    public void interactuar(Colisionable colisionado){
        colisionado.recibirAyuda(EstacionDeCarga.COMBUSTIBLE_OTORGADO, EstacionDeCarga.ENERGIA_OTORGADA);
    }
}
