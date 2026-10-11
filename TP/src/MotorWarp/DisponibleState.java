package src.MotorWarp;

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
}
