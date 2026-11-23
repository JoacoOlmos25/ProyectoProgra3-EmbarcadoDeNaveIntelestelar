package src.MotorWarp;

import src.AC.Bitacora;
import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;
import src.nave.FuenteDeRecursos;
import src.nave.Mantenible;
import src.nave.RecursosInsuficientesException;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Contexto del patrón State para la propulsión de la nave.
 * Expone al exterior las operaciones de desplazamiento, consulta de disponibilidad, reinicio y vinculación de bitácora,
 * delegando las reglas y transiciones en su estado actual.
 *
 * Invariante de clase:
 * - estado != null
 * - recursos != null
 * - estructuraNave != null
 */
public class MotorWarp {

    public static final int COSTO_WARP_ENERGIA = 5;
    public static final int COSTO_WARP_DESGASTE = 3;
    public static final int COSTO_SALTO_COMBUSTIBLE = 2;

    private MotorState estado;
    private Bitacora bitacora;
    private final FuenteDeRecursos recursos;
    private final Mantenible estructuraNave;

    /**
     * Inicializa el Motor Warp en estado disponible, vinculado a los recursos y estructura de la nave.
     * @param recursos proveedor de combustible y energía de la nave, NotNull
     * @param estructuraNave componente mantenible sobre el que impacta el desgaste, NotNull
     *
     * Precondiciones: recursos != null && estructuraNave != null
     * Postcondiciones: el motor queda en estado Disponible (consultaMotorDisponible() == true)
     */
    public MotorWarp(FuenteDeRecursos recursos, Mantenible estructuraNave) {
        assert recursos != null : "La fuente de recursos no puede ser nula";
        assert estructuraNave != null : "La estructura mantenible no puede ser nula";

        this.recursos = recursos;
        this.estructuraNave = estructuraNave;
        this.estado = new DisponibleState(this);

        assert consultaMotorDisponible() : "Postcondición violada: el motor debe iniciar en estado Disponible";
        assert invariante() : "Invariante de MotorWarp violado tras construcción";
    }

    private boolean invariante() {
        return this.estado != null && this.recursos != null && this.estructuraNave != null;
    }

