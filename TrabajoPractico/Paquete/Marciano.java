package Paquete;

public class Marciano extends DecoratorTrip{
    private Tripulante tripu;

    public Marciano(Tripulante tripu){
        super();
        this.tripu = tripu;
    }

    public double getRemu(){
        return getRemu()+18;
    }
}
