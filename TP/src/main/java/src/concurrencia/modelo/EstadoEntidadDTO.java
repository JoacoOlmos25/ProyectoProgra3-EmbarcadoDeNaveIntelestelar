package src.concurrencia.modelo;
import java.io.Serializable;

public class EstadoEntidadDTO implements Serializable {
    private final String nombre;
    private final boolean esGeneradora; // true = Generadora (izquierda), false = Nave (derecha)
    private final EstadoEntidad estado;

    public EstadoEntidadDTO(String nombre, boolean esGeneradora, EstadoEntidad estado) {
        this.nombre = nombre;
        this.esGeneradora = esGeneradora;
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isEsGeneradora() {
        return esGeneradora;
    }

    public EstadoEntidad getEstado() {
        return estado;
    }
}