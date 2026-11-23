package src.concurrencia.modelo;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Consumidor concurrente que solicita y retira baterías desde la {@link PlataformaDeCarga}.
 *
 * <p><b>Invariante de clase:</b>
 * {@code nombre != null && !nombre.isBlank() && plataforma != null && estado != null}.</p>
 */
public class NaveConsumidora implements Runnable {
    private final String nombre;
    private final PlataformaDeCarga plataforma;
    private volatile EstadoEntidad estado;
    private volatile boolean activa;

    /**
     * Crea una nave consumidora asociada a la plataforma de carga compartida.
     * <p><b>Precondiciones:</b> {@code nombre != null && !nombre.isBlank()} y {@code plataforma != null}.</p>
     * <p><b>Postcondiciones:</b> inicia activa en estado {@link EstadoEntidad#TRABAJANDO}.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     */
    public NaveConsumidora(String nombre, PlataformaDeCarga plataforma) {
        assert nombre != null && !nombre.isBlank() : "El nombre de la nave consumidora no puede ser nulo ni vacío";
        assert plataforma != null : "La plataforma de carga no puede ser nula";

        this.nombre = nombre;
        this.plataforma = plataforma;
        this.estado = EstadoEntidad.TRABAJANDO;
        this.activa = true;
    }

    @Override
    public void run() {
        try {
            while (this.activa) {
                // Navegando / consumiendo su energía actual fuera de la plataforma (Color Azul/Gris)
                this.estado = EstadoEntidad.TRABAJANDO;
                Thread.sleep(ThreadLocalRandom.current().nextInt(2000, 3500));

                // 1. Solicita una batería (Color Amarillo - Línea de intento)
                this.estado = EstadoEntidad.SOLICITANDO;
                plataforma.registrarEvento("[" + this.nombre + "] solicitando batería.");
                Thread.sleep(300);

                plataforma.retirar(this);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void detener() {
        this.activa = false;
    }

    public String getNombre() {
        return nombre;
    }

    public EstadoEntidad getEstado() {
        return estado;
    }

    /**
     * Actualiza el estado visual/operativo de la nave consumidora.
     * <p><b>Precondiciones:</b> {@code estado != null}.</p>
     * <p><b>Postcondiciones:</b> {@code getEstado() == estado}.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     */
    public void setEstado(EstadoEntidad estado) {
        assert estado != null : "El estado de la nave consumidora no puede ser nulo";
        this.estado = estado;
    }
}