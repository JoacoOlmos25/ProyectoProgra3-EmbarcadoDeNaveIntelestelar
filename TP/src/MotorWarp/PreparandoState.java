package src.MotorWarp;

public class PreparandoState implements MotorState {

    private MotorWarp motorWarp;

    public PreparandoState(MotorWarp motorWarp) {
        this.motorWarp = motorWarp;
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
    }
}
