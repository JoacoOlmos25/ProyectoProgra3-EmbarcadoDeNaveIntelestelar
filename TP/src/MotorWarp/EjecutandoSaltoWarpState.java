package src.MotorWarp;

import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;

import java.util.ArrayList;

public class EjecutandoSaltoWarpState implements MotorState {

    private MotorWarp motorWarp;

    public EjecutandoSaltoWarpState(MotorWarp motorWarp) {
        this.motorWarp = motorWarp;
        this.motorWarp.registrarEventoMotor("Cambio de estado del Motor Warp","El Motor warp esta activo y en curso");
    }

    public void saltar(Tablero tablero, ArrayList<Posicion> ruta){
        this.motorWarp.mover(tablero, ruta, 1);
    }

    @Override
    public void saltarWarp(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException {
        this.ejecutandoWarp(tablero, ruta);
    }

    @Override
    public void prepararWarp() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Warp en curso: no se puede preparar un nuevo salto");
    }

    @Override
    public void iniciarWarp() throws TransicionRedundanteException {
        throw new TransicionRedundanteException("Warp en curso: no se puede realizar un nuevo salto");
    }

    @Override
    public void ejecutandoWarp(Tablero tablero, ArrayList<Posicion> ruta) {
        if (motorWarp.energiaSuficiente()) {
            motorWarp.consumeEnergiaPorSalto();
            motorWarp.mover(tablero, ruta, 2);
            motorWarp.setEstado(new EnfriandoState(motorWarp));
        }
        else {
            //energía insuficiente, se "desinicializa" Warp y vuelve a preparando
            motorWarp.setEstado(new PreparandoState(motorWarp));
        }
    }

    @Override
    public void enfriarWarp() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Warp en curso: motor no disponible");
    }



    @Override
    public boolean motorDisponible() {
        return false;
    }

}
