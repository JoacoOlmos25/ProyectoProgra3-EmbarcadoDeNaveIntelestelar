package src.Tripulacion;

public class Terricola extends DecoratorTrip {
    private Tripulante tripu;

    public Terricola(Tripulante tripu){
        super();
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
