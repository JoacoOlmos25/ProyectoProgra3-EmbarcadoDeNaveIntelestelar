package src.Misiones;
//Recolección en un punto de interés

import src.AC.Asistente;

public class Mision_02 extends Mision{
    public Mision_02(int combustible, int energia, int desgaste, Asistente ac) {
        super(combustible, energia, desgaste, ac);
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
