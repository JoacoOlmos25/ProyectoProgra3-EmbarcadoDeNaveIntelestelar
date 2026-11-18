package src.Misiones;
//Intercepción y asistencia

import src.AC.Asistente;
import src.destinoytablero.Destino;
import src.destinoytablero.Tablero;

public class Mision_01 extends Mision {

    private static final int REQ_COMBUSTIBLE = 4;
    private static final int REQ_ENERGIA = 5;
    private static final int REQ_DESGASTE = 4;

    public Mision_01(Asistente ac, Destino destino, Tablero tablero) {
        super(REQ_COMBUSTIBLE, REQ_ENERGIA, REQ_DESGASTE, ac, destino, tablero);
    }

    @Override
    protected void ejecutar() {
        System.out.println("Ejecutando Mision_01");
        // se deberia aca de propagar algunas de las excepciones y resolver y/o registrar en al bitacora
    }

    @Override
    protected void evaluarResultado() {
        System.out.println("Mision_01 ejectuada exitosamente");
    }

    @Override
    protected void cerrar() {
        System.out.println(" Mision_01 cerrada");
    }

}
