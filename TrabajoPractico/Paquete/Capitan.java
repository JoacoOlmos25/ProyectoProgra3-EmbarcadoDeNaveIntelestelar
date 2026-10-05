package Paquete;

public class Capitan extends Tripulante {

    private final double base=1000;

    public Capitan(int antiguedad, String ident) {
        super(antiguedad, ident);
    }

    @Override
    public double getRemu() {
        return base + antiguedad*0.2*base;
    }
}
