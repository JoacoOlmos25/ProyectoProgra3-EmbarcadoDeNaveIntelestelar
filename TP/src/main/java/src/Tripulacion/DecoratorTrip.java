package src.Tripulacion;

/**
 * Clase base del patrón Decorator para los orígenes del tripulante.
 * Mantiene la referencia al tripulante envuelto y delega identidad, antigüedad y cargo.
 *
 * Invariante de clase:
 * - tripu != null
 */
public abstract class DecoratorTrip extends Tripulante {

    protected final Tripulante tripu;

    /**
     * Crea un decorador envolviendo un tripulante existente.
     * Precondición: tripu != null
     * Postcondición: el decorador delega identidad, antigüedad y cargo en tripu
     * @param tripu tripulante base o previamente decorado, NotNull
     */
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
    public void setAntiguedad(int antiguedad) throws AntiguedadNegativa {
        this.tripu.setAntiguedad(antiguedad);
    }

    @Override
    public boolean esCapitan() {
        return this.tripu.esCapitan();
    }

    @Override
    public abstract double getRemu();

    @Override
    public String getCargo() {
        return this.tripu.getCargo();
    }

    @Override
    public abstract String toString();
}