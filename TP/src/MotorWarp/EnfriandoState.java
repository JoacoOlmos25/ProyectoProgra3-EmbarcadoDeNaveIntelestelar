package src.MotorWarp;

import src.AC.Bitacora;

import java.time.LocalDateTime;

public class EnfriandoState implements MotorState {

    private MotorWarp motorWarp;

    public EnfriandoState(MotorWarp motorWarp) {
        this.motorWarp = motorWarp;
        Bitacora.agregaEvento(LocalDateTime.now(),"Cambio de estado del Motor Warp","El Motor warp se esta enfriando luego de un salto");
    }


    @Override
    public void prepararSalto() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Motor enfriándose: todavía no se puede utilizar");
    }

    @Override
    public void saltar() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Motor enfriándose: todavía no se puede utilizar");
    }

    @Override
    public void finalizarSalto() throws TransicionRedundanteException {
        throw new TransicionRedundanteException("Salto ya finalizado, enfriamiento ya en curso");
    }

    @Override
    public void hacerDisponible() throws TransicionInvalidaException {
        motorWarp.setEstado(new DisponibleState(motorWarp));
    }

    @Override
    public void cancelarSalto() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Motor enfriándose: no se puede cancelar la acción");
    }

    @Override
    public boolean motorDisponible() {
        return false;
    }

}
