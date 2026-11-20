package src.nave;

import src.AC.Bitacora;
import src.ElementosDelUniverso.ColisionException;
import src.MotorWarp.MotorException;
import src.MotorWarp.MotorWarp;
import src.Tripulacion.Tripulante;
import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Nave implements Comandable {
    //constantes de nave
    private final int capMaxCombustible = 100;
    private final int capMaxEnergia = 100;

    // Atributos comunes de nave
    private int combustible;
    private int energia;
    private int desgaste;
    protected ArrayList<Tripulante> tripulacion = new ArrayList<>();
    protected MotorWarp motorWarp;
    protected Bitacora bitacoraNave;

    /**
     * Contrato:
     * Creación de la nave con sus atributos base (combustible, energía, desgaste, estado motor warp).
     * Precondición:
     * - 0 <= combInicial <= capMaxCombustible
     * - 0 <= eInicial <= capMaxEnergia
     * Invariante de clase:
     * - 0 <= combustible <= capMaxCombustible
     * - 0 <= energia <= capMaxEnergia
     * - 0 <= desgaste <= 100
     * Postcondición:
     * - Devuelve la nave con todos los recursos y el motor warp disponible.
     */
    public Nave(int combInicial, int eInicial) {
        assert combInicial >= 0 && combInicial <= capMaxCombustible : "Combustible inicial fuera de rango";
        assert eInicial >= 0 && eInicial <= capMaxEnergia : "Energía inicial fuera de rango";

        this.combustible = combInicial;
        this.energia = eInicial;
        this.desgaste = 0;
        this.motorWarp = new MotorWarp(this, this);

        verificarInvariantes();
    }

    private void verificarInvariantes() {
        assert this.combustible >= 0 && this.combustible <= capMaxCombustible : "Invariante de combustible violado";
        assert this.energia >= 0 && this.energia <= capMaxEnergia : "Invariante de energía violado";
        assert this.desgaste >= 0 && this.desgaste <= 100 : "Invariante de desgaste violado";
    }

    @Override
    public int getCombustible() {
        return this.combustible;
    }

    @Override
    public int getEnergia() {
        return this.energia;
    }

    @Override
    public int getDesgaste() {
        return this.desgaste;
    }

    public List<Tripulante> getTripulacion() {
        return Collections.unmodifiableList(this.tripulacion);
    }

    /**
     * Precondición: cantidad > 0
     * Postcondición: combustible == combustible_anterior - cantidad
     * @throws RecursosInsuficientesException si cantidad > combustible disponible
     */
    @Override
    public void consumirCombustible(int cantidad) throws RecursosInsuficientesException {
        assert cantidad > 0 : "La cantidad de combustible a consumir debe ser positiva";

        if (this.combustible < cantidad) {
            throw new RecursosInsuficientesException("Combustible insuficiente");
        }
        int anterior = this.combustible;
        this.combustible -= cantidad;

        assert this.combustible == anterior - cantidad : "Postcondición violada al consumir combustible";
        verificarInvariantes();
    }

    /**
     * Precondición: cantidad > 0
     * Postcondición: energia == energia_anterior - cantidad
     * @throws RecursosInsuficientesException si cantidad > energía disponible
     */
    @Override
    public void consumirEnergia(int cantidad) throws RecursosInsuficientesException {
        assert cantidad > 0 : "La cantidad de energía a consumir debe ser positiva";

        if (this.energia < cantidad) {
            throw new RecursosInsuficientesException("Energía insuficiente");
        }
        int anterior = this.energia;
        this.energia -= cantidad;

        assert this.energia == anterior - cantidad : "Postcondición violada al consumir energía";
        verificarInvariantes();
    }

    @Override
    public void recibirDanio(int cantidad) throws ColisionException {
        try {
            consumirEnergia(cantidad);
        } catch (RecursosInsuficientesException e) {
            throw new ColisionException("La nave se quedó sin energía");
        }
    }

    @Override
    public void recibirAyuda(int cantidadCombustible, int cantidadEnergia) {
        this.combustible += Math.min(cantidadCombustible, this.capMaxCombustible - this.combustible);
        this.energia += Math.min(cantidadEnergia, this.capMaxEnergia - this.energia);
    }


    /**
     * Precondición: la nave requiere mantenimiento (desgaste >= 80)
     * Postcondición: desgaste == 0
     * @throws MantenimientoException si la nave no requiere mantenimiento
     */
    @Override
    public void realizarMantenimiento() throws MantenimientoException {
        if (this.requiereMantenimiento()) {
            this.desgaste = 0;
        } else {
            throw new MantenimientoException("La nave no necesita mantenimiento");
        }

        assert this.desgaste == 0 : "Postcondición violada al realizar mantenimiento";
        verificarInvariantes();
    }

    /**
     * Evalúa si la nave requiere mantenimiento según los límites de desgaste establecidos.
     * @return true si la nave necesita mantenimiento, false en caso contrario.
     */
    @Override
    public boolean requiereMantenimiento() {
        return this.desgaste >= 80;
    }

    /**
     * Consulta si el motor Warp está disponible.
     * @return true si el motor está disponible.
     */
    @Override
    public boolean consultarMotorDisponible() {
        return motorWarp.consultaMotorDisponible();
    }

    /**
     * Precondición: cant > 0
     * Postcondición: desgaste == desgaste_anterior + cant
     * @throws MantenimientoException si incrementar el desgaste supera el límite (100)
     */
    @Override
    public void incrementaDesgaste(int cant) throws MantenimientoException {
        assert cant > 0 : "La cantidad de desgaste a incrementar debe ser positiva";

        if ((this.desgaste + cant) > 100) {
            throw new MantenimientoException("Desgaste por encima del permitido");
        }
        int anterior = this.desgaste;
        this.desgaste += cant;

        assert this.desgaste == anterior + cant : "Postcondición violada al incrementar desgaste";
        verificarInvariantes();
    }

    @Override
    public void setBitacora(Bitacora b) {
        assert b != null : "La bitácora no puede ser nula";
        this.bitacoraNave = b;
        this.motorWarp.setBitacora(b);
    }

    /**
     * Incorpora un tripulante a la nave preservando identidad única y capitán único.
     */
    public void insertarTripulante(Tripulante t) {
        if (t == null) {
            throw new NoexisteTripulanteException("El tripulante no puede ser nulo");
        }
        if (tripulacion.contains(t)) {
            throw new IllegalArgumentException("Ya existe un tripulante con esa identidad a bordo");
        }
        if (t.esCapitan() && tieneCapitan()) {
            throw new IllegalArgumentException("La nave ya cuenta con un capitán a bordo");
        }

        tripulacion.add(t);
        if (this.bitacoraNave != null) {
            this.bitacoraNave.agregaEvento(
                    LocalDateTime.now(),
                    "Se agrega un Tripulante",
                    t.toString() + " Remuneracion total=" + t.getRemu() + "}"
            );
        }
    }

    private boolean tieneCapitan() {
        for (Tripulante existente : this.tripulacion) {
            if (existente.esCapitan()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Precondición: cant > 0
     * Postcondición: combustible == combustible_anterior + cant
     */
    @Override
    public void cargarCombustible(int cant) {
        assert cant > 0 : "La cantidad de combustible a cargar debe ser positiva";

        if ((this.combustible + cant) > capMaxCombustible) {
            throw new ExcesoRecursoException("Se excede de la capacidad maxima");
        }
        int anterior = this.combustible;
        this.combustible += cant;

        assert this.combustible == anterior + cant : "Postcondición violada al cargar combustible";
        verificarInvariantes();
    }

    /**
     * Precondición: cant > 0
     * Postcondición: energia == energia_anterior + cant
     */
    @Override
    public void cargarEnergia(int cant) {
        assert cant > 0 : "La cantidad de energía a cargar debe ser positiva";

        if ((this.energia + cant) > capMaxEnergia) {
            throw new ExcesoRecursoException("Se excede de la capacidad maxima");
        }
        int anterior = this.energia;
        this.energia += cant;

        assert this.energia == anterior + cant : "Postcondición violada al cargar energía";
        verificarInvariantes();
    }


    @Override
    public void saltar(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException {
        this.motorWarp.saltar(tablero, ruta);
    }

    @Override
    public void saltarWarp(Tablero tablero, ArrayList<Posicion> ruta) throws MotorException {
        this.motorWarp.saltarWarp(tablero, ruta);
    }
}