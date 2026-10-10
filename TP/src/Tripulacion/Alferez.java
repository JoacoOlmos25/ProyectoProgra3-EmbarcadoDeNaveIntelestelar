package src.Tripulacion;
/**
 * Representa a un tripulante con el rol de Alferez.
 *
 * Invariante de clase:
 * - precioBase > 0
 * - antiguedad >= 0
 * - indent != "" && ident != null
 */
public class Alferez extends Tripulante {

    private final double base=200;

    public Alferez(int antiguedad, String ident) throws AntiguedadNegativa {
        super();
        setAntiguedad(antiguedad);
        this.ident = ident;
    }
    /**
     * Calcula la remuneracion correspondiente del Alferez.
     *
     * Precondición:
     * - Ninguna adicional (los datos ya están garantizados por el invariante de la clase).
     *
     * Postcondición:
     * - Retorna: precioBase + (precioBase * 0.005 * antiguedad)
     * - El valor retornado es estrictamente > 0.
     *
     * @return double que representa el salario final calculado.
     */
    @Override
    public double getRemu() {
        return base + antiguedad*0.005*base;
    }

    @Override
    public String toString() {
        return "Tripulante{" +
                "ident='" + ident + '\'' +
                ", antiguedad=" + antiguedad +
                ", remuneracion total=" + getRemu() +
                ", desgloce: Cargo= Alferez, remuneracion base=" + base +
                ", adicional por antiguedad=" + base*0.005*antiguedad;
    }
}
