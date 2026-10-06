package src.Tripulacion;

public abstract class DecoratorTrip extends Tripulante {

    public DecoratorTrip(int antiguedad, String ident) throws AntiguedadNegativa {
        super(antiguedad, ident);
    }

    public abstract double getRemu();

    public abstract String toString();
}
