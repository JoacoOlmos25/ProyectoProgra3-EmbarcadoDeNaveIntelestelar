package src.nave;
import src.AC.Bitacora;
import src.MotorWarp.MotorWarp;
import src.Tripulacion.Tripulante;

import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class Nave implements FuenteDeRecursos, Mantenible {
    //constantes de nave
    private final int capMaxCombustible = 100;
    private final int capMaxEnergia = 100;
    //atributos comunes de nave
    private int combustible;
    private int energia;
    private int desgaste;
    protected ArrayList <Tripulante> tripulacion = new ArrayList<>();
    protected MotorWarp motorWarp;
    protected Bitacora bitacoraNave;


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
        this.motorWarp = new MotorWarp(this, this);
    }

    @Override
    public int getCombustible() {
        return this.combustible;
    }

    @Override
    public int getEnergia() {
        return this.energia;
    }

    @Override
    public int getDesgaste(){
        return this.desgaste;
    }

    /**Contrato
        *Precondicion
            *Cantidad es positiva
        *Postcondicion
            *Disminuye el combustible en la cantidad pasada
     * @throws RecursosInsuficientesException si cantidad > combustible disponible
     */

    @Override
    public void consumirCombustible(int cantidad) throws RecursosInsuficientesException {
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
     * @throws RecursosInsuficientesException si cantidad > combustible disponible
     */

    @Override
    public void consumirEnergia(int cantidad) throws RecursosInsuficientesException {
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
     * @throws MantenimientoException si la nave no requiere mantenimiento
     */
    @Override
    public void realizarMantenimiento() throws MantenimientoException {

        if (this.requiereMantenimiento()){
            this.desgaste = 0;
        }else
            throw new MantenimientoException("La nave no necesita mantenimiento");//la aparte
    }


    /**
     * Evalúa si la nave requiere mantenimiento según los límites de desgaste establecidos
     * @return true si la nave necesita mantenimiento, falso en caso contrario
     */
    @Override
    public boolean requiereMantenimiento(){
        return this.desgaste >= 80;
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
            *Incrementa el desgaste en la cantidad indicada
     * @throws MantenimientoException si incrementar el desgaste en la cantidad indicada supera el límite (100)
     */
    @Override
    public void incrementaDesgaste(int cant) throws MantenimientoException {

        if ((this.desgaste + cant) > 100){
            throw new MantenimientoException("Desgaste por encima del permitido");
        }else{
            this.desgaste += cant;
        }
    }


    public void setBitacora(Bitacora b) {
        this.bitacoraNave = b;
        this.motorWarp.setBitacora(b); // Le pasamos la referencia al motor
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
        this.bitacoraNave.agregaEvento(LocalDateTime.now(),"Se agrega un Tripulante",t.toString()+" Remuneracion total=" + t.getRemu()+"}");
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
