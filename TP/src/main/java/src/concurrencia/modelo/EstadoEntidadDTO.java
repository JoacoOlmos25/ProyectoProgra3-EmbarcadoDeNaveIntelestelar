package src.concurrencia.modelo;

import java.io.Serializable;

/**
 * Objeto de transferencia inmutable (DTO) que representa el estado instantáneo de una entidad
 * (generadora o nave consumidora) para su renderizado desacoplado en la vista del visor.
 *
 * <p><b>Invariante de clase:</b>
 * {@code nombre != null && !nombre.isBlank() && estado != null}.</p>
 */
public class EstadoEntidadDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String nombre;
    private final boolean esGeneradora; // true = Generadora (izquierda), false = Nave (derecha)
    private final EstadoEntidad estado;

    /**
     * Crea una instantánea inmutable del estado de una entidad concurrente.
     * <p><b>Precondiciones:</b> {@code nombre != null && !nombre.isBlank()} y {@code estado != null}.</p>
     * <p><b>Postcondiciones:</b> el DTO queda inicializado con los datos planos de la entidad.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     */
    public EstadoEntidadDTO(String nombre, boolean esGeneradora, EstadoEntidad estado) {
        assert nombre != null && !nombre.isBlank() : "El nombre de la entidad no puede ser nulo ni vacío";
        assert estado != null : "El estado de la entidad no puede ser nulo";

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