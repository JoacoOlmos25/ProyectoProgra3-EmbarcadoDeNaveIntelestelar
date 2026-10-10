package Paquete.MotorWarp;

import Paquete.Excepciones.EstadoMotorInvalidoException;

public class DisponibleState implements MotorState {

    private MotorWarp motorWarp;

    public DisponibleState(MotorWarp motorWarp) {
        this.motorWarp = motorWarp;
    }

    @Override
    public void prepararSalto() {
        motorWarp.setEstado(new PreparandoState(motorWarp));
    }

    @Override
    public void saltar() throws EstadoMotorInvalidoException {
        throw new EstadoMotorInvalidoException("No se puede saltar sin antes preparar");
    }

    @Override
    public void enfriar() throws EstadoMotorInvalidoException {
        throw new EstadoMotorInvalidoException("No hubo salto; no se debe enfriar");
    }

    @Override
    public void hacerDisponible() {
        //método idempotente; si ya está disponible, hacerlo disponible no cambia nada
    }

    @Override
    public void cancelarSalto() throws EstadoMotorInvalidoException {
        throw new EstadoMotorInvalidoException("Cancelación de salto inválida: no se está ejecutando un salto");
    }
}
