package src.nave;

public interface FuenteDeRecursos {

    int getCombustible();

    int getEnergia();

    void consumirCombustible(int cantidad) throws RecursosInsuficientesException;

    void consumirEnergia(int cantidad) throws RecursosInsuficientesException;

    void cargarCombustible(int cantidad) throws ExcesoRecursoException;

    void cargarEnergia(int cantidad) throws ExcesoRecursoException;
}
