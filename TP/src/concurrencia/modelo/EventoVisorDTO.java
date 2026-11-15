package src.concurrencia.modelo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventoVisorDTO implements Serializable {
    private final String mensajeLog;
    private final int bateriasActuales;
    private final int capacidadMaxima;
    private final int cantidadGeneradoras;
    private final int cantidadNaves;
    private final List<EstadoEntidadDTO> generadorasSnapshot;
    private final List<EstadoEntidadDTO> navesSnapshot;

    public EventoVisorDTO(String mensajeLog, int bateriasActuales, int capacidadMaxima, List<EstadoEntidadDTO> generadoras, List<EstadoEntidadDTO> naves) {
        this.mensajeLog = mensajeLog;
        this.bateriasActuales = bateriasActuales;
        this.capacidadMaxima = capacidadMaxima;

        this.generadorasSnapshot = Collections.unmodifiableList(new ArrayList<>(generadoras));
        this.navesSnapshot = Collections.unmodifiableList(new ArrayList<>(naves));

        this.cantidadGeneradoras = this.generadorasSnapshot.size();
        this.cantidadNaves = this.navesSnapshot.size();
    }

    public String getMensajeLog() {
        return mensajeLog;
    }

    public int getBateriasActuales() {
        return bateriasActuales;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public int getCantidadGeneradoras() {
        return cantidadGeneradoras;
    }

    public int getCantidadNaves() {
        return cantidadNaves;
    }

    public List<EstadoEntidadDTO> getGeneradorasSnapshot() {
        return generadorasSnapshot;
    }

    public List<EstadoEntidadDTO> getNavesSnapshot() {
        return navesSnapshot;
    }
}