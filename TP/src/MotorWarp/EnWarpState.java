package src.MotorWarp;

public class EnWarpState implements MotorState {

    private MotorWarp motorWarp;

    public EnWarpState(MotorWarp motorWarp) {
        this.motorWarp = motorWarp;
    }

    @Override
    public void prepararSalto() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Warp en curso: no se puede preparar un nuevo salto");
    }

    @Override
    public void saltar() throws TransicionRedundanteException {
        throw new TransicionRedundanteException("Warp en curso: no se puede realizar un nuevo salto");
    }

    @Override
    public void finalizarSalto() {
        motorWarp.setEstado(new EnfriandoState(motorWarp));
    }

    @Override
    public void hacerDisponible() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Warp en curso: motor no disponible");
    }

    @Override
    public void cancelarSalto() {
        motorWarp.setEstado(new DisponibleState(motorWarp));
    }
}
