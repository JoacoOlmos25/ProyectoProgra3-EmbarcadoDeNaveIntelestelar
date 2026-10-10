package Paquete;

import Paquete.MotorWarp.MotorWarp;

import java.util.ArrayList;

public abstract class Nave implements IRecursos {
    //constantes de nave
    private final int capMaxCombustible = 100;
    private final int capMaxEnergia = 100;
    protected boolean requiereMantenimiento ;
    //atributos comunes de nave
    private int combustible;
    private int energia;
    private int desgaste;
    protected ArrayList <Tripulante> tripulacion = new ArrayList<>();

    //motor warp
    private MotorWarp motorWarp;

    public Nave(int combInicial,int eInicial,int desInicial) {
        this.combustible = combInicial;
        this.energia = eInicial;
        this.desgaste = desInicial;
        this.requiereMantenimiento = false;
    }

    public void mantenimiento(){

        if (this.requiereMantenimiento){
            this.desgaste = 0;
            this.requiereMantenimiento = false;
        }else
            throw new mantenimientoException();//la desarrollo en un paquete aparte
    }

    public void incrementaDesgaste(int cant){
        int aux;
        if ((this.desgaste + cant) > 100){
            throw new excesoDesgaste();
        }else{
            this.desgaste += cant;
            if (this.desgaste >= 80)
                this.requiereMantenimiento = true;
        }
    }

    //Implementación de interface IRecursos

    public int getCombustible(){
        return this.combustible;
    }

    public void cargarCombustible(int combustible) {

        if (this.combustible + combustible > this.capMaxCombustible) {
            throw new recursosException("Exceso de combustible");
        }

        this.combustible += combustible;
    }

    public void consumirCombustible(int combustible) {
        if (this.combustible - combustible < this.capMaxCombustible) {
            throw new recursosException("Combustible insuficiente");
        }

        this.combustible -= combustible;
    }

    public int getEnergia(){
        return this.energia;
    }

    public void cargarEnergia(int energia) {
        if (this.energia + energia > this.capMaxEnergia) {
            throw new recursosException("Exceso de energía");
        }

        this.energia += energia;
    }

    public void consumirEnergia(int energia) {
        if (this.energia - energia < this.capMaxEnergia) {
            throw new recursosException("Energía insuficiente");
        }

        this.energia -= energia;
    }

    public void ejecutarSaltoWarp() throws recursosException {

    }
}
