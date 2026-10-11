package src.AC;

import src.nave.Nave;

public class Asistente {
    private Nave nave;
    private Bitacora bitacora = new Bitacora();

    public Asistente(Nave n){
      this.nave = n;
    };

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
}
