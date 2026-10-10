package src.Tripulacion;

public class Marciano extends DecoratorTrip {
    private Tripulante tripu;

    /**Contrato
        *Este es para un tripulante con cargo le asigna como origen Marciano
        *Precondiciones
            *Debe exister un cargo para el tripulante (no puede ser null)
        *PostCondiciones
            *Debe devolver el tripulante junto con su origen Marciano
     */

    public Marciano(Tripulante tripu){
        super();
        this.tripu = tripu;
    }

    /**Contrato
        *Le agrega 18 por ser de origen Marciano a la liquidacion de haberes
        *Precondicion
            *Trae segun su carga la liquidacion de haberes(positiva)
        *Postcondicion
            *Devuelve la liquidacion de haberes total
    */

    public double getRemu(){
        return tripu.getRemu()+18;
    }

    @Override
    public String toString() {
        return tripu.toString() + ", origen= Marciano, subsidio mensual=" + 18 ;
    }
}
