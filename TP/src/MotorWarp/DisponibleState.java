package src.MotorWarp;

import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;

import java.util.ArrayList;

public class DisponibleState implements MotorState {

    private MotorWarp motorWarp;


    public DisponibleState(MotorWarp motorWarp) {
        this.motorWarp = motorWarp;
        this.motorWarp.registrarEventoMotor("Cambio de estado del Motor Warp","El Motor warp esta disponible y en espera");
    }

    @Override
    public void saltar(Tablero tablero, ArrayList<Posicion> ruta){
        this.prepararWarp();
        this.motorWarp.mover(tablero, ruta, 1);
    }

    @Override
    public void saltarWarp(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException {
        //Warp no inicializado, se salta normal
        this.saltar(tablero, ruta);
    }

    @Override
    public void prepararWarp() {
        motorWarp.setEstado(new PreparandoState(motorWarp));
    }

    @Override
    public void iniciarWarp() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("No se puede saltar sin antes preparar");
    }

    @Override
    public void ejecutandoWarp(Tablero tablero, ArrayList<Posicion> ruta) throws TransicionInvalidaException {
        throw new TransicionInvalidaException("No hubo salto; no se debe finalizar");
    }

    @Override
    public void enfriarWarp() throws TransicionRedundanteException {
        throw new TransicionRedundanteException("Motor ya disponible");
    }



    @Override
    public boolean motorDisponible() {
        return true;
    }
}
