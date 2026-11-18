package src.nave;

public interface FuenteDeRecursos {

    int getCombustible();

    int getEnergia();

    void consumirCombustible(int cantidad) throws RecursosInsuficientesException;

    void consumirEnergia(int cantidad) throws RecursosInsuficientesException;
}
