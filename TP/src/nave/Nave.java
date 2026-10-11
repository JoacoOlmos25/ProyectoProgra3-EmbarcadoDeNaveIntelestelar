package src.nave;
import src.MotorWarp.MotorWarp;
import src.Tripulacion.Tripulante;

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
    protected MotorWarp motorWarp;

    /**Contrato
        *Creacion de la nave con sus atributos base (combustible, energia, desgaste, estado motor warp)
        *Precondicion
            *- Ninguna adicional (los datos ya están garantizados por el invariante de la clase).
        *Invariante de clase
            *-Combustible y energia deben ser mayor a 0
            *-Desgaste se inicializa en 0
            *-El estado del motor warp se inicia en disponible
        *Postcondicion
            *Devuelve la nave con todos los recursos y estados cargados
     */

    public Nave(int combInicial, int eInicial) {
        this.combustible = combInicial;
        this.energia = eInicial;
        this.desgaste = 0;
        this.requiereMantenimiento = false;
        this.motorWarp = new MotorWarp();
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

    /**Contrato
        *Precondicion
            *Cantidad es positiva
        *Postcondicion
            *Disminuye el combustible en la cantidad pasada
     */

    public void consumirCombustible(int cantidad) {
        if (this.combustible >= cantidad) {
            this.combustible -= cantidad;
        } else {
            throw new RecursosInsuficientesException("Combustible insuficiente");
        }
    }

    /**Contrato
        *Precondicion
            *Cantidad es positiva
        *Postcondicion
            *Disminuye la energia en la cantidad pasada
     */

    public void consumirEnergia(int cantidad) {
        if (this.energia >= cantidad) {
            this.energia -= cantidad;
        } else {
            throw new RecursosInsuficientesException("Energía insuficiente");
        }
    }

    /**Contrato
        *Precondicion
            *Comprueba si es necesario realizar mantenimiento en la nave
        *Postcondicion
            *Devuelve la nave en perfecto estado
     */

    public void realizaMantenimiento(){

        if (this.requiereMantenimiento){
            this.desgaste = 0;
            this.requiereMantenimiento = false;
        }else
            throw new MantenimientoException("La nave no necesita mantenimiento");//la aparte
    }


    /**
     * Evalúa si la nave requiere mantenimiento según los límites de desgaste establecidos
     * @return true si la nave necesita mantenimiento, falso en caso contrario
     */
    public boolean consultaReqMantenimiento(){
        return this.requiereMantenimiento;
    }


    /**
     * Consulta si el motor Warp está disponible
     * @return true si el motor está disponible
     */
    public boolean consultaMotorDisponible(){
        return motorWarp.consultaMotorDisponible();
    }

    /**Contrato
        *Precondicion
            *Cant es positiva
        *Postcondicion
            *Devuelve la nave con un desgaste
     */

    public void incrementaDesgaste(int cant){
        int aux;
        if ((this.desgaste + cant) > 100){
            throw new ExcesoDesgasteException("Desgaste por encima de lo que se va a usar");
        }else{
            this.desgaste += cant;
            if (this.desgaste >= 80)
                this.requiereMantenimiento = true;
        }
    }

    /**Contrato
        *Precondicion
            *t debe tener una tripulacion
        *Postcondicion
            *Devuelve dentro de la nave el nuevo tripulante
     */

    public void insertarTripulante(Tripulante t){
        if (t == null)
            throw new NoexisteTripulanteException("El tripulante no puede ser nulo");
        if (tripulacion.contains(t))
            throw new IllegalArgumentException("El tripulante ya esta abordo");
        //cuadno se haga el motor warp con los estados podria poner validacion de no agregar tripulaion en pleno salto

        tripulacion.add(t);
    }

//    Una carga de combustible o energía no podrá superar la capacidad máxima. Una operación que no pueda
//    completarse sin violar un límite deberá rechazarse sin modificar parcialmente la nave

    /**Contrato
        *Precondicion
            *Cant es positiva
        *Postcondicion
            *Devuelve la nave cargada con la nueva cantidad de combustible
     */

    public void cargaCombustible(int cant){
        if ((this.combustible + cant) > capMaxCombustible){
            throw new ExcesoRecursoException("Se escede de la capacidad maxima");
        }
        this.combustible += cant ;
    }

    /**Contrato
        *Precondicion
            *Cant es positiva
        *Postcondicion
            *Devuelve la nave cargada con la nueva cantidad de energia
     */

    public void cargaEnergia(int cant){
        if ((this.energia + cant) > capMaxEnergia){
            throw new ExcesoRecursoException("Se escede de la capacidad maxima");
        }
        this.energia += cant ;
    }
}
