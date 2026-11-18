package src.Misiones;

import src.AC.Asistente;
import src.destinoytablero.Destino;
import src.destinoytablero.Tablero;

//Retorno seguro a una zona designada
public class Mision_03 extends Mision{

    private static final int REQ_COMBUSTIBLE = 4;
    private static final int REQ_ENERGIA = 0;
    private static final int REQ_DESGASTE = 4;

    public Mision_03(Asistente ac, Destino destino, Tablero tablero) {
        super(REQ_COMBUSTIBLE, REQ_ENERGIA, REQ_DESGASTE, ac, destino, tablero);
    }

    @Override
    protected void ejecutar() {
        System.out.println("Ejecutando Mision_03");
    }

    @Override
    protected void evaluarResultado() {
        System.out.println("Mision_01 ejectuada exitosamente");
    }

    @Override
    protected void cerrar() {
        System.out.println(" Mision_03 cerrada");
    }
}
