package Paquete.MotorWarp;

import Paquete.Excepciones.EstadoMotorInvalidoException;

public class EnWarpState implements MotorState {

    private MotorWarp motorWarp;

    public EnWarpState(MotorWarp motorWarp) {
        this.motorWarp = motorWarp;
    }

    @Override
    public void prepararSalto() throws EstadoMotorInvalidoException {
        throw new EstadoMotorInvalidoException("Warp en curso: no se puede preparar un nuevo salto");
    }

    @Override
    public void saltar() throws EstadoMotorInvalidoException {
        throw new EstadoMotorInvalidoException("Warp en curso: no se puede realizar un nuevo salto");
    }

    @Override
    public void enfriar() {
        motorWarp.setEstado(new EnfriandoState(motorWarp));
    }

    @Override
    public void hacerDisponible() throws EstadoMotorInvalidoException {
        throw new EstadoMotorInvalidoException("Warp en curso: motor no disponible");
    }

    @Override
    public void cancelarSalto() throws EstadoMotorInvalidoException {
        motorWarp.setEstado(new DisponibleState(motorWarp));
    }
}
