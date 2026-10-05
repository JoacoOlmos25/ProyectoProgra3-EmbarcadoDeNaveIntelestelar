package Paquete;

public abstract class DecoratorTrip extends Tripulante {

    public DecoratorTrip(int antiguedad, String ident) {
        super(antiguedad, ident);
    }

    public abstract double getRemu();

    public abstract String toString();
}
