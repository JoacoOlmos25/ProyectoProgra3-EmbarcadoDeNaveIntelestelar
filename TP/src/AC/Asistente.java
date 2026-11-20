package src.AC;

import src.ElementosDelUniverso.ColisionException;
import src.ElementosDelUniverso.ElementoDelUniverso;
import src.MotorWarp.MotorException;
import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;
import src.nave.Comandable;
import src.nave.Nave;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Asistente {
    private Comandable nave;
    private Bitacora bitacora = new Bitacora();

    public Asistente(Nave n){
      this.nave = n;
      this.nave.setBitacora(this.bitacora);
    }

    public int getCombustibleNave(){
        return nave.getCombustible();
    }

    public int getEnergiaNave(){
        return nave.getEnergia();
    }

    public boolean consultaReqMantenimiento(){
        return nave.requiereMantenimiento();
    }

    public boolean consultaMotorDisp(){
        return nave.consultarMotorDisponible();
    }

    public void despegue(int combustible, int energia, int desgaste){
        nave.consumirCombustible(combustible);
        nave.consumirEnergia(energia);
        nave.incrementaDesgaste(desgaste);
    }

    public void inspeccionarPosicionActual(Tablero tablero) throws ColisionException {

        ElementoDelUniverso colision = tablero.inspeccionarPosicion();

        if (colision != null)
            colision.interactuar(this.nave);
    }

    public void movimiento(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException {

        ElementoDelUniverso[][] entornoInmediato = tablero.observarAlrededores();
        //por contrato de .observarAlrededores(), la nave está en la posición (1, 1) del entorno

        Posicion naveEnTablero = tablero.getPosicionNave();
        Posicion coordenadasEntorno = new Posicion(naveEnTablero.getX() - 1, naveEnTablero.getY() - 1);
        Posicion siguienteCasillero = ruta.get(0);
        Posicion siguienteCasilleroEnEntorno = new Posicion( siguienteCasillero.getX() - coordenadasEntorno.getX(), siguienteCasillero.getY() - coordenadasEntorno.getY());

        if ( entornoInmediato [siguienteCasilleroEnEntorno.getX()] [siguienteCasilleroEnEntorno.getY()] == null)
            this.nave.saltar(tablero, ruta);
        else
            this.nave.saltarWarp(tablero, ruta);

    }


    public void mostrarBitacora(){
        bitacora.mostrarBitacora();
    }

    public void registrarEvento(String titulo, String msj) {
        bitacora.agregaEvento(LocalDateTime.now(), titulo, msj);
    }
}
