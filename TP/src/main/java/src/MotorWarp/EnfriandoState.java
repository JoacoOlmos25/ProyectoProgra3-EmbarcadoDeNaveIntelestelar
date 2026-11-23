package src.MotorWarp;

import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;

import java.util.ArrayList;

/**
 * Estado "Enfriando" del Motor Warp.
 * Consume un turno de enfriamiento (sin desplazar la nave), aplica el desgaste del salto y vuelve a Disponible.
 *
 * Invariante de clase:
 * - motorWarp != null
 */
public class EnfriandoState implements MotorState {

    private final MotorWarp motorWarp;

    /**
     * Pre: motorWarp != null.
     * Post: el estado queda asociado al motor y registra su ingreso a Enfriando.
     */
    public EnfriandoState(MotorWarp motorWarp) {
        assert motorWarp != null : "El MotorWarp no puede ser nulo";
        this.motorWarp = motorWarp;
        this.motorWarp.registrarEventoMotor("Cambio de estado del Motor Warp", "El Motor warp se está enfriando luego de un salto");
    }

    @Override
    public void saltar(Tablero tablero, ArrayList<Posicion> ruta) {
        assert tablero != null && ruta != null : "El tablero y la ruta no pueden ser nulos";
        this.enfriarWarp();
    }

    @Override
    public void saltarWarp(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException {
        assert tablero != null && ruta != null : "El tablero y la ruta no pueden ser nulos";
        this.enfriarWarp();
    }

    @Override
    public void prepararWarp() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Motor enfriándose: todavía no se puede preparar un nuevo salto");
    }

    @Override
    public void iniciarWarp() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Motor enfriándose: todavía no se puede iniciar un salto");
    }

    @Override
    public void ejecutandoWarp(Tablero tablero, ArrayList<Posicion> ruta) throws TransicionRedundanteException {
        throw new TransicionRedundanteException("Salto ya finalizado, enfriamiento ya en curso");
    }

    @Override
    public void enfriarWarp() {
        motorWarp.aplicaDesgastePorSalto();
        motorWarp.registrarEventoMotor(
                "Enfriamiento de Motor Warp completado",
                "Desgaste estructural incrementado en " + MotorWarp.COSTO_WARP_DESGASTE + " unidades."
        );
        motorWarp.setEstado(new DisponibleState(motorWarp));
    }

    @Override
    public boolean motorDisponible() {
        return false;
    }

    @Override
    public void reiniciarWarp() {
        // Completa el enfriamiento pendiente (aplica el desgaste y vuelve a Disponible)
        this.enfriarWarp();
    }
}