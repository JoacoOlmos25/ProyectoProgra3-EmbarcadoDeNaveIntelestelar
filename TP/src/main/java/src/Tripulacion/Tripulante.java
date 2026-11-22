package src.Tripulacion;
/**
 * Representa la clase abstracta base Tripulante.
 * la cual heredan las clases concretas con los diferentes roles
 * y las clases decoradoras como los origenes del tripulante
 */

public abstract class Tripulante {
    protected String ident;
    protected int antiguedad;

    public Tripulante(){
        super();
    }

    @Override
    public abstract String toString();

    public abstract double getRemu();

    public String getIdent(){ return ident; }

    public int getAntiguedad() {
        return antiguedad;
    }

    public void setAntiguedad(int antiguedad) throws AntiguedadNegativa{
        if (antiguedad >= 0)
            this.antiguedad = antiguedad;
        else{
            //excepcion
            throw new AntiguedadNegativa();
        }
    }
    public boolean esCapitan() {
        return false;
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
