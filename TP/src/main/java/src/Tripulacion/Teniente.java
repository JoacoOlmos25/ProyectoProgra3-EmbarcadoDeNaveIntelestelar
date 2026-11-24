package src.Tripulacion;

/**
 * Representa a un tripulante con el rol de Teniente.
 *
 * Invariante de clase:
 * - base > 0
 * - antiguedad >= 0
 * - ident != null && !ident.isBlank()
 */
public class Teniente extends Tripulante {

    private final double base = 400;

    /**
     * Crea un Teniente con la antigüedad e identidad indicadas.
     * Precondición: ident != null && !ident.isBlank()
     * Postcondición: el objeto cumple el invariante de clase
     * @param antiguedad años de servicio (>= 0)
     * @param ident identificador único del tripulante, NotNull y no vacío
     * @throws AntiguedadNegativa si antiguedad < 0
     */
    public Teniente(int antiguedad, String ident) throws AntiguedadNegativa {
        super();
        assert ident != null && !ident.isBlank() : "La identidad del teniente no puede ser nula ni vacía";
        setAntiguedad(antiguedad);
        this.ident = ident;
        assert invariante() : "Invariante de Teniente violado tras construcción";
    }

    private boolean invariante() {
        return base > 0 && antiguedad >= 0 && ident != null && !ident.isBlank();
    }

    /**
     * Calcula la remuneración correspondiente al Teniente.
     * Precondición: se cumple el invariante de clase.
     * Postcondición: retorna base + (base * 0.03 * antiguedad) y el valor retornado es > 0.
     * @return salario mensual calculado
     */
    @Override
    public double getRemu() {
        double remu = base + antiguedad * 0.03 * base;
        assert remu > 0 : "Postcondición violada: la remuneración debe ser positiva";
        return remu;
    }

    @Override
    public String getCargo() { return "Teniente"; }

    @Override
    public String toString() {
        return "Tripulante{"
                + "ident='" + ident + '\''
                + ", antiguedad=" + antiguedad
                + ", desglose: Cargo= Teniente, remuneracion base=" + base
                + ", adicional por antiguedad=" + base * 0.03 * antiguedad;
    }
}