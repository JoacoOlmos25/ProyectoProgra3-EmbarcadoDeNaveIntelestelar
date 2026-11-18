package src.Misiones;
//Recolección en un punto de interés

import src.AC.Asistente;
import src.destinoytablero.Destino;
import src.destinoytablero.Tablero;

public class Mision_02 extends Mision{

    private static final int REQ_COMBUSTIBLE = 4;
    private static final int REQ_ENERGIA = 10;
    private static final int REQ_DESGASTE = 4;

    public Mision_02(Asistente ac, Destino destino, Tablero tablero) {
        super(REQ_COMBUSTIBLE, REQ_ENERGIA, REQ_DESGASTE, ac, destino, tablero);
    }


    @Override
    protected void ejecutar() {
        System.out.println("Ejecutando Mision_02");
    }

    @Override
    protected void evaluarResultado() {
        System.out.println("Mision_02 ejectuada exitosamente");
    }

    @Override
    protected void cerrar() {
        System.out.println(" Mision_02 cerrada");
    }
}
