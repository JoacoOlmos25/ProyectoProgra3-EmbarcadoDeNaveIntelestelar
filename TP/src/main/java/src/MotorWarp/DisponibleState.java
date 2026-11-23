package src.MotorWarp;

import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;

import java.util.ArrayList;

/**
 * Estado "Disponible" del Motor Warp.
 * Al recibir una orden de salto, inicia la preparación del Warp y ejecuta un salto normal.
 *
 * Invariante de clase:
 * - motorWarp != null
 */
public class DisponibleState implements MotorState {

    private final MotorWarp motorWarp;

    /**
     * Pre: motorWarp != null.
     * Post: el estado queda asociado al motor y registra su ingreso a Disponible.
     */
    public DisponibleState(MotorWarp motorWarp) {
        assert motorWarp != null : "El MotorWarp no puede ser nulo";
        this.motorWarp = motorWarp;
        this.motorWarp.registrarEventoMotor("Cambio de estado del Motor Warp", "El Motor warp está disponible y en espera");
    }

    @Override
    public void saltar(Tablero tablero, ArrayList<Posicion> ruta) {
        assert tablero != null && ruta != null && !ruta.isEmpty() : "Tablero y ruta válida requeridos para saltar";
        this.prepararWarp();
        this.motorWarp.saltoNormal(tablero, ruta);
    }

    @Override
    public void saltarWarp(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException {
        // Warp no inicializado: inicia preparación y realiza salto normal
        this.saltar(tablero, ruta);
    }

    @Override
    public void prepararWarp() {
        motorWarp.setEstado(new PreparandoState(motorWarp));
    }

    @Override
    public void iniciarWarp() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("No se puede iniciar salto Warp sin antes preparar");
    }

    @Override
    public void ejecutandoWarp(Tablero tablero, ArrayList<Posicion> ruta) throws TransicionInvalidaException {
        throw new TransicionInvalidaException("No hubo salto Warp iniciado; no se puede ejecutar");
    }

    @Override
    public void enfriarWarp() throws TransicionRedundanteException {
        throw new TransicionRedundanteException("Motor ya disponible");
    }

    @Override
    public boolean motorDisponible() {
        return true;
    }

    @Override
    public void reiniciarWarp() {
        // Motor ya disponible: no hay nada que reiniciar
    }
}