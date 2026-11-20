package src.nave;

import src.AC.Bitacora;
import src.ElementosDelUniverso.Colisionable;
import src.ElementosDelUniverso.ElementoDelUniverso;
import src.MotorWarp.MotorException;
import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;

import java.util.ArrayList;

public interface Comandable extends FuenteDeRecursos, Mantenible, Colisionable {

    void setBitacora(Bitacora bitacora);

    boolean consultarMotorDisponible();

    void saltar(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException;

    void saltarWarp(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException;


}
