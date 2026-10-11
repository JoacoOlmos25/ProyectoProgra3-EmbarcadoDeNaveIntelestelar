package src.MotorWarp;

public interface MotorState {

    /**
     * Prepara el salto: modifica el estado del motor Warp de "Disponible" a "Preparando salto"
     * Post: el motor Warp queda en estado "Preparando Salto"
     * @throws MotorException si el estado inicial del motor no es "Disponible"
     */
    void prepararSalto() throws MotorException;

    /**
     * Inicia el salto: modifica el estado del motor Warp de "Preparando salto" a "En Warp"
     * Post: el motor Warp queda en estado "En Warp"
     * @throws MotorException si el estado inicial del motor no es "Preparando salto"
     */
    void saltar() throws MotorException;

    /**
     * Finaliza el salto y comienza enfriamiento: modifica el estado del motor Warp de "En Warp" a "Enfriando"
     * Post: el motor Warp queda en estado "Enfriando"
     * @throws MotorException si el estado inicial del motor no es "En Warp"
     */
    void finalizarSalto() throws MotorException;

    /**
     * Finaliza enfriamiento: modifica el estado del motor Warp de "Enfriando" a "Disponible"
     * Post: el motor Warp queda en estado "Disponible"
     * @throws MotorException si el estado inicial del motor no es "Enfriando"
     */
    void hacerDisponible() throws MotorException;

    /**
     * Cancela salto: modifica el estado del motor Warp de "En Warp" a "Disponible"
     * Post: el motor Warp queda en estado "Disponible"
     * @throws MotorException si el estado inicial del motor no es "En Warp"
     */
    void cancelarSalto() throws MotorException;


    /**
     * Verifica si el motor Warp está disponible
     * @return boolean, true si está disponible, false en caso contrario
     */
    boolean motorDisponible();
}
