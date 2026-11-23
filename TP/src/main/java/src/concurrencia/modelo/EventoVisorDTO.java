package src.concurrencia.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Objeto de transferencia inmutable (DTO) que encapsula una instantánea del estado
 * de la {@link PlataformaDeCarga} y sus entidades concurrentes para notificar a la vista vía Observer.
 *
 * <p><b>Invariante de clase:</b>
 * {@code mensajeLog != null && capacidadMaxima > 0 && 0 <= bateriasActuales <= capacidadMaxima
 * && generadorasSnapshot != null && navesSnapshot != null
 * && cantidadGeneradoras == generadorasSnapshot.size() && cantidadNaves == navesSnapshot.size()}.</p>
 */
public class EventoVisorDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String mensajeLog;
    private final int bateriasActuales;
    private final int capacidadMaxima;
    private final int cantidadGeneradoras;
    private final int cantidadNaves;
    private final List<EstadoEntidadDTO> generadorasSnapshot;
    private final List<EstadoEntidadDTO> navesSnapshot;

    /**
     * Construye una instantánea inmutable de los contadores y entidades del universo paralelo.
     * <p><b>Precondiciones:</b> {@code mensajeLog != null}, {@code capacidadMaxima > 0},
     * {@code 0 <= bateriasActuales <= capacidadMaxima}, {@code generadoras != null} y {@code naves != null}.</p>
     * <p><b>Postcondiciones:</b> almacena copias inmodificables de ambas listas y conserva el invariante de clase.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     */
    public EventoVisorDTO(String mensajeLog, int bateriasActuales, int capacidadMaxima,
                          List<EstadoEntidadDTO> generadoras, List<EstadoEntidadDTO> naves) {
        assert mensajeLog != null : "El mensaje de log no puede ser nulo";
        assert capacidadMaxima > 0 : "La capacidad máxima debe ser mayor a 0";
        assert bateriasActuales >= 0 && bateriasActuales <= capacidadMaxima
                : "La cantidad de baterías actuales está fuera de rango";
        assert generadoras != null : "La lista de generadoras no puede ser nula";
        assert naves != null : "La lista de naves no puede ser nula";

        this.mensajeLog = mensajeLog;
        this.bateriasActuales = bateriasActuales;
        this.capacidadMaxima = capacidadMaxima;

        this.generadorasSnapshot = Collections.unmodifiableList(new ArrayList<>(generadoras));
        this.navesSnapshot = Collections.unmodifiableList(new ArrayList<>(naves));

        this.cantidadGeneradoras = this.generadorasSnapshot.size();
        this.cantidadNaves = this.navesSnapshot.size();

        assert invariante() : "Invariante de EventoVisorDTO violado tras construcción";
    }

    private boolean invariante() {
        return mensajeLog != null
                && capacidadMaxima > 0
                && bateriasActuales >= 0 && bateriasActuales <= capacidadMaxima
                && generadorasSnapshot != null && navesSnapshot != null
                && cantidadGeneradoras == generadorasSnapshot.size()
                && cantidadNaves == navesSnapshot.size();
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