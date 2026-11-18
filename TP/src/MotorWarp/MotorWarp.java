package src.MotorWarp;

import src.AC.Bitacora;
import src.nave.FuenteDeRecursos;
import src.nave.Mantenible;

import java.time.LocalDateTime;

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


    /*
    public void saltoWarp() throws MotorException {
        estado.prepararSalto();
        estado.saltar();
        estado.ejecutarSalto();
        estado.enfriar();
    }

    Originalmente, este método realizaba todo el proceso del salto de principio a fin, pero como ahora la modalidad del juego es por turnos coordinados en un mismo hilo,
    las fases se deben separar para realizarse una por vez. Como la fase a ejecutar depende del estado particular del motor en ese momento, debe ser el mismo
    estado el que contenga la lógica concreta a ejecutar.
     */

    public void saltarWarp() throws MotorException {
        estado.saltarWarp();
    }

    public void cancelarSalto() throws MotorException {
        estado.cancelarSalto();
    } //qué pasa con el combustible si se cancela el salto?

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
