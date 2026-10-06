package src.Paquete;

public class Vulcano extends DecoratorTrip{
    private Tripulante tripu;

    public Vulcano(Tripulante tripu)throws AntiguedadNegativa{
        super(tripu.antiguedad, tripu.ident);
        this.tripu = tripu;
    }

    public double getRemu(){
        return tripu.getRemu()+ 30;
    }

    @Override
    public String toString() {
        return tripu.toString() + ", origen= Vulcano, subsidio mensual=" + 30;
    }
}
