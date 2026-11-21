package src.MotorWarp;

import src.AC.Bitacora;
import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;
import src.nave.FuenteDeRecursos;
import src.nave.Mantenible;
import src.nave.RecursosInsuficientesException;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MotorWarp {

    public static final int COSTO_WARP_ENERGIA = 5;
    public static final int COSTO_WARP_DESGASTE = 3;
    public static final int COSTO_SALTO_COMBUSTIBLE = 2;

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
     * Realiza un salto normal: consume el combustible del salto y avanza un casillero sobre la ruta.
     * Precondiciones: tablero y ruta no nulos, la ruta no está vacía
     * Postcondiciones: el combustible de la nave disminuye en COSTO_SALTO_COMBUSTIBLE y la nave
     *                  queda en la primera posición de la ruta, que se quita de la misma
     * @throws RecursosInsuficientesException si la nave no tiene combustible para el salto (la nave no se mueve)
     */
    public void saltoNormal(Tablero tablero, ArrayList<Posicion> ruta) {
        recursos.consumirCombustible(COSTO_SALTO_COMBUSTIBLE);
        this.mover(tablero, ruta, 1);
    }

    /**
     * Avanza la nave sobre la ruta la cantidad de pasos indicada, consumiendo las posiciones recorridas.
     * @param tablero tablero donde se mueve la nave, NotNull
     * @param ruta posiciones pendientes de la ruta, NotNull
     * @param pasos cantidad de casilleros a avanzar
     *
     * Precondiciones: pasos > 0, la ruta no está vacía
     * Postcondiciones: se quitan de la ruta min(pasos, ruta.size()) posiciones y la nave queda en la última
     *                  de ellas; si quedaban menos posiciones que pasos, la nave avanza solo las que quedaban
     */
    public void mover(Tablero tablero, ArrayList<Posicion> ruta, int pasos){
        assert tablero != null && ruta != null : "El tablero y la ruta no pueden ser nulos";
        assert pasos > 0 : "La cantidad de pasos debe ser positiva";
        assert !ruta.isEmpty() : "No hay posiciones pendientes en la ruta";

        int pasosReales = Math.min(pasos, ruta.size());
        Posicion posicionSiguiente = null;
        for (int i = 0; i < pasosReales; i++)
            posicionSiguiente = ruta.remove(0);

        tablero.moverNave(posicionSiguiente);
    }

    public boolean consultaMotorDisponible(){
        return estado.motorDisponible();
    }

    public void registrarEventoMotor(String titulo, String msj) {
        if(this.bitacora != null) {
            this.bitacora.agregaEvento(LocalDateTime.now(), titulo, msj);
        }
    }

    /**
     * Devuelve el motor al estado disponible al cerrar una misión.
     * Precondiciones: ninguna
     * Postcondiciones: consultaMotorDisponible() == true, salvo que el enfriamiento pendiente supere el desgaste permitido
     * @throws MantenimientoException si al completar el enfriamiento el desgaste supera el permitido
     */
    public void reiniciar(){
        estado.reiniciarWarp();
    }

    public void saltoNormal(ArrayList<Posicion> ruta,Tablero tablero) {
        recursos.consumirCombustible(COSTO_SALTO_COMBUSTIBLE);
        this.mover(tablero, ruta, 1);
        this.registrarEventoMotor(
                "Salto Normal ejecutado",
                "Avance de 1 casillero. Combustible consumido: " + COSTO_SALTO_COMBUSTIBLE
        );
    }

    public void setBitacora(Bitacora b) {
        this.bitacora = b;
        if (b != null) {
            this.registrarEventoMotor(
                    "Motor Warp conectado",
                    "Motor inicializado. Disponible: " + this.consultaMotorDisponible()
            );
        }
    }
}
