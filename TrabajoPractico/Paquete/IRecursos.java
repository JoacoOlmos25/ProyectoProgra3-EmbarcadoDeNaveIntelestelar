package Paquete;

public interface IRecursos {

    int getCombustible();
    void cargarCombustible(int combustible);
    void consumirCombustible(int combustible);

    int getEnergia();
    void cargarEnergia(int energia);
    void consumirEnergia(int energia);

}
