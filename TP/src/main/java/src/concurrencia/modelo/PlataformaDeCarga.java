package src.concurrencia.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Recurso compartido (Monitor) del módulo Productor-Consumidor en el Universo Paralelo.
 * Coordina el acceso exclusivo y la sincronización entre {@link GeneradoraDeEnergia} (productores)
 * y {@link NaveConsumidora} (consumidores) mediante {@code synchronized}, {@code while},
 * {@code wait()} y {@code notifyAll()}, notificando los cambios a la vista por Observer.
 *
 * <p><b>Invariante de clase:</b>
 * {@code capacidadMaxima > 0 && baterias != null && 0 <= baterias.size() <= capacidadMaxima}.</p>
 */
@SuppressWarnings("deprecation")
public class PlataformaDeCarga extends Observable {
    private final List<Bateria> baterias;
    private final int capacidadMaxima;

    // Listas para llevar el registro de entidades activas
    private final List<GeneradoraDeEnergia> generadoras;
    private final List<NaveConsumidora> naves;
    private final List<Thread> hilosActivos;

    /**
     * Crea una plataforma de carga vacía con la capacidad máxima indicada.
     * <p><b>Precondiciones:</b> {@code capacidadMaxima > 0}.</p>
     * <p><b>Postcondiciones:</b> la plataforma inicia con 0 baterías y sin entidades activas.</p>
     * <p><b>Excepciones declaradas:</b> {@link IllegalArgumentException} si {@code capacidadMaxima <= 0}.</p>
     *
     * @param capacidadMaxima cantidad máxima de baterías almacenables simultáneamente
     */
    public PlataformaDeCarga(int capacidadMaxima) {
        if (capacidadMaxima <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser mayor a 0.");
        }
        this.capacidadMaxima = capacidadMaxima;
        this.baterias = new ArrayList<>();
        this.generadoras = new ArrayList<>();
        this.naves = new ArrayList<>();
        this.hilosActivos = new ArrayList<>();

        assert invariante() : "Invariante de PlataformaDeCarga violado tras construcción";
    }

    private boolean invariante() {
        return this.capacidadMaxima > 0
                && this.baterias != null
                && this.baterias.size() >= 0
                && this.baterias.size() <= this.capacidadMaxima;
    }

    /**
     * Deposita una batería en la plataforma bajo exclusión mutua. Si la plataforma está llena,
     * suspende el hilo productor hasta que se libere espacio.
     * <p><b>Precondiciones:</b> {@code generadora != null && bateria != null}.</p>
     * <p><b>Postcondiciones:</b> la batería queda almacenada en la plataforma ({@code baterias.size() <= capacidadMaxima})
     * y se notifica con {@code notifyAll()} a los hilos en espera.</p>
     * <p><b>Excepciones declaradas:</b> {@link InterruptedException} si el hilo es interrumpido mientras espera u opera.</p>
     *
     * @param generadora entidad productora que solicita depositar
     * @param bateria batería producida a almacenar
     */
    public synchronized void depositar(GeneradoraDeEnergia generadora, Bateria bateria) throws InterruptedException {
        assert generadora != null : "La generadora no puede ser nula";
        assert bateria != null : "La batería a depositar no puede ser nula";

        while (this.baterias.size() >= this.capacidadMaxima) {
            generadora.setEstado(EstadoEntidad.ESPERANDO); // Pasa a ROJO
            notificarCambio("[" + generadora.getNombre() + "] esperando a que se libere espacio en la Plataforma.");
            wait();
        }

        int tamAnterior = this.baterias.size();
        generadora.setEstado(EstadoEntidad.OPERANDO);
        this.baterias.add(bateria);
        assert this.baterias.size() == tamAnterior + 1 : "Postcondición violada al depositar batería";
        assert invariante() : "Invariante violado al depositar batería";

        notificarCambio("[" + generadora.getNombre() + "] depositando " + bateria + " en la Plataforma.");

        try {
            // Pausa para que en el visor se observe la conexión VERDE exclusiva
            Thread.sleep(600);
        } finally {
            generadora.setEstado(EstadoEntidad.TRABAJANDO);
            notificarCambio("[" + generadora.getNombre() + "] completó el depósito. Baterías: "
                    + this.baterias.size() + "/" + this.capacidadMaxima);
            notifyAll();
        }
    }

