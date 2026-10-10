package src.Tripulacion;

public class Vulcano extends DecoratorTrip {
    private Tripulante tripu;

    /**Contrato
        *Este es para un tripulante con cargo le asigna como origen Vulcano
        *Precondiciones
            *Debe exister un cargo para el tripulante (no puede ser null)
        *PostCondiciones
            *Debe devolver el tripulante junto con su origen Vulcano
     */

    public Vulcano(Tripulante tripu){
        super();
        this.tripu = tripu;
    }

    /**Contrato
        *Le agrega 30 por ser de origen Vulcano a la liquidacion de haberes
        *Precondicion
            *Trae segun su carga la liquidacion de haberes(positiva)
        *Postcondicion
            *Devuelve la liquidacion de haberes total
     */

    public double getRemu(){
        return tripu.getRemu()+ 30;
    }

    @Override
    public String toString() {
        return tripu.toString() + ", origen= Vulcano, subsidio mensual=" + 30;
    }
}
