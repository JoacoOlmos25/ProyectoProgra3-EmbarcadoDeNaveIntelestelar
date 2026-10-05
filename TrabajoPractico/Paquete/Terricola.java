package Paquete;

public class Terricola extends DecoratorTrip{
    private Tripulante tripu;

    public Terricola(Tripulante tripu){
        super();
        this.tripu = tripu;
    }

    public double getRemu(){
        return getRemu()+20;
    }
}
