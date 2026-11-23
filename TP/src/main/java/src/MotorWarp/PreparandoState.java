package src.MotorWarp;

import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;

import java.util.ArrayList;

/**
 * Estado "Preparando salto" del Motor Warp.
 * Completa la ignición hacia el estado En Warp (si hay energía) mientras realiza un salto normal.
 *
 * Invariante de clase:
 * - motorWarp != null
 */
public class PreparandoState implements MotorState {

    private final MotorWarp motorWarp;

    /**
     * Pre: motorWarp != null.
     * Post: el estado queda asociado al motor y registra su ingreso a Preparando.
     */
    public PreparandoState(MotorWarp motorWarp) {
        assert motorWarp != null : "El MotorWarp no puede ser nulo";
        this.motorWarp = motorWarp;
        this.motorWarp.registrarEventoMotor("Cambio de estado del Motor Warp", "El Motor warp se está preparando para el salto");
    }

    @Override
    public void saltar(Tablero tablero, ArrayList<Posicion> ruta) {
        assert tablero != null && ruta != null && !ruta.isEmpty() : "Tablero y ruta válida requeridos para saltar";
        this.iniciarWarp();
        this.motorWarp.saltoNormal(tablero, ruta);
    }

    @Override
    public void saltarWarp(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException {
        // Warp todavía no listo: avanza la preparación y realiza salto normal
        this.saltar(tablero, ruta);
    }

    @Override
    public void prepararWarp() throws TransicionRedundanteException {
        throw new TransicionRedundanteException("Preparación de salto ya en curso");
    }

    @Override
    public void iniciarWarp() {
        if (motorWarp.energiaSuficiente()) {
            motorWarp.setEstado(new EjecutandoSaltoWarpState(motorWarp));
        } else {
            motorWarp.registrarEventoMotor(
                    "Ignición Warp fallida",
                    "Energía insuficiente para iniciar el salto Warp."
            );
        }
    }

    @Override
    public void ejecutandoWarp(Tablero tablero, ArrayList<Posicion> ruta) throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Preparando salto: no se puede ejecutar el salto todavía");
    }

    @Override
    public void enfriarWarp() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Preparando salto: no corresponde enfriamiento");
    }

    @Override
    public boolean motorDisponible() {
        return false;
    }

    @Override
    public void reiniciarWarp() {
        // Se descarta la preparación pendiente
        motorWarp.setEstado(new DisponibleState(motorWarp));
    }
}