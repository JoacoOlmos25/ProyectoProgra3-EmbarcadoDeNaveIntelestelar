package src.Tripulacion;

/**
 * Representa a un tripulante con el rol de Capitán.
 *
 * Invariante de clase:
 * - base > 0
 * - antiguedad >= 0
 * - ident != null && !ident.isBlank()
 */
public class Capitan extends Tripulante {

    private final double base = 1000;

    /**
     * Crea un Capitán con la antigüedad e identidad indicadas.
     * Precondición: ident != null && !ident.isBlank()
     * Postcondición: el objeto cumple el invariante de clase y esCapitan() == true
     * @param antiguedad años de servicio (>= 0)
     * @param ident identificador único del tripulante, NotNull y no vacío
     * @throws AntiguedadNegativa si antiguedad < 0
     */
    public Capitan(int antiguedad, String ident) throws AntiguedadNegativa {
        super();
        assert ident != null && !ident.isBlank() : "La identidad del capitán no puede ser nula ni vacía";
        setAntiguedad(antiguedad);
        this.ident = ident;
        assert invariante() : "Invariante de Capitan violado tras construcción";
    }

    private boolean invariante() {
        return base > 0 && antiguedad >= 0 && ident != null && !ident.isBlank();
    }

    @Override
    public boolean esCapitan() {
        return true;
    }

    /**
     * Calcula la remuneración correspondiente al Capitán.
     * Precondición: se cumple el invariante de clase.
     * Postcondición: retorna base + (base * 0.2 * antiguedad) y el valor retornado es > 0.
     * @return salario mensual calculado
     */
    @Override
    public double getRemu() {
        double remu = base + antiguedad * 0.2 * base;
        assert remu > 0 : "Postcondición violada: la remuneración debe ser positiva";
        return remu;
    }

    @Override
    public String toString() {
        return "Tripulante{"
                + "ident='" + ident + '\''
                + ", antiguedad=" + antiguedad
                + ", desglose: Cargo= Capitan, remuneracion base=" + base
                + ", adicional por antiguedad=" + base * 0.2 * antiguedad;
    }
}