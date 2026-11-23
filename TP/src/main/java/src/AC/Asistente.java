package src.AC;

import src.ElementosDelUniverso.ColisionException;
import src.ElementosDelUniverso.ElementoDelUniverso;
import src.MotorWarp.MotorException;
import src.Tripulacion.Tripulante;
import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;
import src.nave.Comandable;
import src.nave.MantenimientoException;
import src.nave.Nave;
import src.nave.RecursosInsuficientesException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Asistente de Comando de una nave individual.
 * Coordina la navegación por radar, las interacciones con el tablero, la gestión de recursos y la bitácora.
 *
 * Invariante de clase:
 * - nave != null
 * - bitacora != null
 */
public class Asistente {
    private final Comandable nave;
    private final Bitacora bitacora = new Bitacora();

    /**
     * Crea un asistente vinculado a la nave indicada y asocia su bitácora.
     * Precondiciones: n != null
     * Postcondiciones: la nave y su motor quedan vinculados a la bitácora del asistente
     */
    public Asistente(Nave n) {
        assert n != null : "La nave administrada por el asistente no puede ser nula";
        this.nave = n;
        this.nave.setBitacora(this.bitacora);
    }

    public int getCombustibleNave() {
        return nave.getCombustible();
    }

    public int getEnergiaNave() {
        return nave.getEnergia();
    }

    public int getDesgasteNave() {
        return nave.getDesgaste();
    }

    public String getTipoNave() {
        return nave.getClass().getSimpleName();
    }

    public boolean consultaReqMantenimiento() {
        return nave.requiereMantenimiento();
    }

    public boolean consultaMotorDisp() {
        return nave.consultarMotorDisponible();
    }

    /**
     * Inspecciona la casilla actual de la nave en el tablero y resuelve la interacción si hay un elemento.
     * Precondiciones: tablero != null
     * Postcondiciones: si existe un ElementoDelUniverso en la posición actual, se invoca interactuar(nave)
     * @throws ColisionException si la interacción resulta letal o derrota a la nave
     */
    public void inspeccionarPosicionActual(Tablero tablero) throws ColisionException {
        assert tablero != null : "El tablero no puede ser nulo";
        ElementoDelUniverso colision = tablero.inspeccionarPosicion();
        if (colision != null) {
            colision.interactuar(this.nave);
        }
    }

    /**
     * Evalúa el entorno inmediato con el radar de corto alcance y ordena salto normal o Salto Warp.
     * Precondiciones: tablero != null, ruta != null && !ruta.isEmpty()
     * Postcondiciones: si la próxima casilla está despejada ejecuta salto normal; si hay un elemento, intenta Salto Warp
     * @throws MotorException si el motor produce una falla de transición
     */
    public void movimiento(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException {
        assert tablero != null : "El tablero no puede ser nulo";
        assert ruta != null && !ruta.isEmpty() : "La ruta no puede ser nula ni vacía";

        ElementoDelUniverso[][] entornoInmediato = tablero.observarAlrededores();
        // Por contrato de observarAlrededores(), la nave está en la posición (1, 1) del entorno
        Posicion naveEnTablero = tablero.getPosicionNave();
        Posicion coordenadasEntorno = new Posicion(naveEnTablero.getX() - 1, naveEnTablero.getY() - 1);
        Posicion siguienteCasillero = ruta.get(0);
        Posicion siguienteCasilleroEnEntorno = new Posicion(
                siguienteCasillero.getX() - coordenadasEntorno.getX(),
                siguienteCasillero.getY() - coordenadasEntorno.getY()
        );

        if (entornoInmediato[siguienteCasilleroEnEntorno.getX()][siguienteCasilleroEnEntorno.getY()] == null) {
            this.nave.saltar(tablero, ruta);
        } else {
            this.nave.saltarWarp(tablero, ruta);
        }
    }

    public void insertarTripulante(Tripulante t) {
        nave.insertarTripulante(t);
    }

    public List<Tripulante> getTripulacion() {
        return nave.getTripulacion();
    }

    /**
     * Solicita el mantenimiento estructural de la nave y registra el resultado en bitácora.
     * Precondiciones: la nave requiere mantenimiento (desgaste >= 80%)
     * Postcondiciones: el desgaste queda en 0% y se registra el evento en la bitácora
     * @throws MantenimientoException si la nave no requiere mantenimiento
     */
    public void realizarMantenimiento() throws MantenimientoException {
        nave.realizarMantenimiento();
        registrarEvento("Mantenimiento", "Mantenimiento completado. Desgaste reiniciado a 0%.");
    }

    public List<Evento> getEventosBitacora() {
        return bitacora.getEventos();
    }

    /**
     * Agrega un nuevo evento a la bitácora de la nave con la hora actual.
     * Precondiciones: titulo != null && !titulo.isBlank(), msj != null && !msj.isBlank()
     * Postcondiciones: la cantidad de eventos en la bitácora se incrementa en 1
     */
    public void registrarEvento(String titulo, String msj) {
        assert titulo != null && !titulo.isBlank() : "El título del evento no puede ser nulo ni vacío";
        assert msj != null && !msj.isBlank() : "El mensaje del evento no puede ser nulo ni vacío";
        bitacora.agregaEvento(LocalDateTime.now(), titulo, msj);
    }

    /**
     * Aplica a la nave el costo inicial de una misión.
     * Precondiciones: combustible >= 0, energia >= 0, desgaste >= 0
     * Postcondiciones: se descuentan los recursos y se suma el desgaste indicados; los valores en 0 no se aplican
     * @throws RecursosInsuficientesException si la nave no cuenta con los recursos
     * @throws MantenimientoException si el desgaste resultante supera el permitido
     */
    public void despegue(int combustible, int energia, int desgaste) {
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

        if (nave.getCombustible() < combustible || nave.getEnergia() < energia) {
            throw new RecursosInsuficientesException("La nave no tiene recursos suficientes para la transferencia");
        }
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
     * Postcondiciones: el motor queda disponible (salvo desgaste excedido)
     * @throws MantenimientoException si el enfriamiento pendiente supera el desgaste permitido
     */
    public void reiniciarMotor() {
        nave.reiniciarMotor();
    }
}