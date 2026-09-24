package Paquete;

import java.util.ArrayList;

public abstract class Nave {
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

}
