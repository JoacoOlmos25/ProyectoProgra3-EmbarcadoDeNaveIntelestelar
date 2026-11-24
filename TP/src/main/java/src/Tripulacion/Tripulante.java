package src.Tripulacion;

/**
 * Clase abstracta base del componente Tripulante (Patrón Decorator).
 * Define identidad, antigüedad y contrato común para los cargos concretos y los decoradores de origen.
 *
 * Invariante de clase (para instancias de roles concretos):
 * - ident != null && !ident.isBlank()
 * - antiguedad >= 0
 */
public abstract class Tripulante {
    protected String ident;
    protected int antiguedad;

    public Tripulante() {
        super();
    }

    @Override
    public abstract String toString();

    /**
     * Calcula la liquidación mensual de haberes del tripulante.
     * Precondición: el tripulante cumple su invariante de clase.
     * Postcondición: devuelve un valor estrictamente positivo (> 0).
     * @return remuneración mensual calculada
     */
    public abstract double getRemu();

    public String getIdent() {
        return ident;
    }

    public int getAntiguedad() {
        return antiguedad;
    }

    /**
     * Establece la antigüedad del tripulante en años.
     * Precondición: ninguna (la validación de dominio se realiza mediante excepción propia).
     * Postcondición: getAntiguedad() == antiguedad.
     * @param antiguedad años de servicio
     * @throws AntiguedadNegativa si antiguedad < 0
     */
    public void setAntiguedad(int antiguedad) throws AntiguedadNegativa {
        if (antiguedad >= 0) {
            this.antiguedad = antiguedad;
        } else {
            throw new AntiguedadNegativa();
        }
        assert this.antiguedad == antiguedad : "Postcondición violada al asignar antigüedad";
    }

    /**
     * Indica polimórficamente si el tripulante ocupa el cargo de Capitán (evita instanceof).
     * Postcondición: no modifica el estado.
     * @return true si es Capitán, false en caso contrario
     */
    public boolean esCapitan() {
        return false;
    }

    public abstract String getCargo();

    public String getOrigen() {
        return "Sin especificar";
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Tripulante)) {
            return false;
        }
        Tripulante otro = (Tripulante) obj;
        String miIdent = this.getIdent();
        String otroIdent = otro.getIdent();
        return miIdent != null && miIdent.equals(otroIdent);
    }

    @Override
    public int hashCode() {
        String miIdent = this.getIdent();
        return (miIdent == null) ? 0 : miIdent.hashCode();
    }
}