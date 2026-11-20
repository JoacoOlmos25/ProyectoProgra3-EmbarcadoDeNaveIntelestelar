package src.MotorWarp;

import src.AC.Bitacora;
import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;
import src.nave.FuenteDeRecursos;
import src.nave.Mantenible;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MotorWarp {

    public static final int COSTO_WARP_ENERGIA = 5;
    public static final int COSTO_WARP_DESGASTE = 3;
    public static final int COSTO_SALTO_ENERGIA = 2;

    private MotorState estado;
    private Bitacora bitacora;
    private FuenteDeRecursos recursos;
    private Mantenible estructuraNave;


    public MotorWarp(FuenteDeRecursos recursos, Mantenible estructuraNave) {
        this.estado = new DisponibleState(this);
        this.recursos = recursos;
        this.estructuraNave = estructuraNave;
    }

    //Facade para acceso a recursos. Alternativa no elegida: Facade para transiciones de estado.
    public boolean energiaSuficiente(){
        return recursos.getEnergia() > COSTO_WARP_ENERGIA;
    }

    public void consumeEnergiaPorSalto(){
        recursos.consumirEnergia(MotorWarp.COSTO_WARP_ENERGIA);
    }

    public void aplicaDesgastePorSalto(){
        estructuraNave.incrementaDesgaste(COSTO_WARP_DESGASTE);
    }

    public void setEstado(MotorState estado) {
        this.estado = estado;
    }

    public void saltar(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException{
        estado.saltar(tablero, ruta);
    }

    public void saltarWarp(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException {
        estado.saltarWarp(tablero, ruta);
    }


    /**
     *
     * @param tablero
     * @param ruta
     * @param pasos > 0
     */
    public void mover(Tablero tablero, ArrayList<Posicion> ruta, int pasos){

        Posicion posicionSiguiente;
        for (int i = 0; i < pasos; i++)
            posicionSiguiente = ruta.remove(0);

        tablero.moverNave(posicionSiguiente);
    }


    public boolean consultaMotorDisponible(){
        return estado.motorDisponible();
    }

    public void setBitacora(Bitacora b) {
        this.bitacora = b;
    }

    public void registrarEventoMotor(String titulo, String msj) {
        if(this.bitacora != null) {
            this.bitacora.agregaEvento(LocalDateTime.now(), titulo, msj);
        }
    }
}
