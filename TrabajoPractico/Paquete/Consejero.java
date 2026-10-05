package Paquete;

public class Consejero extends Tripulante{
    //Deberia agregar la variable base como final?
    private final double base=600;

    public Consejero(int antiguedad, String ident) {
        super(antiguedad, ident);
    }

    @Override
    public double getRemu() {
        return base + antiguedad*0.05*base;
    }

    //Agregar 2 PG por cada consejo registrado(NI puta idea pero hay q hacerlo)
}
