package src.AC;

import src.ElementosDelUniverso.ColisionException;
import src.ElementosDelUniverso.ElementoDelUniverso;
import src.MotorWarp.MotorException;
import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;
import src.nave.RecursosInsuficientesException;
import src.nave.Comandable;
import src.nave.Nave;
import java.util.List;
import src.Tripulacion.Tripulante;
import src.nave.MantenimientoException;
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

    /**
     * Aplica a la nave el costo inicial de una misión.
     * Precondiciones: combustible >= 0, energia >= 0, desgaste >= 0
     * Postcondiciones: se descuentan los recursos y se suma el desgaste indicados; los valores en 0 no se aplican
     * @throws RecursosInsuficientesException si la nave no cuenta con los recursos
     * @throws MantenimientoException si el desgaste resultante supera el permitido
     */
    public void despegue(int combustible, int energia, int desgaste){
        assert combustible >= 0 && energia >= 0 && desgaste >= 0 : "Los costos de despegue no pueden ser negativos";

        if (combustible > 0) nave.consumirCombustible(combustible);
        if (energia > 0) nave.consumirEnergia(energia);
        if (desgaste > 0) nave.incrementaDesgaste(desgaste);
    }

    /**
     * Transfiere recursos de la nave a una nave aliada (misión M-01).
     * Precondiciones: combustible >= 0, energia >= 0
     * Postcondiciones: si la nave cuenta con ambos recursos, se descuentan; si no, no se descuenta ninguno
     * @throws RecursosInsuficientesException si la nave no cuenta con alguno de los recursos solicitados
     */
    public void transferirRecursos(int combustible, int energia) {
        assert combustible >= 0 && energia >= 0 : "Los recursos a transferir no pueden ser negativos";

        if (nave.getCombustible() < combustible || nave.getEnergia() < energia)
            throw new RecursosInsuficientesException("La nave no tiene recursos suficientes para la transferencia");
        if (combustible > 0) nave.consumirCombustible(combustible);
        if (energia > 0) nave.consumirEnergia(energia);
    }

    /**
     * Reacondiciona por completo la nave (misión M-03).
     * Precondiciones: ninguna
     * Postcondiciones: la nave queda con combustible y energía al máximo y desgaste en 0
     */
    public void reacondicionarNave() {
        nave.reacondicionamientoTotal();
    }

    /**
     * Devuelve el motor de la nave al estado disponible.
     * Precondiciones: ninguna
     * Postcondiciones: el motor queda disponible
     * @throws MantenimientoException si el enfriamiento pendiente supera el desgaste permitido
     */
    public void reiniciarMotor() {
        nave.reiniciarMotor();
    }
}
