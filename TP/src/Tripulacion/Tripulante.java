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
}
