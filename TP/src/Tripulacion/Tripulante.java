package src.Tripulacion;


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
