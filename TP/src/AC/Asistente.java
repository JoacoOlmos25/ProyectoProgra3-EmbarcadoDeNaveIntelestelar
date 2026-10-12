package src.AC;

import src.nave.Nave;

import java.time.LocalDateTime;

public class Asistente {
    private Nave nave;
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
        return nave.consultaReqMantenimiento();
    }

    public boolean consultaMotorDisp(){
        return nave.consultaMotorDisponible();
    }

    public void mostrarBitacora(){
        bitacora.mostrarBitacora();
    }

    public void registrarEvento(String titulo, String msj) {
        bitacora.agregaEvento(LocalDateTime.now(), titulo, msj);
    }
}
