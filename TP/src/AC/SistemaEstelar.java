package src.AC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import src.Tripulacion.AntiguedadNegativa;
import src.Tripulacion.Tripulante;
import src.Tripulacion.TripulanteFactory;
import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;
import src.nave.MantenimientoException;
import src.nave.Nave;
import src.nave.NaveFactory;

/**
 * Modelo de la aplicación (Fachada).
 * Encapsula la flota, la nave activa, las fábricas y el tablero del universo.
 * Desconoce por completo la existencia de vistas y controladores.
 */
public class SistemaEstelar {

    private final NaveFactory naveFactory;
    private final TripulanteFactory tripulanteFactory;
    private final Tablero tablero;

    private final List<Asistente> flota;
    private final List<String> nombresFlota;
    private int indiceActivo = -1;

    public SistemaEstelar(Tablero tablero) {
        assert tablero != null : "El tablero no puede ser nulo";
        this.tablero = tablero;
        this.naveFactory = new NaveFactory();
        this.tripulanteFactory = new TripulanteFactory();
        this.flota = new ArrayList<>();
        this.nombresFlota = new ArrayList<>();
    }

    public Tablero getTablero() {
        return tablero;
    }

    public String crearNave(String tipo) {
        Nave nuevaNave = naveFactory.crearNave(tipo);
        Asistente nuevoAsistente = new Asistente(nuevaNave);
        String etiqueta = tipo + " #" + (flota.size() + 1);

        nuevoAsistente.registrarEvento("Alta de Nave", "Se incorporó " + etiqueta + " a la flota.");
        flota.add(nuevoAsistente);
        nombresFlota.add(etiqueta);
        indiceActivo = flota.size() - 1;
        return etiqueta;
    }

    public boolean seleccionarNave(int indice) {
        if (indice < 0 || indice >= flota.size() || indice == indiceActivo) {
            return false;
        }
        indiceActivo = indice;
        return true;
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
     * Valida las condiciones previas de despegue y calcula la ruta planificada
     * (punto de enganche para delegar luego al objeto Mision definitivo).
     */
    public List<Posicion> prepararRutaMision() {
        Asistente ac = exigirAsistenteActivo();
        if (ac.consultaReqMantenimiento()) {
            throw new IllegalStateException("La nave requiere mantenimiento (desgaste >= 80%) antes de despegar.");
        }
        Posicion origen = tablero.getPosicionNave();
        Posicion destino = tablero.obtenerObjetivo();
        return calcularRuta(origen, destino);
    }

    private List<Posicion> calcularRuta(Posicion inicio, Posicion fin) {
        List<Posicion> ruta = new ArrayList<>();
        int f = inicio.getX();
        int c = inicio.getY();
        ruta.add(new Posicion(f, c));
        while (f != fin.getX() || c != fin.getY()) {
            if (f < fin.getX()) f++;
            else if (f > fin.getX()) f--;
            if (c < fin.getY()) c++;
            else if (c > fin.getY()) c--;
            ruta.add(new Posicion(f, c));
        }
        return ruta;
    }

    public String generarResumenInforme(String mision, Posicion origen, Posicion destino, int encuentros) {
        Asistente ac = exigirAsistenteActivo();
        return "Nave: " + getNombreNaveActiva() + "\n"
                + "Misión ejecutada: " + mision + "\n"
                + "Trayectoria: (" + origen.getX() + ", " + origen.getY() + ") -> ("
                + destino.getX() + ", " + destino.getY() + ")\n"
                + "Encuentros registrados: " + encuentros + "\n"
                + "Estado final -> Combustible: " + ac.getCombustibleNave()
                + " | Energía: " + ac.getEnergiaNave()
                + " | Desgaste: " + ac.getDesgasteNave() + "%\n"
                + "Motor Warp disponible: " + (ac.consultaMotorDisp() ? "SÍ" : "NO");
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