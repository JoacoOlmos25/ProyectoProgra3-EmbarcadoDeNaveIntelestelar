package src.MotorWarp;

import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;

import java.util.ArrayList;

public interface MotorState {

    void saltar(Tablero tablero, ArrayList<Posicion> ruta);

    /**
     * Elige la fase a ejecutar según el estado en el que se encuentra el motor en el momento de invocación.
     * Pre: ninguna.
     * Post: el motor Warp, mediante su estado, ejecuta polimórficamente el código correspondiente según la fase del Salto Warp en la que se encuentra
     * @throws MotorException si hay transición inválida o se cancela el salto
     */
    void saltarWarp(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException;

    /**
     * Prepara el salto: modifica el estado del motor Warp de "Disponible" a "Preparando salto"
     * Post: el motor Warp queda en estado "Preparando Salto"
     * @throws MotorException si el estado inicial del motor no es "Disponible"
     */
    void prepararWarp() throws MotorException;

    /**
     * Inicia el salto: modifica el estado del motor Warp de "Preparando salto" a "En Warp"
     * Post: el motor Warp queda en estado "En Warp"
     * @throws MotorException si el estado inicial del motor no es "Preparando salto"
     */
    void iniciarWarp() throws MotorException;

    /**
     * Si puede realiza el salto, mueve la nave y comienza enfriamiento: modifica el estado del motor Warp de "En Warp" a "Enfriando"
     * Post: el motor Warp queda en estado "Enfriando"
     * @throws MotorException si el estado inicial del motor no es "En Warp"
     */
    void ejecutandoWarp(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException;

    /**
     * Finaliza enfriamiento: modifica el estado del motor Warp de "Enfriando" a "Disponible"
     * Post: el motor Warp queda en estado "Disponible"
     * @throws MotorException si el estado inicial del motor no es "Enfriando"
     */
    void enfriarWarp() throws MotorException;

    /**
     * Verifica si el motor Warp está disponible
     * @return boolean, true si está disponible, false en caso contrario
     */
    boolean motorDisponible();
}