    /**
     * Retira una batería de la plataforma bajo exclusión mutua. Si la plataforma está vacía,
     * suspende el hilo consumidor hasta que alguna generadora deposite suministros.
     * <p><b>Precondiciones:</b> {@code nave != null}.</p>
     * <p><b>Postcondiciones:</b> devuelve una {@link Bateria} no nula, reduce en 1 el stock de la plataforma
     * y despierta a los hilos en espera con {@code notifyAll()}.</p>
     * <p><b>Excepciones declaradas:</b> {@link InterruptedException} si el hilo es interrumpido mientras espera u opera.</p>
     *
     * @param nave entidad consumidora que solicita retirar una batería
     * @return batería retirada de la plataforma
     */
    public synchronized Bateria retirar(NaveConsumidora nave) throws InterruptedException {
        assert nave != null : "La nave consumidora no puede ser nula";

        while (this.baterias.isEmpty()) {
            nave.setEstado(EstadoEntidad.ESPERANDO); // Pasa a ROJO
            notificarCambio("[" + nave.getNombre() + "] esperando por falta de baterías en la Plataforma.");
            wait();
        }

        int tamAnterior = this.baterias.size();
        nave.setEstado(EstadoEntidad.OPERANDO); // Pasa a VERDE
        Bateria retirada = this.baterias.remove(0);
        assert retirada != null && this.baterias.size() == tamAnterior - 1 : "Postcondición violada al retirar batería";
        assert invariante() : "Invariante violado al retirar batería";

        notificarCambio("Batería retirada y utilizada por [" + nave.getNombre() + "].");

        try {
            // Pausa para visualizar la conexión VERDE exclusiva
            Thread.sleep(600);
        } finally {
            nave.setEstado(EstadoEntidad.TRABAJANDO);
            notificarCambio("[" + nave.getNombre() + "] se desconectó de la Plataforma. Baterías: "
                    + this.baterias.size() + "/" + this.capacidadMaxima);
            notifyAll();
        }
        return retirada;
    }

    /**
     * Incorpora dinámicamente una nueva generadora de energía e inicia su hilo de ejecución.
     * <p><b>Precondiciones:</b> ninguna.</p>
     * <p><b>Postcondiciones:</b> la nueva generadora queda registrada, se notifica a los observadores y su hilo inicia.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     */
    public synchronized void crearGeneradora() {
        String nombre = "Generadora " + (this.generadoras.size() + 1);
        GeneradoraDeEnergia nuevaGen = new GeneradoraDeEnergia(nombre, this);
        this.generadoras.add(nuevaGen);
        Thread hilo = new Thread(nuevaGen, "Hilo-" + nombre);
        hilo.setDaemon(true);
        this.hilosActivos.add(hilo);
        notificarCambio("¡Ingresó una nueva [" + nombre + "] al universo!");
        hilo.start();
    }

    /**
     * Incorpora dinámicamente una nueva nave consumidora e inicia su hilo de ejecución.
     * <p><b>Precondiciones:</b> ninguna.</p>
     * <p><b>Postcondiciones:</b> la nueva nave queda registrada, se notifica a los observadores y su hilo inicia.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     */
    public synchronized void crearNaveConsumidora() {
        String nombre = "Nave " + (this.naves.size() + 1);
        NaveConsumidora nuevaNave = new NaveConsumidora(nombre, this);
        this.naves.add(nuevaNave);
        Thread hilo = new Thread(nuevaNave, "Hilo-" + nombre);
        hilo.setDaemon(true);
        this.hilosActivos.add(hilo);
        notificarCambio("¡Ingresó una nueva [" + nombre + "] al universo!");
        hilo.start();
    }

    /**
     * Registra y difunde a los observadores un evento textual con una instantánea inmutable del estado actual.
     * <p><b>Precondiciones:</b> {@code mensaje != null && !mensaje.isBlank()}.</p>
     * <p><b>Postcondiciones:</b> todos los observadores registrados reciben un {@link EventoVisorDTO} actualizado.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     *
     * @param mensaje texto descriptivo del evento
     */
    public synchronized void registrarEvento(String mensaje) {
        assert mensaje != null && !mensaje.isBlank() : "El mensaje de evento no puede ser nulo ni vacío";
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

    /**
     * Detiene de forma controlada todas las generadoras, naves consumidoras e hilos activos de la simulación.
     * <p><b>Precondiciones:</b> ninguna.</p>
     * <p><b>Postcondiciones:</b> todas las entidades quedan marcadas como inactivas y sus hilos interrumpidos.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     */
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