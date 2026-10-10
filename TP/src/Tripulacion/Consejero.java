package src.Tripulacion;
/**
 * Representa a un tripulante con el rol de Consejero.
 *
 * Invariante de clase:
 * - precioBase > 0
 * - antiguedad >= 0
 * - cantidadConsejos >= 0
 * - indent != "" && ident != null
 */
public class Consejero extends Tripulante {
    //Deberia agregar la variable base como final?
    private final double base=600;
    private int cantConsejos;

    public Consejero(int antiguedad, String ident) throws AntiguedadNegativa {
        super();
        setAntiguedad(antiguedad);
        this.ident = ident;
        this.cantConsejos = 0;
    }
    /**
     * Calcula la remuneracion correspondiente del consejero.
     *
     * Precondición:
     * - Ninguna adicional (los datos ya están garantizados por el invariante de la clase).
     *
     * Postcondición:
     * - Retorna: precioBase + (precioBase * 0.05 * antiguedad) + (2 * cantidadConsejos)
     * - El valor retornado es estrictamente > 0.
     *
     * @return double que representa el salario final calculado.
     */
    @Override
    public double getRemu() {
        return base + antiguedad*0.05*base + cantConsejos*2;
    }
    /**
     * Suma un nuevo consejo al contador individual de la instancia.
     *
     * Precondición:
     * - Ninguna (el estado válido inicial está garantizado por el Invariante de clase).
     *
     * Postcondición:
     * - cantConsejos == cantConsejos_anterior + 1
     */
    public void nuevoConsejos(){
        this.cantConsejos+=1;
    }

    @Override
    public String toString() {
        return "Tripulante{" +
                "ident='" + ident + '\'' +
                ", antiguedad=" + antiguedad +
                ", remuneracion total=" + getRemu() +
                ", desgloce: Cargo= Consejero, remuneracion base=" + base +
                ", adicional por antiguedad=" + base*0.05*antiguedad;
    }
}
