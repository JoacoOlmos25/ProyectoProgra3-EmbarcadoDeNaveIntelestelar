package src.nave;

import src.AC.Bitacora;
import src.ElementosDelUniverso.Colisionable;
import src.MotorWarp.MotorException;
import src.Tripulacion.Tripulante;
import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;

import java.util.ArrayList;
import java.util.List;

public interface Comandable extends FuenteDeRecursos, Mantenible, Colisionable {

    void setBitacora(Bitacora bitacora);

    boolean consultarMotorDisponible();

    void saltar(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException;

    void saltarWarp(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException;

    void reacondicionamientoTotal();

    void reiniciarMotor();

    /**
     * Incorpora un tripulante a la nave preservando identidad única y capitán único.
     * Precondición: t != null
     * Postcondición: el tripulante queda a bordo y registrado en la bitácora
     * @throws NoexisteTripulanteException si el tripulante es nulo
     * @throws IllegalArgumentException si ya existe un tripulante con esa identidad o ya hay un capitán a bordo
     */
    void insertarTripulante(Tripulante t);

    /**
     * Consulta la tripulación de la nave.
     * Postcondición: devuelve una lista inmodificable; el estado de la nave no cambia
     * @return tripulación a bordo
     */
    List<Tripulante> getTripulacion();
}