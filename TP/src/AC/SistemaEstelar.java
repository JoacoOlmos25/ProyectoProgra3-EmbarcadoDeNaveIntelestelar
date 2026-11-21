package src.AC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import src.destinoytablero.Posicion;
import src.Misiones.Mision;
import src.Misiones.MisionFactory;
import src.Tripulacion.AntiguedadNegativa;
import src.Tripulacion.Tripulante;
import src.Tripulacion.TripulanteFactory;
import src.destinoytablero.Destino;
import src.destinoytablero.Tablero;
import src.nave.MantenimientoException;
import src.nave.Nave;
import src.nave.NaveFactory;

/**
 * Modelo de la aplicación (Fachada).
 * Encapsula la flota, la nave activa, las fábricas, el tablero del universo y su destino.
 * Desconoce por completo la existencia de vistas y controladores.
 */
public class SistemaEstelar {

    private final NaveFactory naveFactory;
    private final TripulanteFactory tripulanteFactory;
    private final MisionFactory misionFactory;
    private final Tablero tablero;
    private final Destino destino;

    private final List<Asistente> flota;
    private final List<String> nombresFlota;
    /** Última posición conocida de cada nave de la flota (mismo índice que flota). */
    private final List<Posicion> posicionesFlota;
    private int indiceActivo = -1;


    public SistemaEstelar(Tablero tablero) {
        assert tablero != null : "El tablero no puede ser nulo";
        this.tablero = tablero;
        this.destino = new Destino(tablero);
        this.naveFactory = new NaveFactory();
        this.tripulanteFactory = new TripulanteFactory();
        this.misionFactory = new MisionFactory();
        this.flota = new ArrayList<>();
        this.nombresFlota = new ArrayList<>();
        this.posicionesFlota = new ArrayList<>();
    }

    public Tablero getTablero() {
        return tablero;
    }

    /**
     * Crea una nave del tipo indicado, la incorpora a la flota y la deja como activa en la base de origen.
     * Precondiciones: no hay una misión en curso
     * Postcondiciones: la nave anterior conserva su posición; la nueva queda activa y ubicada en la base del tablero
     * @param tipo tipo de nave ("Explorador", "Carguero" o "Ataque")
     * @return etiqueta de la nave creada
     * @throws IllegalArgumentException si el tipo de nave es desconocido (la flota no cambia)
     */
    public String crearNave(String tipo) {
        Nave nuevaNave = naveFactory.crearNave(tipo);
        Asistente nuevoAsistente = new Asistente(nuevaNave);
        String etiqueta = tipo + " #" + (flota.size() + 1);

        guardarPosicionActiva();
        nuevoAsistente.registrarEvento("Alta de Nave", "Se incorporó " + etiqueta + " a la flota.");
        flota.add(nuevoAsistente);
        nombresFlota.add(etiqueta);
        posicionesFlota.add(tablero.getPosicionBase());
        indiceActivo = flota.size() - 1;
        tablero.moverNave(posicionesFlota.get(indiceActivo));

        assert flota.size() == nombresFlota.size() && flota.size() == posicionesFlota.size()
                : "Las listas de la flota quedaron desalineadas";
        return etiqueta;
    }

    /**
     * Cambia la nave activa, guardando la posición de la anterior y ubicando en el tablero a la nueva.
     * Precondiciones: no hay una misión en curso
     * Postcondiciones: si el índice es válido y distinto del activo, la nave anterior conserva su posición
     *                  y la nave del tablero pasa a la última posición conocida de la nave seleccionada
     * @param indice posición de la nave en la flota
     * @return true si se cambió la nave activa, false en caso contrario
     */
    public boolean seleccionarNave(int indice) {
        if (indice < 0 || indice >= flota.size() || indice == indiceActivo) {
            return false;
        }
        guardarPosicionActiva();
        indiceActivo = indice;
        tablero.moverNave(posicionesFlota.get(indiceActivo));
        return true;
    }

    /**
     * Guarda la posición actual del tablero como la última posición conocida de la nave activa
     * Postcondiciones: si hay nave activa, su posición guardada coincide con la posición de la nave en el tablero
     */
    private void guardarPosicionActiva() {
        if (hayNaveActiva()) {
            posicionesFlota.set(indiceActivo, tablero.getPosicionNave());
        }
    }

    public String agregarTripulante(String cargo, String origen, int antiguedad, String ident)
            throws AntiguedadNegativa {
        Asistente ac = exigirAsistenteActivo();
        Tripulante tripulante = tripulanteFactory.crearTripulante(cargo, origen, antiguedad, ident);
        ac.insertarTripulante(tripulante);
        return ident + " (" + cargo + " - " + origen + ") | Haberes: $"
                + String.format("%.2f", tripulante.getRemu());
    }

    public void realizarMantenimiento() throws MantenimientoException {
        Asistente ac = exigirAsistenteActivo();
        ac.realizarMantenimiento();
    }

    /**
     * Crea la misión indicada para la nave activa, sobre el tablero y el destino del sistema.
     * Precondiciones: codigo es "M-01", "M-02" o "M-03"
     * Postcondiciones: devuelve una misión nueva, todavía no realizada; el estado del sistema no cambia
     * @param codigo código de la misión
     * @return misión lista para invocar realizarMision() desde un hilo secundario
     * @throws IllegalStateException si no hay una nave activa
     * @throws IllegalArgumentException si el código de misión es desconocido
     */
    public Mision crearMision(String codigo) {
        Asistente ac = exigirAsistenteActivo();
        return misionFactory.crearMision(codigo, ac, destino, tablero);
    }

    public void registrarEventoActivo(String titulo, String msj) {
        if (hayNaveActiva()) {
            flota.get(indiceActivo).registrarEvento(titulo, msj);
        }
    }

    // --- Consultas de estado para la capa de presentación (sin exponer colecciones mutables) ---

    public boolean hayNaveActiva() {
        return indiceActivo >= 0 && indiceActivo < flota.size();
    }

    public int getIndiceActivo() {
        return indiceActivo;
    }

    public String getNombreNaveActiva() {
        return hayNaveActiva() ? nombresFlota.get(indiceActivo) : null;
    }

    public List<String> getNombresFlota() {
        return Collections.unmodifiableList(nombresFlota);
    }

    public Asistente getAsistenteActivo() {
        return hayNaveActiva() ? flota.get(indiceActivo) : null;
    }

    public List<String> getResumenTripulacionActiva() {
        if (!hayNaveActiva()) {
            return Collections.emptyList();
        }
        List<String> detalle = new ArrayList<>();
        for (Tripulante t : flota.get(indiceActivo).getTripulacion()) {
            detalle.add(
                    t.getIdent()
                            + (t.esCapitan() ? " [CAPITÁN]" : "")
                            + " | Antig: " + t.getAntiguedad() + " años"
                            + " | Salario: $" + String.format("%.2f", t.getRemu())
            );
        }
        return detalle;
    }

    private Asistente exigirAsistenteActivo() {
        if (!hayNaveActiva()) {
            throw new IllegalStateException("Primero debe crear y seleccionar una nave.");
        }
        return flota.get(indiceActivo);
    }
}