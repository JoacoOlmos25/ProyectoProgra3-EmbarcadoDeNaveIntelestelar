package src.AC;

import src.nave.Nave;
import java.util.List;
import src.Tripulacion.Tripulante;
import src.nave.MantenimientoException;
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
        return nave.requiereMantenimiento();
    }

    public boolean consultaMotorDisp(){
        return nave.consultaMotorDisponible();
    }

    public int getDesgasteNave() {
        return nave.getDesgaste();
    }

    public String getTipoNave() {
        return nave.getClass().getSimpleName();
    }

    public void insertarTripulante(Tripulante t) {
        nave.insertarTripulante(t);
    }

    public List<Tripulante> getTripulacion() {
        return nave.getTripulacion();
    }

    public void realizarMantenimiento() throws MantenimientoException {
        nave.realizarMantenimiento();
        registrarEvento("Mantenimiento", "Mantenimiento completado. Desgaste reiniciado a 0%.");
    }

    public List<Evento> getEventosBitacora() {
        return bitacora.getEventos();
    }

    public void mostrarBitacora(){
        bitacora.mostrarBitacora();
    }

    public void registrarEvento(String titulo, String msj) {
        bitacora.agregaEvento(LocalDateTime.now(), titulo, msj);
    }
}
