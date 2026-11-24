package src.Tripulacion;

public class Marciano extends DecoratorTrip {

    private static final int SUBSIDIO = 18;

    /**
     * Contrato:
     * Asigna el origen Marciano a un tripulante existente.
     * Precondición: tripu != null
     * Postcondición: el tripulante queda decorado con el origen Marciano y su subsidio mensual.
     */
    public Marciano(Tripulante tripu) {
        super(tripu);
    }

    /**
     * Contrato:
     * Suma el subsidio mensual por origen Marciano (18) a la remuneración base.
     * Precondición: tripu.getRemu() > 0
     * Postcondición: retorna tripu.getRemu() + SUBSIDIO (> 0)
     */
    @Override
    public double getRemu() {
        double total = tripu.getRemu() + SUBSIDIO;
        assert total > 0 : "Postcondición violada: remuneración decorada debe ser positiva";
        return total;
    }

    @Override
    public String getOrigen() { return "Marciano"; }

    @Override
    public String toString() {
        return tripu.toString() + ", origen= Marciano, subsidio mensual=" + SUBSIDIO;
    }
}