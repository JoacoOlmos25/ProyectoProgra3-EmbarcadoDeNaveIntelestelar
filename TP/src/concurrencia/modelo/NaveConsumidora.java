package src.concurrencia.modelo;
import java.util.concurrent.ThreadLocalRandom;

public class NaveConsumidora implements Runnable {
    private final String nombre;
    private final PlataformaDeCarga plataforma;
    private volatile EstadoEntidad estado;
    private volatile boolean activa;

    public NaveConsumidora(String nombre, PlataformaDeCarga plataforma) {
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
                // Breve pausa antes del synchronized para hacer visible el intento en la GUI
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

    public void setEstado(EstadoEntidad estado) {
        this.estado = estado;
    }
}