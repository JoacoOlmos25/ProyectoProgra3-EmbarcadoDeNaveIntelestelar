package src.MotorWarp;

public class EjecutandoSaltoWarpState implements MotorState {

    private MotorWarp motorWarp;

    public EjecutandoSaltoWarpState(MotorWarp motorWarp) {
        this.motorWarp = motorWarp;
        this.motorWarp.registrarEventoMotor("Cambio de estado del Motor Warp","El Motor warp esta activo y en curso");
    }

    @Override
    public void saltarWarp() throws MotorException {
        this.ejecutarSalto();
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
    public void ejecutarSalto() {
        if (motorWarp.energiaSuficiente()) {
            motorWarp.consumeEnergiaPorSalto();
            //TODO: mueve la nave
            motorWarp.setEstado(new EnfriandoState(motorWarp));
        }
        else {                                                              //debería lanzarse excepción y catchearse acá?
            motorWarp.setEstado(new PreparandoState(motorWarp));
        }
    }

    @Override
    public void enfriar() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Warp en curso: motor no disponible");
    }

    @Override
    public void cancelarSalto() {
        motorWarp.setEstado(new DisponibleState(motorWarp));
        this.motorWarp.registrarEventoMotor("Cambio de estado del Motor Warp","Se cancelo el salto del Motor warp");
    }

    @Override
    public boolean motorDisponible() {
        return false;
    }

}