    /**
     * Solicita un salto normal al estado actual del motor.
     * @param tablero tablero donde se desplaza la nave, NotNull
     * @param ruta lista de posiciones pendientes hacia el objetivo, NotNull
     *
     * Precondiciones: tablero != null && ruta != null
     * Postcondiciones: se ejecuta la acción correspondiente al estado vigente y se preserva el invariante
     * @throws MotorException si ocurre una falla de transición en el estado actual
     */
    public void saltar(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException {
        assert tablero != null && ruta != null : "El tablero y la ruta no pueden ser nulos";
        estado.saltar(tablero, ruta);
        assert invariante() : "Invariante de MotorWarp violado tras saltar";
    }

    /**
     * Solicita un Salto Warp al estado actual del motor.
     * @param tablero tablero donde se desplaza la nave, NotNull
     * @param ruta lista de posiciones pendientes hacia el objetivo, NotNull
     *
     * Precondiciones: tablero != null && ruta != null
     * Postcondiciones: se ejecuta la fase correspondiente del ciclo Warp y se preserva el invariante
     * @throws MotorException si ocurre una transición inválida o se cancela el salto
     */
    public void saltarWarp(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException {
        assert tablero != null && ruta != null : "El tablero y la ruta no pueden ser nulos";
        estado.saltarWarp(tablero, ruta);
        assert invariante() : "Invariante de MotorWarp violado tras saltarWarp";
    }

    /**
     * Consulta si el motor se encuentra en estado Disponible.
     * Precondiciones: ninguna
     * Postcondiciones: no modifica el estado interno
     * @return true si el motor está disponible, false en caso contrario
     */
    public boolean consultaMotorDisponible() {
        return estado.motorDisponible();
    }

    /**
     * Devuelve el motor al estado disponible al cerrar una misión.
     * Precondiciones: ninguna
     * Postcondiciones: consultaMotorDisponible() == true, salvo que el enfriamiento pendiente supere el desgaste permitido
     */
    public void reiniciar() {
        estado.reiniciarWarp();
        assert consultaMotorDisponible() : "Postcondición violada: el motor no quedó disponible tras reiniciar";
        assert invariante() : "Invariante de MotorWarp violado tras reiniciar";
    }

    /**
     * Vincula la bitácora donde el motor registra sus cambios de estado y saltos.
     * @param b bitácora de la nave, NotNull
     *
     * Precondiciones: b != null
     * Postcondiciones: el motor queda asociado a la bitácora y registra su conexión
     */
    public void setBitacora(Bitacora b) {
        assert b != null : "La bitácora del motor no puede ser nula";
        this.bitacora = b;
        this.registrarEventoMotor(
                "Motor Warp conectado",
                "Motor inicializado. Disponible: " + this.consultaMotorDisponible()
        );
        assert invariante() : "Invariante de MotorWarp violado tras setBitacora";
    }

    // =========================================================================
    // COLABORACIÓN INTERNA CON LOS ESTADOS (visibilidad de paquete src.MotorWarp)
    // =========================================================================

    /**
     * Verifica si la nave dispone de energía suficiente para ejecutar un salto Warp sin quedar en 0.
     * Precondiciones: ninguna
     * Postcondiciones: no modifica el estado
     */
    boolean energiaSuficiente() {
        return recursos.getEnergia() > COSTO_WARP_ENERGIA;
    }

    /**
     * Descuenta la energía requerida para un Salto Warp.
     * Precondiciones: la nave cuenta con energía suficiente
     * Postcondiciones: la energía disminuye en COSTO_WARP_ENERGIA
     */
    void consumeEnergiaPorSalto() {
        recursos.consumirEnergia(MotorWarp.COSTO_WARP_ENERGIA);
    }

    /**
     * Incrementa el desgaste estructural tras el enfriamiento de un Salto Warp.
     * Precondiciones: ninguna
     * Postcondiciones: el desgaste aumenta en COSTO_WARP_DESGASTE (o lanza MantenimientoException si supera 100)
     */
    void aplicaDesgastePorSalto() {
        estructuraNave.incrementaDesgaste(COSTO_WARP_DESGASTE);
    }

    /**
     * Cambia el estado interno del motor.
     * @param estado nuevo estado del motor, NotNull
     *
     * Precondiciones: estado != null
     * Postcondiciones: this.estado == estado
     */
    void setEstado(MotorState estado) {
        assert estado != null : "El nuevo estado del motor no puede ser nulo";
        this.estado = estado;
        assert this.estado == estado : "Postcondición violada al cambiar de estado";
        assert invariante() : "Invariante de MotorWarp violado tras setEstado";
    }

    /**
     * Realiza un salto normal: consume el combustible del salto, avanza un casillero y registra el evento.
     * @param tablero tablero donde se mueve la nave, NotNull
     * @param ruta posiciones pendientes de la ruta, NotNull y no vacía
     *
     * Precondiciones: tablero != null && ruta != null && !ruta.isEmpty()
     * Postcondiciones: el combustible disminuye en COSTO_SALTO_COMBUSTIBLE, se consume la primera posición de la ruta
     *                  y la nave queda ubicada en ella
     * @throws RecursosInsuficientesException si la nave no tiene combustible para el salto (la nave no se mueve)
     */
    void saltoNormal(Tablero tablero, ArrayList<Posicion> ruta) {
        assert tablero != null && ruta != null : "El tablero y la ruta no pueden ser nulos";
        assert !ruta.isEmpty() : "No hay posiciones pendientes en la ruta para realizar un salto normal";

        recursos.consumirCombustible(COSTO_SALTO_COMBUSTIBLE);
        this.mover(tablero, ruta, 1);
        this.registrarEventoMotor(
                "Salto Normal ejecutado",
                "Avance de 1 casillero. Combustible consumido: " + COSTO_SALTO_COMBUSTIBLE
        );
        assert invariante() : "Invariante de MotorWarp violado tras saltoNormal";
    }

    /**
     * Avanza la nave sobre la ruta la cantidad de pasos indicada, consumiendo las posiciones recorridas.
     * @param tablero tablero donde se mueve la nave, NotNull
     * @param ruta posiciones pendientes de la ruta, NotNull
     * @param pasos cantidad de casilleros a avanzar
     *
     * Precondiciones: tablero != null && ruta != null && pasos > 0 && !ruta.isEmpty()
     * Postcondiciones: se quitan de la ruta min(pasos, ruta.size()) posiciones y la nave queda en la última de ellas
     */
    void mover(Tablero tablero, ArrayList<Posicion> ruta, int pasos) {
        assert tablero != null && ruta != null : "El tablero y la ruta no pueden ser nulos";
        assert pasos > 0 : "La cantidad de pasos debe ser positiva";
        assert !ruta.isEmpty() : "No hay posiciones pendientes en la ruta";

        int tamAnterior = ruta.size();
        int pasosReales = Math.min(pasos, tamAnterior);
        Posicion posicionSiguiente = null;
        for (int i = 0; i < pasosReales; i++) {
            posicionSiguiente = ruta.remove(0);
        }

        tablero.moverNave(posicionSiguiente);

        assert ruta.size() == tamAnterior - pasosReales : "Postcondición violada: cantidad de posiciones removidas inconsistente";
        assert tablero.getPosicionNave().equals(posicionSiguiente) : "Postcondición violada: la nave no quedó en la posición esperada";
    }

    /**
     * Registra un evento del motor en la bitácora si esta fue vinculada.
     * Precondiciones: titulo != null && !titulo.isBlank() && msj != null
     * Postcondiciones: si bitacora != null, se agrega el evento con la marca temporal actual
     */
    void registrarEventoMotor(String titulo, String msj) {
        assert titulo != null && !titulo.isBlank() : "El título del evento de motor no puede ser nulo ni vacío";
        assert msj != null : "El mensaje del evento de motor no puede ser nulo";

        if (this.bitacora != null) {
            this.bitacora.agregaEvento(LocalDateTime.now(), titulo, msj);
        }
    }
}