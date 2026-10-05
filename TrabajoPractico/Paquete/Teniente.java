package Paquete;

public class Teniente extends Tripulante{

    private final double base=400;

    public Teniente(int antiguedad, String ident) {
        super(antiguedad, ident);
    }

    @Override
    public double getRemu() {
        return base + antiguedad*0.03*base;
    }

    @Override
    public String toString() {
        return "Tripulante{" +
                "ident='" + ident + '\'' +
                ", antiguedad=" + antiguedad +
                ", remuneracion total=" + getRemu() +
                ", desgloce: Cargo= Teniente, remuneracion base=" + base +
                ", adicional por antiguedad=" + base*0.03*antiguedad;
    }
}
