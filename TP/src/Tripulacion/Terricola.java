package src.Tripulacion;

public class Terricola extends DecoratorTrip {
    private Tripulante tripu;

    /**Contrato
        *Este es para un tripulante con cargo le asigna como origen Terricola
        *Precondiciones
            *Debe exister un cargo para el tripulante (no puede ser null)
        *PostCondiciones
            *Debe devolver el tripulante junto con su origen Terricola
    */

    public Terricola(Tripulante tripu){
        super();
        this.tripu = tripu;
    }

    /**Contrato
        *Le agrega 20 por ser de origen Terricolas a la liquidacion de haberes
        *Precondicion
            *Trae segun su carga la liquidacion de haberes(positiva)
        *Postcondicion
            *Devuelve la liquidacion de haberes total
     */

    public double getRemu(){
        return tripu.getRemu()+20;
    }

    @Override
    public String toString() {
        return tripu.toString() + ", origen= Terricola, subsidio mensual=" + 20 ;
    }
}
