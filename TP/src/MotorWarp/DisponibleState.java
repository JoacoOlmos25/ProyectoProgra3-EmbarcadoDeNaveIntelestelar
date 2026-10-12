package src.MotorWarp;

import src.AC.Bitacora;

import java.time.LocalDateTime;

public class DisponibleState implements MotorState {

    private MotorWarp motorWarp;

    public DisponibleState(MotorWarp motorWarp) {
        this.motorWarp = motorWarp;
        this.motorWarp.registrarEventoMotor("Cambio de estado del Motor Warp","El Motor warp esta disponible y en espera");
    }

    @Override
    public void prepararSalto() {
        motorWarp.setEstado(new PreparandoState(motorWarp));
    }

    @Override
    public void saltar() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("No se puede saltar sin antes preparar");
    }

    @Override
    public void finalizarSalto() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("No hubo salto; no se debe finalizar");
    }

    @Override
    public void hacerDisponible() throws TransicionRedundanteException {
        throw new TransicionRedundanteException("Motor ya disponible");
    }

    @Override
    public void cancelarSalto() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Cancelación de salto inválida: no se está ejecutando un salto");
    }

    @Override
    public boolean motorDisponible() {
        return true;
    }
}
