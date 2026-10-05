package Paquete;

public class Alferez extends Tripulante{

    private final double base=200;

    public Alferez(int antiguedad, String ident) {
        super(antiguedad, ident);
    }

    @Override
    public double getRemu() {
        return base + antiguedad*0.005*base;
    }
}
