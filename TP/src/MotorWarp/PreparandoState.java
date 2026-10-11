package src.MotorWarp;

import src.AC.Bitacora;

import java.time.LocalDateTime;

public class PreparandoState implements MotorState {

    private MotorWarp motorWarp;

    public PreparandoState(MotorWarp motorWarp) {
        this.motorWarp = motorWarp;
        Bitacora.agregaEvento(LocalDateTime.now(),"Cambio de estado del Motor Warp","El Motor warp se esta Preparando para el salto");
    }

    @Override
    public void prepararSalto() throws TransicionRedundanteException {
        throw new TransicionRedundanteException("Preparación de salto ya en curso");
    }

    @Override
    public void saltar() {
        motorWarp.setEstado(new EnWarpState(motorWarp));
    }

    @Override
    public void finalizarSalto() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Preparando salto: no se puede finalizar");
    }

    @Override
    public void hacerDisponible() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Preparando salto: motor no disponible");
    }

    @Override
    public void cancelarSalto() throws TransicionInvalidaException {
        motorWarp.setEstado(new DisponibleState(motorWarp));
        Bitacora.agregaEvento(LocalDateTime.now(),"Cambio de estado del Motor Warp","Se cancelo el salto del Motor warp, al estar en Preparacion vuelve a Disponible");
    }

    @Override
    public boolean motorDisponible() {
        return false;
    }

}
