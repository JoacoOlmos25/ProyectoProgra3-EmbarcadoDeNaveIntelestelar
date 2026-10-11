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
    }

    @Override
    protected void evaluarResultado() {

    }

    @Override
    protected void cerrar() {

    }

}
