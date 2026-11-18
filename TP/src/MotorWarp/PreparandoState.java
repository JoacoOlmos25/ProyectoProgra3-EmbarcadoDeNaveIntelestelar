package src.MotorWarp;

public class PreparandoState implements MotorState {

    private MotorWarp motorWarp;

    public PreparandoState(MotorWarp motorWarp) {
        this.motorWarp = motorWarp;
        this.motorWarp.registrarEventoMotor("Cambio de estado del Motor Warp","El Motor warp se esta Preparando para el salto");
    }

    @Override
    public void saltarWarp() throws MotorException {
        this.saltar();
    }

    @Override
    public void prepararSalto() throws TransicionRedundanteException {
        throw new TransicionRedundanteException("Preparación de salto ya en curso");
    }

    @Override
    public void saltar() {
        if (motorWarp.energiaSuficiente())
            motorWarp.setEstado(new EjecutandoSaltoWarpState(motorWarp));
    }

    @Override
    public void ejecutarSalto() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Preparando salto: no se puede finalizar");
    }

    @Override
    public void enfriar() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Preparando salto: motor no disponible");
    }

    @Override
    public void cancelarSalto() throws TransicionInvalidaException {
        motorWarp.setEstado(new DisponibleState(motorWarp));
        this.motorWarp.registrarEventoMotor("Cambio de estado del Motor Warp","Se cancelo el salto del Motor warp, al estar en Preparacion vuelve a Disponible");
    }

    @Override
    public boolean motorDisponible() {
        return false;
    }

}
