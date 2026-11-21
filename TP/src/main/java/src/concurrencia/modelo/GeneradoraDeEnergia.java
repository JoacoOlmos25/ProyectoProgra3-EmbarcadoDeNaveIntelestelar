package src.concurrencia.modelo;
import java.util.concurrent.ThreadLocalRandom;


public class GeneradoraDeEnergia implements Runnable {
    private final String nombre;
    private final PlataformaDeCarga plataforma;
    private volatile EstadoEntidad estado;
    private volatile boolean activa;

    public GeneradoraDeEnergia(String nombre, PlataformaDeCarga plataforma) {
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
                // Breve pausa antes del synchronized para que en la GUI se aprecie el estado Amarillo
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

    public void setEstado(EstadoEntidad estado) {
        this.estado = estado;
    }
}
