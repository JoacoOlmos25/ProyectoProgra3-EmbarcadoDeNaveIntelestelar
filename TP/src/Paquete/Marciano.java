package src.Paquete;

public class Marciano extends DecoratorTrip{
    private Tripulante tripu;

    public Marciano(Tripulante tripu) throws AntiguedadNegativa{
        super(tripu.antiguedad, tripu.ident);
        this.tripu = tripu;
    }

    public double getRemu(){
        return tripu.getRemu()+18;
    }

    @Override
    public String toString() {
        return tripu.toString() + ", origen= Marciano, subsidio mensual=" + 18 ;
    }
}
