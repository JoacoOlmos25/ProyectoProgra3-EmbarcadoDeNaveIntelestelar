package src.concurrencia.modelo;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Productor concurrente que fabrica baterías y las deposita en la {@link PlataformaDeCarga}.
 *
 * <p><b>Invariante de clase:</b>
 * {@code nombre != null && !nombre.isBlank() && plataforma != null && estado != null}.</p>
 */
public class GeneradoraDeEnergia implements Runnable {
    private final String nombre;
    private final PlataformaDeCarga plataforma;
    private volatile EstadoEntidad estado;
    private volatile boolean activa;

    /**
     * Crea una generadora asociada a la plataforma de carga compartida.
     * <p><b>Precondiciones:</b> {@code nombre != null && !nombre.isBlank()} y {@code plataforma != null}.</p>
     * <p><b>Postcondiciones:</b> inicia activa en estado {@link EstadoEntidad#TRABAJANDO}.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     */
    public GeneradoraDeEnergia(String nombre, PlataformaDeCarga plataforma) {
        assert nombre != null && !nombre.isBlank() : "El nombre de la generadora no puede ser nulo ni vacío";
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
                // 1. Produce una batería fuera de la plataforma (Color Azul/Gris)
                this.estado = EstadoEntidad.TRABAJANDO;
                Thread.sleep(ThreadLocalRandom.current().nextInt(1500, 3000));
                Bateria nuevaBateria = new Bateria();
                plataforma.registrarEvento("Batería producida por [" + this.nombre + "].");

                // 2. Solicita acceso a la PlataformaDeCarga (Color Amarillo - Línea de intento)
                this.estado = EstadoEntidad.SOLICITANDO;
                plataforma.registrarEvento("[" + this.nombre + "] solicitando acceso para depositar.");
                Thread.sleep(300);

                plataforma.depositar(this, nuevaBateria);
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
     * Actualiza el estado visual/operativo de la generadora.
     * <p><b>Precondiciones:</b> {@code estado != null}.</p>
     * <p><b>Postcondiciones:</b> {@code getEstado() == estado}.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     */
    public void setEstado(EstadoEntidad estado) {
        assert estado != null : "El estado de la generadora no puede ser nulo";
        this.estado = estado;
    }
}