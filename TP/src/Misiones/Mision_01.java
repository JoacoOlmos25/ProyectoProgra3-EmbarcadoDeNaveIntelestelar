package src.Misiones;
//Intercepción y asistencia

import src.AC.Asistente;

public class Mision_01 extends Mision {
    public Mision_01(int combustible, int energia, int desgaste, Asistente ac) {
        super(combustible, energia, desgaste, ac);
    }

    public Mision_01() {}

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
