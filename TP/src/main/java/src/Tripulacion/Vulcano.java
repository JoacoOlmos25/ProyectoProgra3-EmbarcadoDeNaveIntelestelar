package src.Tripulacion;

public class Vulcano extends DecoratorTrip {

    private static final int SUBSIDIO = 30;

    /**
     * Contrato:
     * Asigna el origen Vulcano a un tripulante existente.
     * Precondición: tripu != null
     * Postcondición: el tripulante queda decorado con el origen Vulcano y su subsidio mensual.
     */
    public Vulcano(Tripulante tripu) {
        super(tripu);
    }

    /**
     * Contrato:
     * Suma el subsidio mensual por origen Vulcano (30) a la remuneración base.
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
        return tripu.toString() + ", origen= Vulcano, subsidio mensual=" + SUBSIDIO;
    }
}