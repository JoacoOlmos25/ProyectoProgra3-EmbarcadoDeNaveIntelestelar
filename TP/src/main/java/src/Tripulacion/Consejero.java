package src.Tripulacion;

/**
 * Representa a un tripulante con el rol de Consejero.
 *
 * Invariante de clase:
 * - base > 0
 * - antiguedad >= 0
 * - cantConsejos >= 0
 * - ident != null && !ident.isBlank()
 */
public class Consejero extends Tripulante {

    private final double base = 600;
    private int cantConsejos;

    /**
     * Crea un Consejero con la antigüedad e identidad indicadas y 0 consejos iniciales.
     * Precondición: ident != null && !ident.isBlank()
     * Postcondición: cantConsejos == 0 y se cumple el invariante de clase
     * @param antiguedad años de servicio (>= 0)
     * @param ident identificador único del tripulante, NotNull y no vacío
     * @throws AntiguedadNegativa si antiguedad < 0
     */
    public Consejero(int antiguedad, String ident) throws AntiguedadNegativa {
        super();
        assert ident != null && !ident.isBlank() : "La identidad del consejero no puede ser nula ni vacía";
        setAntiguedad(antiguedad);
        this.ident = ident;
        this.cantConsejos = 0;
        assert invariante() : "Invariante de Consejero violado tras construcción";
    }

    private boolean invariante() {
        return base > 0 && antiguedad >= 0 && cantConsejos >= 0 && ident != null && !ident.isBlank();
    }

    /**
     * Calcula la remuneración correspondiente al Consejero.
     * Precondición: se cumple el invariante de clase.
     * Postcondición: retorna base + (base * 0.05 * antiguedad) + (2 * cantConsejos) y es > 0.
     * @return salario mensual calculado
     */
    @Override
    public double getRemu() {
        double remu = base + antiguedad * 0.05 * base + cantConsejos * 2;
        assert remu > 0 : "Postcondición violada: la remuneración debe ser positiva";
        return remu;
    }

    /**
     * Suma un nuevo consejo al contador individual de la instancia.
     * Precondición: ninguna (el estado válido está garantizado por el invariante).
     * Postcondición: cantConsejos == cantConsejos_anterior + 1.
     */
    public void nuevoConsejos() {
        int anterior = this.cantConsejos;
        this.cantConsejos += 1;
        assert this.cantConsejos == anterior + 1 : "Postcondición violada al incrementar consejos";
        assert invariante() : "Invariante de Consejero violado";
    }

    @Override
    public String getCargo() { return "Consejero"; }

    @Override
    public String toString() {
        return "Tripulante{"
                + "ident='" + ident + '\''
                + ", antiguedad=" + antiguedad
                + ", desglose: Cargo= Consejero, remuneracion base=" + base
                + ", adicional por antiguedad=" + base * 0.05 * antiguedad
                + ", adicional por consejos=" + cantConsejos * 2;
    }
}