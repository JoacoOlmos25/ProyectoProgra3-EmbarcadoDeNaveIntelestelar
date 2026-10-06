package src.Tripulacion;

public class Alferez extends Tripulante {

    private final double base=200;

    public Alferez(int antiguedad, String ident) throws AntiguedadNegativa {
        super(antiguedad, ident);
    }

    @Override
    public double getRemu() {
        return base + antiguedad*0.005*base;
    }

    @Override
    public String toString() {
        return "Tripulante{" +
                "ident='" + ident + '\'' +
                ", antiguedad=" + antiguedad +
                ", remuneracion total=" + getRemu() +
                ", desgloce: Cargo= Alferez, remuneracion base=" + base +
                ", adicional por antiguedad=" + base*0.005*antiguedad;
    }
}
