package src.ElementosDelUniverso;

public interface Colisionable extends ElementoDelUniverso{

    void recibirDanio(int cantidad) throws ColisionException;

    void recibirAyuda(int cantidadCombustible, int cantidadEnergia);

    void enfrentadoPorExplorador() throws ColisionException;

    void enfrentadoPorCarguero() throws ColisionException;

    void enfrentadoPorAtaque() throws ColisionException;
}
