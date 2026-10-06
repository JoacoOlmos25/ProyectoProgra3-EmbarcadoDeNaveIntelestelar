package src.nave;
import src.Paquete.Tripulante;

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
    //protected EstadoNave estadoActual; cuando hagamos lo del motor warp

    public Nave(int combInicial,int eInicial) {
        this.combustible = combInicial;
        this.energia = eInicial;
        this.desgaste = 0;
        this.requiereMantenimiento = false;
        // this.estadoActual = new Disponible(this); cuando este el motor warp

    }

    public int getCombustible() {
        return this.combustible;
    }

    public int getEnergia() {
        return this.energia;
    }

    public int getDesgaste(){
        return this.desgaste;
    }

    public void consumirCombustible(int cantidad) {
        if (this.combustible >= cantidad) {
            this.combustible -= cantidad;
        } else {
            throw new RecursosInsuficientesException("Combustible insuficiente");
        }
    }

    public void consumirEnergia(int cantidad) {
        if (this.energia >= cantidad) {
            this.energia -= cantidad;
        } else {
            throw new RecursosInsuficientesException("Energía insuficiente");
        }
    }


    public void mantenimiento(){

        if (this.requiereMantenimiento){
            this.desgaste = 0;
            this.requiereMantenimiento = false;
        }else
            throw new MantenimientoException("La nave no necesita mantenimiento");//la aparte
    }

    public void incrementaDesgaste(int cant){
        int aux;
        if ((this.desgaste + cant) > 100){
            throw new ExcesoDesgaste("Desgaste por encima de lo que se va a usar");
        }else{
            this.desgaste += cant;
            if (this.desgaste >= 80)
                this.requiereMantenimiento = true;
        }
    }

    public void insertarTrioulante(Tripulante t){
        if (t == null)
            throw new NoexisteTripulanteException("El tripulante no puede ser nulo");
        if (tripulacion.contains(t))
            throw new IllegalArgumentException("El tripulante ya esta abordo");
        //cuadno se haga el motor warp con los estados podria poner validacion de no agregar tripulaion en pleno salto

        tripulacion.add(t);
    }

//    Una carga de combustible o energía no podrá superar la capacidad máxima. Una operación que no pueda
//    completarse sin violar un límite deberá rechazarse sin modificar parcialmente la nave

    public void cargaCombustible(int cant){
        if ((this.combustible + cant) > capMaxCombustible){
            throw new ExcesoRecurso("Se escede de la capacidad maxima");
        }
        this.combustible += cant ;
    }

    public void cargaEnergia(int cant){
        if ((this.energia + cant) > capMaxEnergia){
            throw new ExcesoRecurso("Se escede de la capacidad maxima");
        }
        this.energia += cant ;
    }
}
