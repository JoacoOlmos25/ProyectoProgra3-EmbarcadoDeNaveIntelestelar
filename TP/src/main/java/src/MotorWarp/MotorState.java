package src.MotorWarp;

import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;

import java.util.ArrayList;

/**
 * Contrato común para los estados operativos del Motor Warp (Patrón State).
 * Cada implementación encapsula el comportamiento y las transiciones válidas de su fase.
 */
public interface MotorState {

    /**
     * Ejecuta un desplazamiento normal o la acción correspondiente al estado actual del motor.
     * Pre: tablero != null, ruta != null, !ruta.isEmpty() (cuando el estado requiere desplazamiento).
     * Post: el motor avanza la nave o completa la fase pendiente (ej. enfriamiento) y actualiza su estado según corresponda.
     * @param tablero tablero donde se desplaza la nave
     * @param ruta lista mutable de posiciones pendientes hacia el objetivo
     */
    void saltar(Tablero tablero, ArrayList<Posicion> ruta);

    /**
     * Ejecuta el intento de Salto Warp según la fase en la que se encuentra el motor.
     * Pre: tablero != null, ruta != null, !ruta.isEmpty() (cuando el estado requiere desplazamiento).
     * Post: el motor Warp ejecuta polimórficamente la fase correspondiente del salto o cae en salto normal/enfriamiento.
     * @param tablero tablero donde se desplaza la nave
     * @param ruta lista mutable de posiciones pendientes hacia el objetivo
     * @throws MotorException si se produce una transición inválida o se cancela el salto
     */
    void saltarWarp(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException;

    /**
     * Prepara el salto: modifica el estado del motor Warp de "Disponible" a "Preparando salto".
     * Pre: ninguna.
     * Post: el motor Warp queda en estado "Preparando salto".
     * @throws MotorException si el estado inicial del motor no es "Disponible"
     */
    void prepararWarp() throws MotorException;

    /**
     * Inicia el salto: modifica el estado del motor Warp de "Preparando salto" a "En Warp".
     * Pre: ninguna.
     * Post: si la nave cuenta con energía suficiente, el motor queda en estado "En Warp"; de lo contrario permanece en "Preparando salto".
     * @throws MotorException si el estado inicial del motor no es "Preparando salto"
     */
    void iniciarWarp() throws MotorException;

    /**
     * Ejecuta el salto hiperespacial, mueve la nave hasta 2 casilleros y pasa a enfriamiento.
     * Pre: tablero != null, ruta != null, !ruta.isEmpty().
     * Post: si hay energía suficiente, la nave avanza hasta 2 casilleros y el motor pasa a "Enfriando"; si no, vuelve a "Preparando salto".
     * @param tablero tablero donde se desplaza la nave
     * @param ruta lista mutable de posiciones pendientes hacia el objetivo
     * @throws MotorException si el estado inicial del motor no es "En Warp"
     */
    void ejecutandoWarp(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException;

    /**
     * Finaliza el enfriamiento aplicando el desgaste del salto: pasa de "Enfriando" a "Disponible".
     * Pre: ninguna.
     * Post: se aplica el desgaste de enfriamiento y el motor queda en estado "Disponible".
     * @throws MotorException si el estado inicial del motor no es "Enfriando"
     */
    void enfriarWarp() throws MotorException;

    /**
     * Verifica si el motor Warp está disponible para iniciar una misión.
     * Pre: ninguna.
     * Post: no modifica el estado del motor.
     * @return true si el motor está en estado "Disponible", false en caso contrario
     */
    boolean motorDisponible();

    /**
     * Reinicia el motor al cerrar una misión: descarta la preparación o el salto pendiente y, si se estaba enfriando,
     * completa el enfriamiento aplicando su desgaste.
     * Pre: ninguna.
     * Post: el motor Warp queda en estado "Disponible" (salvo MantenimientoException no chequeada si el desgaste supera 100,
     *       en cuyo caso permanece en "Enfriando").
     */
    void reiniciarWarp();
}