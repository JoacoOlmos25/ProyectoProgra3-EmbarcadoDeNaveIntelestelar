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

    }

    @Override
    protected void cerrar() {

    }
}
