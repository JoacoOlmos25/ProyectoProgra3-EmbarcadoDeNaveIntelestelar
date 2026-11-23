package src.MotorWarp;

import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;

import java.util.ArrayList;

/**
 * Estado "En Warp" del Motor Warp (listo para ejecutar el salto hiperespacial).
 * Permite ejecutar el salto de 2 casilleros al invocar saltarWarp(), o desplazarse normalmente con saltar().
 *
 * Invariante de clase:
 * - motorWarp != null
 */
public class EjecutandoSaltoWarpState implements MotorState {

    private final MotorWarp motorWarp;

    /**
     * Pre: motorWarp != null.
     * Post: el estado queda asociado al motor y registra que está listo/en curso para Warp.
     */
    public EjecutandoSaltoWarpState(MotorWarp motorWarp) {
        assert motorWarp != null : "El MotorWarp no puede ser nulo";
        this.motorWarp = motorWarp;
        this.motorWarp.registrarEventoMotor("Cambio de estado del Motor Warp", "El Motor warp está activo y listo para el salto");
    }

    @Override
    public void saltar(Tablero tablero, ArrayList<Posicion> ruta) {
        assert tablero != null && ruta != null && !ruta.isEmpty() : "Tablero y ruta válida requeridos para saltar";
        this.motorWarp.saltoNormal(tablero, ruta);
    }

    @Override
    public void saltarWarp(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException {
        assert tablero != null && ruta != null && !ruta.isEmpty() : "Tablero y ruta válida requeridos para saltarWarp";
        this.ejecutandoWarp(tablero, ruta);
    }

    @Override
    public void prepararWarp() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Warp en curso: no se puede preparar un nuevo salto");
    }

    @Override
    public void iniciarWarp() throws TransicionRedundanteException {
        throw new TransicionRedundanteException("Warp en curso: la ignición ya fue completada");
    }

    @Override
    public void ejecutandoWarp(Tablero tablero, ArrayList<Posicion> ruta) {
        assert tablero != null && ruta != null && !ruta.isEmpty() : "Tablero y ruta válida requeridos para ejecutandoWarp";

        if (motorWarp.energiaSuficiente()) {
            motorWarp.consumeEnergiaPorSalto();
            motorWarp.mover(tablero, ruta, 2);
            motorWarp.registrarEventoMotor(
                    "Salto Warp ejecutado",
                    "Salto hiperespacial de hasta 2 casilleros completado. Energía consumida: " + MotorWarp.COSTO_WARP_ENERGIA
            );
            motorWarp.setEstado(new EnfriandoState(motorWarp));
        } else {
            motorWarp.registrarEventoMotor(
                    "Salto Warp abortado",
                    "Energía insuficiente para completar el salto (requiere > " + MotorWarp.COSTO_WARP_ENERGIA + "). El motor vuelve a preparación."
            );
            motorWarp.setEstado(new PreparandoState(motorWarp));
        }
    }

    @Override
    public void enfriarWarp() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Warp en curso: no se puede enfriar antes de completar el salto");
    }

    @Override
    public boolean motorDisponible() {
        return false;
    }

    @Override
    public void reiniciarWarp() {
        // Se descarta el salto pendiente
        motorWarp.setEstado(new DisponibleState(motorWarp));
    }
}