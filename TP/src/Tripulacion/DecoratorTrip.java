package src.Tripulacion;

/**
 * Clase base del patrón Decorator para los orígenes del tripulante.
 * Mantiene la referencia al tripulante envuelto y delega identidad, antigüedad y cargo.
 */
public abstract class DecoratorTrip extends Tripulante {

    protected Tripulante tripu;

    public DecoratorTrip(Tripulante tripu) {
        super();
        assert tripu != null : "El tripulante a decorar no puede ser nulo";
        this.tripu = tripu;
    }

    @Override
    public String getIdent() {
        return this.tripu.getIdent();
    }

    @Override
    public int getAntiguedad() {
        return this.tripu.getAntiguedad();
    }

    @Override
    public boolean esCapitan() {
        return this.tripu.esCapitan();
    }

    @Override
    public abstract double getRemu();

    @Override
    public abstract String toString();
}