package src.MotorWarp;

import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;

import java.util.ArrayList;

public class PreparandoState implements MotorState {

    private MotorWarp motorWarp;

    public PreparandoState(MotorWarp motorWarp) {
        this.motorWarp = motorWarp;
        this.motorWarp.registrarEventoMotor("Cambio de estado del Motor Warp","El Motor warp se esta Preparando para el salto");
    }

    @Override
    public void saltar(Tablero tablero, ArrayList<Posicion> ruta){
        this.iniciarWarp();
        this.motorWarp.mover(tablero, ruta, 1);
    }

    @Override
    public void saltarWarp(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException {
        //Warp no inicializado, se salta normal
        this.saltar(tablero, ruta);
    }

    @Override
    public void prepararWarp() throws TransicionRedundanteException {
        throw new TransicionRedundanteException("Preparación de salto ya en curso");
    }

    @Override
    public void iniciarWarp() {
        if (motorWarp.energiaSuficiente())
            motorWarp.setEstado(new EjecutandoSaltoWarpState(motorWarp));
    }

    @Override
    public void ejecutandoWarp(Tablero tablero, ArrayList<Posicion> ruta) throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Preparando salto: no se puede finalizar");
    }

    @Override
    public void enfriarWarp() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Preparando salto: motor no disponible");
    }



    @Override
    public boolean motorDisponible() {
        return false;
    }

}
