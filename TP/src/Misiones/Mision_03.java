package src.Misiones;

import src.AC.Asistente;

//Retorno seguro a una zona designada
public class Mision_03 extends Mision{
    public Mision_03(int combustible, int energia, int desgaste, Asistente ac) {
        super(combustible, energia, desgaste, ac);
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
