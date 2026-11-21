package src.MotorWarp;

import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;

import java.util.ArrayList;

public class EnfriandoState implements MotorState {

    private MotorWarp motorWarp;

    public EnfriandoState(MotorWarp motorWarp) {
        this.motorWarp = motorWarp;
        this.motorWarp.registrarEventoMotor("Cambio de estado del Motor Warp","El Motor warp se esta enfriando luego de un salto");
    }

    @Override
    public void saltar(Tablero tablero, ArrayList<Posicion> ruta){
        this.enfriarWarp();
    }

    @Override
    public void saltarWarp(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException {
        this.enfriarWarp();
    }


    @Override
    public void prepararWarp() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Motor enfriándose: todavía no se puede utilizar");
    }

    @Override
    public void iniciarWarp() throws TransicionInvalidaException {
        throw new TransicionInvalidaException("Motor enfriándose: todavía no se puede utilizar");
    }

    @Override
    public void ejecutandoWarp(Tablero tablero, ArrayList<Posicion> ruta) throws TransicionRedundanteException {
        throw new TransicionRedundanteException("Salto ya finalizado, enfriamiento ya en curso");
    }

    @Override
    public void enfriarWarp() {
        motorWarp.aplicaDesgastePorSalto();
        motorWarp.registrarEventoMotor(
                "Enfriamiento de Motor Warp completado",
                "Desgaste estructural incrementado en " + MotorWarp.COSTO_WARP_DESGASTE + " unidades."
        );
        motorWarp.setEstado(new DisponibleState(motorWarp));
    }



    @Override
    public boolean motorDisponible() {
        return false;
    }

    @Override
    public void reiniciarWarp() {
        //Se completa el enfriamiento (aplica el desgaste y pasa a Disponible)
        this.enfriarWarp();
    }
}
