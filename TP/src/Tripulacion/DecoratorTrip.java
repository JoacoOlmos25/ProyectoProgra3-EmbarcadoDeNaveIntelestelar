package src.Tripulacion;

public abstract class DecoratorTrip extends Tripulante {

    /**Contrato
        *Este es el patron de diseño para las clases de tipo origen
        *Precondicon
            *Asegurase de que herede de tripulante
        *Postcondicion
            *Obtener el tripulante con su origen
    */

    public DecoratorTrip(){
        super();
    }

    public abstract double getRemu();

    public abstract String toString();
}
