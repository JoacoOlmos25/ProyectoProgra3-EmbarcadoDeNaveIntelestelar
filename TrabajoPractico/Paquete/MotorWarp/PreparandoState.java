package Paquete.MotorWarp;

import Paquete.Excepciones.EstadoMotorInvalidoException;

public class PreparandoState implements MotorState {

    private MotorWarp motorWarp;

    public PreparandoState(MotorWarp motorWarp) {
        this.motorWarp = motorWarp;
    }

    @Override
    public void prepararSalto() {
        //idempotente, si ya está en preparación no pasa nada
    }

    @Override
    public void saltar() {
        motorWarp.setEstado(new EnWarpState(motorWarp));
    }

    @Override
    public void enfriar() throws EstadoMotorInvalidoException {
        throw new EstadoMotorInvalidoException("Preparando salto: no se puede enfriar");
    }

    @Override
    public void hacerDisponible() throws EstadoMotorInvalidoException {
        throw new EstadoMotorInvalidoException("Preparando salto: motor no disponible");
    }

    @Override
    public void cancelarSalto() throws EstadoMotorInvalidoException {
        motorWarp.setEstado(new DisponibleState(motorWarp));
    }
}
