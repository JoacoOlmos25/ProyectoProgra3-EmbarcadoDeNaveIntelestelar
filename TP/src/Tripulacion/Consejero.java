package src.Tripulacion;

public class Consejero extends Tripulante {
    //Deberia agregar la variable base como final?
    private final double base=600;
    private int cantConsejos;

    public Consejero(int antiguedad, String ident, int cantConsejos) throws AntiguedadNegativa {
        super();
        setAntiguedad(antiguedad);
        this.ident = ident;
        this.cantConsejos = cantConsejos;
    }

    @Override
    public double getRemu() {
        return base + antiguedad*0.05*base + cantConsejos*2;
    }


    //Agregar 2 PG por cada consejo registrado(NI puta idea pero hay q hacerlo)
    @Override
    public String toString() {
        return "Tripulante{" +
                "ident='" + ident + '\'' +
                ", antiguedad=" + antiguedad +
                ", remuneracion total=" + getRemu() +
                ", desgloce: Cargo= Consejero, remuneracion base=" + base +
                ", adicional por antiguedad=" + base*0.05*antiguedad;
    }
}
