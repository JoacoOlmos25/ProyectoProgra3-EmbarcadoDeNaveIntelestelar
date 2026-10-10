package Paquete.MotorWarp;

import Paquete.Excepciones.EstadoMotorInvalidoException;

public class EnfriandoState implements MotorState {

    private MotorWarp motorWarp;

    public EnfriandoState(MotorWarp motorWarp) {
        this.motorWarp = motorWarp;
    }


    @Override
    public void prepararSalto() throws EstadoMotorInvalidoException {
        throw new EstadoMotorInvalidoException("Motor enfriándose: todavía no se puede utilizar");
    }

    @Override
    public void saltar() throws EstadoMotorInvalidoException {
        throw new EstadoMotorInvalidoException("Motor enfriándose: todavía no se puede utilizar");
    }

    @Override
    public void enfriar() {
        //idempotente
    }

    @Override
    public void hacerDisponible() throws EstadoMotorInvalidoException {
        motorWarp.setEstado(new DisponibleState(motorWarp));
    }

    @Override
    public void cancelarSalto() throws EstadoMotorInvalidoException {
        throw new EstadoMotorInvalidoException("Motor enfriándose: no se puede cancelar la acción");
    }
}
