package src.Tripulacion;

public class Terricola extends DecoratorTrip {

    private static final int SUBSIDIO = 20;

    /**
     * Contrato:
     * Asigna el origen Terrícola a un tripulante existente.
     * Precondición: tripu != null
     * Postcondición: el tripulante queda decorado con el origen Terrícola y su subsidio mensual.
     */
    public Terricola(Tripulante tripu) {
        super(tripu);
    }

    /**
     * Contrato:
     * Suma el subsidio mensual por origen Terrícola (20) a la remuneración base.
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
    public String toString() {
        return tripu.toString() + ", origen= Terricola, subsidio mensual=" + SUBSIDIO;
    }
}