package src.Paquete;

public class Terricola extends DecoratorTrip{
    private Tripulante tripu;

    public Terricola(Tripulante tripu)throws AntiguedadNegativa{
        super(tripu.antiguedad, tripu.ident);
        this.tripu = tripu;
    }

    public double getRemu(){
        return tripu.getRemu()+20;
    }

    @Override
    public String toString() {
        return tripu.toString() + ", origen= Terricola, subsidio mensual=" + 20 ;
    }
}
