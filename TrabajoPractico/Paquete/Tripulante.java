package Paquete;

public abstract class Tripulante {
    protected String ident;
    protected int antiguedad;

    public Tripulante(int antiguedad, String ident) {
        this.antiguedad = antiguedad;
        this.ident = ident;
    }

    public abstract double getRemu();

    public String getIdent() {
        return ident;
    }

    public int getAntiguedad() {
        return antiguedad;
    }
}
