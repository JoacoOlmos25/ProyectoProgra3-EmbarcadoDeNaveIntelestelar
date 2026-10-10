package src.Tripulacion;
/**
 * Representa a un tripulante con el rol de Consejero.
 *
 * Invariante de clase:
 * - precioBase > 0
 * - antiguedad >= 0
 * - indent != "" && ident != null
 */
public class Capitan extends Tripulante {

    private final double base=1000;

    public Capitan(int antiguedad, String ident) throws AntiguedadNegativa {
        super();
        setAntiguedad(antiguedad);
        this.ident = ident;
    }
    /**
     * Calcula la remuneracion correspondiente del Capitan.
     *
     * Precondición:
     * - Ninguna adicional (los datos ya están garantizados por el invariante de la clase).
     *
     * Postcondición:
     * - Retorna: precioBase + (precioBase * 0.2 * antiguedad)
     * - El valor retornado es estrictamente > 0.
     *
     * @return double que representa el salario final calculado.
     */
    @Override
    public double getRemu() {
        return base + antiguedad*0.2*base;
    }

    @Override
    public String toString() {
       return "Tripulante{" +
               "ident='" + ident + '\'' +
               ", antiguedad=" + antiguedad +
               ", desgloce: Cargo= Capitan, remuneracion base=" + base +
               ", adicional por antiguedad=" + base*0.2*antiguedad;
    }
}
