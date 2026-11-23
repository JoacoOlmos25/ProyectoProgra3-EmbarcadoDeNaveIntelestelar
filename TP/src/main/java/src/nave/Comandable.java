package src.nave;

import src.AC.Bitacora;
import src.ElementosDelUniverso.Colisionable;
import src.MotorWarp.MotorException;
import src.Tripulacion.Tripulante;
import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;

import java.util.ArrayList;
import java.util.List;

/**
 * Interfaz operativa de una nave comandable por el Asistente.
 * Agrupa gestión de recursos, mantenimiento, colisiones, propulsión y tripulación.
 */
public interface Comandable extends FuenteDeRecursos, Mantenible, Colisionable {

    /**
     * Vincula la bitácora donde la nave y su motor registrarán los eventos.
     * Precondición: bitacora != null
     * Postcondición: la nave y su motor quedan asociados a la bitácora indicada.
     */
    void setBitacora(Bitacora bitacora);

    /**
     * Consulta si el motor Warp de la nave se encuentra disponible.
     * Postcondición: no modifica el estado de la nave.
     * @return true si el motor está en estado Disponible, false en caso contrario
     */
    boolean consultarMotorDisponible();

    /**
     * Ejecuta un desplazamiento normal a través del motor Warp.
     * Precondición: tablero != null && ruta != null
     * Postcondición: se delega el salto normal al motor Warp sobre el tablero y la ruta.
     * @throws MotorException si ocurre un error de transición en el motor
     */
    void saltar(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException;

    /**
     * Ejecuta un intento de salto hiperespacial a través del motor Warp.
     * Precondición: tablero != null && ruta != null
     * Postcondición: se delega el salto Warp al motor sobre el tablero y la ruta.
     * @throws MotorException si ocurre una transición inválida o se cancela el salto
     */
    void saltarWarp(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException;

    /**
     * Recarga al máximo combustible y energía y reinicia el desgaste a 0 (M-03).
     * Precondición: ninguna.
     * Postcondición: combustible y energía al máximo y desgaste == 0.
     */
    void reacondicionamientoTotal();

    /**
     * Devuelve el motor Warp al estado disponible al cerrar una misión.
     * Precondición: ninguna.
     * Postcondición: consultarMotorDisponible() == true (salvo MantenimientoException si el enfriamiento excede 100).
     */
    void reiniciarMotor();

    /**
     * Incorpora un tripulante a la nave preservando identidad única y capitán único.
     * Precondición: t != null
     * Postcondición: el tripulante queda a bordo y registrado en la bitácora.
     * @throws NoexisteTripulanteException si el tripulante es nulo
     * @throws IllegalArgumentException si ya existe un tripulante con esa identidad o ya hay un capitán a bordo
     */
    void insertarTripulante(Tripulante t);

    /**
     * Consulta la tripulación de la nave.
     * Postcondición: devuelve una lista inmodificable; el estado de la nave no cambia.
     * @return tripulación a bordo
     */
    List<Tripulante> getTripulacion();
}