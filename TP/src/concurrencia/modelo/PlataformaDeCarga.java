package src.concurrencia.modelo;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class PlataformaDeCarga extends Observable {
    private final List<Bateria> baterias;
    private final int capacidadMaxima;

    // Listas para llevar el registro de entidades activas
    private final List<GeneradoraDeEnergia> generadoras;
    private final List<NaveConsumidora> naves;
    private final List<Thread> hilosActivos;

    public PlataformaDeCarga(int capacidadMaxima) {
        if (capacidadMaxima <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser mayor a 0.");
        }
        this.capacidadMaxima = capacidadMaxima;
        this.baterias = new ArrayList<>();
        this.generadoras = new ArrayList<>();
        this.naves = new ArrayList<>();
        this.hilosActivos = new ArrayList<>();
    }

    public synchronized void depositar(GeneradoraDeEnergia generadora, Bateria bateria) throws InterruptedException {
        while (this.baterias.size() >= this.capacidadMaxima) {
            generadora.setEstado(EstadoEntidad.ESPERANDO); // Pasa a ROJO
            notificarCambio("[" + generadora.getNombre() + "] esperando a que se libere espacio en la Plataforma.");
            wait();
        }

        generadora.setEstado(EstadoEntidad.OPERANDO);
        this.baterias.add(bateria);
        notificarCambio("[" + generadora.getNombre() + "] depositando " + bateria + " en la Plataforma.");

        // Pausa para que en el visor se observe la conexión VERDE exclusiva
        Thread.sleep(600);
        generadora.setEstado(EstadoEntidad.TRABAJANDO);
        notificarCambio("[" + generadora.getNombre() + "] completó el depósito. Baterías: " + this.baterias.size() + "/" + this.capacidadMaxima);
        notifyAll();
    }

    public synchronized Bateria retirar(NaveConsumidora nave) throws InterruptedException {
        while (this.baterias.isEmpty()) {
            nave.setEstado(EstadoEntidad.ESPERANDO); // Pasa a ROJO
            notificarCambio("[" + nave.getNombre() + "] esperando por falta de baterías en la Plataforma.");
            wait();
        }
        //(Pasa a VERDE)
        nave.setEstado(EstadoEntidad.OPERANDO);
        Bateria retirada = this.baterias.remove(0);
        notificarCambio("Batería retirada y utilizada por [" + nave.getNombre() + "].");
        // Pausa para visualizar la conexión VERDE exclusiva
        Thread.sleep(600);
        nave.setEstado(EstadoEntidad.TRABAJANDO);
        notificarCambio("[" + nave.getNombre() + "] se desconectó de la Plataforma. Baterías: " + this.baterias.size() + "/" + this.capacidadMaxima);

        notifyAll();
        return retirada;
    }

    public synchronized void crearGeneradora() {
        String nombre = "Generadora " + (this.generadoras.size() + 1);
        GeneradoraDeEnergia nuevaGen = new GeneradoraDeEnergia(nombre, this);
        this.generadoras.add(nuevaGen);
        Thread hilo = new Thread(nuevaGen, "Hilo-" + nombre);
        this.hilosActivos.add(hilo);
        notificarCambio("¡Ingresó una nueva [" + nombre + "] al universo!");
        hilo.start();
    }

    public synchronized void crearNaveConsumidora() {
        String nombre = "Nave " + (this.naves.size() + 1);
        NaveConsumidora nuevaNave = new NaveConsumidora(nombre, this);
        this.naves.add(nuevaNave);
        Thread hilo = new Thread(nuevaNave, "Hilo-" + nombre);
        this.hilosActivos.add(hilo);
        notificarCambio("¡Ingresó una nueva [" + nombre + "] al universo!");
        hilo.start();
    }

    // observable
    public synchronized void registrarEvento(String mensaje) {
        notificarCambio(mensaje);
    }
    private void notificarCambio(String mensajeLog) {
        List<EstadoEntidadDTO> snapGen = new ArrayList<>();
        for (GeneradoraDeEnergia g : this.generadoras) {
            snapGen.add(new EstadoEntidadDTO(g.getNombre(), true, g.getEstado()));
        }

        List<EstadoEntidadDTO> snapNaves = new ArrayList<>();
        for (NaveConsumidora n : this.naves) {
            snapNaves.add(new EstadoEntidadDTO(n.getNombre(), false, n.getEstado()));
        }

        EventoVisorDTO evento = new EventoVisorDTO(
                mensajeLog,
                this.baterias.size(),
                this.capacidadMaxima,
                snapGen,
                snapNaves
        );

        setChanged();
        notifyObservers(evento);
    }

    public synchronized void detenerSimulacion() {
        for (GeneradoraDeEnergia g : this.generadoras) {
            g.detener();
        }
        for (NaveConsumidora n : this.naves) {
            n.detener();
        }
        for (Thread t : this.hilosActivos) {
            t.interrupt();
        }
    }
}