package src.Misiones;

import src.AC.Asistente;
import src.AC.Evento;
import src.ElementosDelUniverso.ColisionException;
import src.ElementosDelUniverso.ElementoDelUniverso;
import src.MotorWarp.MotorException;
import src.destinoytablero.Destino;
import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;
import src.nave.MantenimientoException;
import src.nave.RecursosInsuficientesException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase base abstracta del patrón Template Method para el ciclo de vida de las misiones.
 * Define el esqueleto fijo (preparar -> ejecutar -> evaluarResultado -> cerrar) y delega
 * en las subclases la definición opcional del objetivo, el nombre y la evaluación final.
 *
 * Invariante de clase:
 * - combustibleBase >= 0 && energiaBase >= 0
 * - desgasteBase >= 0 && desgasteBase < 100
 * - ac != null && destino != null && tablero != null
 * - turnos >= 0 && encuentros >= 0 && saltosNormales >= 0 && saltosWarp >= 0
 */
public abstract class Mision {
    /** Pausa entre medio turno y medio turno, para que el movimiento sea visible en el proyector. */
    private static final int PAUSA_TURNO_MS = 600;
    private ObservadorMision observador;

    protected int combustibleBase;
    protected int energiaBase;
    protected int desgasteBase;
    protected Asistente ac;
    protected Destino destino;
    protected Tablero tablero;
    protected Posicion objetivo;
    protected ArrayList<Posicion> ruta;

    private List<Posicion> rutaPlanificada = Collections.emptyList();
    private final StringBuilder detalleInforme = new StringBuilder();
    private String informeFinal = "";
    private String motivoFallo = "";
    private boolean exitosa = false;
    private int turnos = 0;
    private int encuentros = 0;
    private ElementoDelUniverso ultimoElementoInteractuado;

    private int saltosNormales = 0;
    private int saltosWarp = 0;

    /**
     * Crea una misión con valores determinados.
     * @param combustible cantidad de combustible que consumirá la misión al despegar; combustible >= 0
     * @param energia cantidad de energía que consumirá la misión al despegar; energía >= 0
     * @param desgaste desgaste que genera el despegue; desgaste >= 0, desgaste < 100
     * @param ac referencia al asistente de comandos que se utilizará para ejecutar la misión, NotNull
     * @param destino coordinador de los turnos del universo, NotNull
     * @param tablero tablero sobre el que se desarrolla la misión, NotNull
     *
     * Precondiciones: combustible >= 0, energia >= 0, 0 <= desgaste < 100, ac != null, destino != null, tablero != null
     * Postcondiciones: la misión queda creada, sin objetivo ni ruta calculados, cumpliendo el invariante de clase
     */
    public Mision(int combustible, int energia, int desgaste, Asistente ac, Destino destino, Tablero tablero) {
        assert combustible >= 0 && energia >= 0 : "Los recursos de la misión no pueden ser negativos";
        assert desgaste >= 0 && desgaste < 100 : "El desgaste de la misión debe estar entre 0 y 99";
        assert ac != null && destino != null && tablero != null : "El asistente, el destino y el tablero no pueden ser nulos";

        this.combustibleBase = combustible;
        this.energiaBase = energia;
        this.desgasteBase = desgaste;
        this.ac = ac;
        this.destino = destino;
        this.tablero = tablero;

        assert invariante() : "Invariante de Mision violado tras construcción";
    }

    private boolean invariante() {
        return combustibleBase >= 0
                && energiaBase >= 0
                && desgasteBase >= 0 && desgasteBase < 100
                && ac != null && destino != null && tablero != null
                && turnos >= 0 && encuentros >= 0
                && saltosNormales >= 0 && saltosWarp >= 0;
    }

    /**
     * Registra quién recibe los avisos de ruta y encuentros de la misión.
     * Precondiciones: debe invocarse antes de realizarMision()
     * Postcondiciones: los avisos de la misión se envían al observador indicado (null = sin avisos)
     * @param observador receptor de los avisos, puede ser null
     */
    public void setObservador(ObservadorMision observador) {
        this.observador = observador;
    }

    @Override
    public String toString() {
        return "Mision{Combustible necesario=" + combustibleBase + ", Energia necesaria=" + energiaBase + ", Desgaste=" + desgasteBase + '}';
    }

    /**
     * Método template con los pasos necesarios para realizar una misión: preparar -> ejecutar -> evaluar -> cerrar.
     * Precondiciones: debe invocarse desde un hilo secundario, ya que el ciclo de travesía incluye pausas (Thread.sleep)
     * Postcondiciones: la misión se realiza con éxito o se interrumpe por una MisionException; en ambos casos
     *                  queda registrada en la bitácora, se arma el informe final y el método termina de forma controlada
     */
    public final void realizarMision() {
        try {
            this.preparar();
            ac.registrarEvento("Misión preparada", getNombre() + ": recursos suficientes, mantenimiento correcto y motor disponible");
            this.ejecutar();
            this.evaluarResultado();
            this.exitosa = true;
        } catch (MisionException e) {
            this.exitosa = false;
            this.motivoFallo = e.getMessage();
            ac.registrarEvento("Misión fallida", getNombre() + ": " + e.getMessage());
        }
        this.cerrar();
        assert !this.informeFinal.isBlank() : "Postcondición violada: el informe final no fue generado";
        assert invariante() : "Invariante de Mision violado al finalizar realizarMision";
    }

    /**
     * Prepara la misión, verificando recursos, definiendo el objetivo y generando la ruta.
     * Precondiciones: el tablero ya debe estar creado
     * Postcondiciones: el atributo "objetivo" adquiere la posición definida por la misión, "ruta" se llena con las
     *                  posiciones por las cuales se debe mover la nave y "rutaPlanificada" guarda una copia inmodificable
     * @throws PrepMisionException si no se pudo preparar la misión
     */
    protected void preparar() throws PrepMisionException {
        if (ac.getCombustibleNave() < combustibleBase || ac.getEnergiaNave() < energiaBase) {
            throw new PrepMisionException("Recursos insuficientes");
        }
        if (ac.consultaReqMantenimiento()) {
            throw new PrepMisionException("La nave necesita mantenimiento");
        }
        if (!ac.consultaMotorDisp()) {
            throw new PrepMisionException("El motor no está disponible");
        }

        this.objetivo = this.definirObjetivo();
        assert this.objetivo != null : "El objetivo definido no puede ser nulo";

        if (this.objetivo.equals(tablero.getPosicionNave())) {
            throw new PrepMisionException("La nave ya se encuentra en el objetivo");
        }

        this.ruta = calcularRuta(this.tablero.getPosicionNave(), this.objetivo);
        assert this.ruta != null && !this.ruta.isEmpty() : "Postcondición violada: la ruta calculada no puede estar vacía";

        this.rutaPlanificada = Collections.unmodifiableList(new ArrayList<>(this.ruta));
        if (observador != null) {
            observador.rutaPlanificada(this.rutaPlanificada);
        }
    }

    /**
     * Hook para definir el objetivo de la misión. Por defecto, el tablero elige uno al azar.
     * Las misiones con destino conocido (ej. retorno a la base) sobrescriben este método.
     * Precondiciones: el tablero ya debe estar creado
     * Postcondiciones: el objetivo devuelto queda establecido en el tablero
     * @return posición del objetivo, NotNull
     */
    protected Posicion definirObjetivo() {
        return tablero.obtenerObjetivo();
    }

    /**
     * Calcula la ruta en forma de L (primero movimientos en X; luego movimientos en Y) entre partida y objetivo.
     *
     * @param puntoDePartida punto de partida válido dentro del Tablero, NotNull
     * @param objetivo punto de llegada válido dentro del Tablero, NotNull y distinto de puntoDePartida
     * @return ruta calculada como lista de Posiciones (sin incluir el punto de partida)
     *
     * Precondiciones: puntoDePartida != null, objetivo != null, !puntoDePartida.equals(objetivo)
     * Postcondiciones: la ruta resultante no está vacía y su última posición coincide con objetivo
     */
    private ArrayList<Posicion> calcularRuta(Posicion puntoDePartida, Posicion objetivo) {
        assert puntoDePartida != null && objetivo != null : "El punto de partida y el objetivo no pueden ser nulos";
        assert !puntoDePartida.equals(objetivo) : "El punto de partida no puede coincidir con el objetivo";

        ArrayList<Posicion> rutaCalculada = new ArrayList<>();
        int sentidoX = Integer.signum(objetivo.getX() - puntoDePartida.getX());
        int sentidoY = Integer.signum(objetivo.getY() - puntoDePartida.getY());

        int x = puntoDePartida.getX();
        int y = puntoDePartida.getY();

        while (x != objetivo.getX()) {
            x += sentidoX;
            rutaCalculada.add(new Posicion(x, y));
        }
        while (y != objetivo.getY()) {
            y += sentidoY;
            rutaCalculada.add(new Posicion(x, y));
        }

        assert !rutaCalculada.isEmpty() && rutaCalculada.get(rutaCalculada.size() - 1).equals(objetivo)
                : "La ruta calculada debe terminar en el objetivo";
        return rutaCalculada;
    }

    /**
     * Ejecuta la misión: aplica el costo de despegue y realiza el ciclo de turnos hasta llegar al objetivo.
     * Precondiciones: la misión fue preparada (objetivo != null y ruta != null && !ruta.isEmpty())
     * Postcondiciones: la nave se encuentra en el objetivo
     * @throws MisionException si la nave no puede despegar o la travesía se interrumpe
     */
    protected final void ejecutar() throws MisionException {
        assert this.objetivo != null && this.ruta != null && !this.ruta.isEmpty()
                : "Precondición violada: la misión debe estar preparada antes de ejecutar";

        try {
            ac.despegue(this.combustibleBase, this.energiaBase, this.desgasteBase);
        } catch (RecursosInsuficientesException | MantenimientoException e) {
            throw new MisionException("No se pudo despegar: " + e.getMessage());
        }
        ac.registrarEvento("Despegue", getNombre() + " en curso hacia (" + objetivo.getX() + ", " + objetivo.getY() + ")");
        this.cicloDeTravesia();
        assert llegoAlObjetivo() : "Postcondición violada: ejecutar() terminó sin alcanzar el objetivo";
    }

    /**
     * Alterna turnos de la nave y del universo hasta que la nave llega al objetivo.
     * Precondiciones: ruta != null && !ruta.isEmpty()
     * Postcondiciones: la nave se encuentra en el objetivo
     * @throws MisionException si la nave no sobrevive, se agotan los turnos o la ejecución es interrumpida
     */
    private void cicloDeTravesia() throws MisionException {
        assert this.ruta != null && !this.ruta.isEmpty() : "La ruta no puede estar vacía al iniciar la travesía";
        int maxTurnos = tablero.getX() * tablero.getY();

        while (!llegoAlObjetivo()) {
            if (turnos >= maxTurnos) {
                throw new MisionException("Se agotaron los turnos sin alcanzar el objetivo");
            }
            turnos++;

            this.turnoDeLaNave();
            this.verificarCondicionDeVida();
            this.esperarTurno();

            if (!llegoAlObjetivo()) {
                this.turnoDelUniverso();
                this.esperarTurno();
            }
        }
    }

    /**
     * Turno de la nave: inspecciona su casilla, se mueve (salto normal o warp según el radar) e inspecciona la nueva casilla.
     * Precondiciones: ruta != null && !ruta.isEmpty()
     * Postcondiciones: la nave avanzó 0, 1 o 2 casilleros e interactuó con los elementos encontrados
     * @throws MisionException si una colisión, el motor o los recursos impiden continuar
     */
    private void turnoDeLaNave() throws MisionException {
        assert this.ruta != null && !this.ruta.isEmpty() : "La ruta no puede estar vacía durante el turno de la nave";

        try {
            this.inspeccionarPosicion();

            Posicion antes = tablero.getPosicionNave();
            ac.movimiento(this.tablero, this.ruta);
            Posicion despues = tablero.getPosicionNave();

            if (!despues.equals(antes)) {
                int distancia = Math.abs(despues.getX() - antes.getX()) + Math.abs(despues.getY() - antes.getY());
                if (distancia >= 2) {
                    saltosWarp++;
                } else {
                    saltosNormales++;
                }
                this.inspeccionarPosicion();
            }
        } catch (ColisionException e) {
            throw new MisionException(e.getMessage());
        } catch (MotorException e) {
            throw new MisionException("Falla del motor: " + e.getMessage());
        } catch (RecursosInsuficientesException | MantenimientoException e) {
            throw new MisionException(e.getMessage());
        }
    }

    /**
     * Interactúa con el elemento de la casilla actual, una única vez por encuentro.
     * Postcondiciones: si hay un elemento distinto al último con el que se interactuó, se registra el encuentro,
     *                  se aplica la interacción y se notifica al observador con el resultado efectivo (incluso ante fallo).
     * @throws ColisionException si la interacción deja a la nave en condición crítica o derrotada
     */
    private void inspeccionarPosicion() throws ColisionException {
        ElementoDelUniverso elemento = tablero.inspeccionarPosicion();

        if (elemento != null && elemento != ultimoElementoInteractuado) {
            encuentros++;
            Posicion pos = tablero.getPosicionNave();
            String nombreElemento = elemento.getClass().getSimpleName();
            String ubicacion = "(" + pos.getX() + ", " + pos.getY() + ")";

            ac.registrarEvento("Encuentro", "La nave encontró [" + nombreElemento + "] en " + ubicacion);
            int indiceTrasEncuentro = ac.getEventosBitacora().size();

            try {
                ac.inspeccionarPosicionActual(this.tablero);
                String resultado = extraerResultadoInteraccion(indiceTrasEncuentro);
                String detalle = "[" + nombreElemento + "] en " + ubicacion
                        + (resultado.isEmpty() ? "" : " — " + resultado);

                if (observador != null) {
                    observador.interaccionDetectada("ENCUENTRO: " + nombreElemento.toUpperCase(), detalle);
                }
            } catch (ColisionException e) {
                String detalle = "[" + nombreElemento + "] en " + ubicacion + " — " + e.getMessage();
                if (observador != null) {
                    observador.interaccionDetectada("COLISIÓN CRÍTICA: " + nombreElemento.toUpperCase(), detalle);
                }
                throw e;
            }
        }
        ultimoElementoInteractuado = elemento;
    }

    /**
     * Recupera el mensaje del evento que el subsistema acaba de registrar tras la interacción.
     * Precondiciones: indiceInicial >= 0
     * Postcondiciones: no modifica el estado de la bitácora.
     */
    private String extraerResultadoInteraccion(int indiceInicial) {
        assert indiceInicial >= 0 : "El índice inicial de bitácora no puede ser negativo";
        List<Evento> eventos = ac.getEventosBitacora();
        if (eventos.size() > indiceInicial) {
            return eventos.get(eventos.size() - 1).getMsj();
        }
        return "";
    }

    /**
     * Verifica que la nave pueda seguir operando.
     * Postcondiciones: no modifica el estado
     * @throws MisionException si desgaste >= 100, energía <= 0 o combustible <= 0
     */
    private void verificarCondicionDeVida() throws MisionException {
        if (ac.getDesgasteNave() >= 100) {
            throw new MisionException("La nave alcanzó el desgaste máximo");
        }
        if (ac.getEnergiaNave() <= 0) {
            throw new MisionException("La nave se quedó sin energía");
        }
        if (ac.getCombustibleNave() <= 0) {
            throw new MisionException("La nave se quedó sin combustible");
        }
    }

    private void turnoDelUniverso() {
        destino.turnoDestino();
    }

    private void esperarTurno() throws MisionException {
        try {
            Thread.sleep(PAUSA_TURNO_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new MisionException("La misión fue interrumpida");
        }
    }

    private boolean llegoAlObjetivo() {
        return tablero.getPosicionNave().equals(this.objetivo);
    }

    /**
     * Aplica las entregas o recompensas propias de cada misión al llegar al objetivo.
     * Precondiciones: la nave se encuentra en el objetivo
     * @throws MisionException si no se puede completar la operación final
     */
    protected abstract void evaluarResultado() throws MisionException;

    /**
     * Nombre de la misión para la bitácora y el informe.
     * @return nombre de la misión, NotNull
     */
    protected abstract String getNombre();

    /**
     * Cierra la misión: devuelve el motor a disponible, registra el cierre en la bitácora y arma el informe final.
     * Postcondiciones: el motor queda disponible (salvo desgaste excedido, que queda registrado) y getInformeFinal()
     *                  devuelve el informe con el resultado, los turnos, saltos, encuentros, el detalle propio
     *                  y el estado final de la nave.
     */
    protected void cerrar() {
        try {
            ac.reiniciarMotor();
        } catch (MantenimientoException e) {
            ac.registrarEvento("Motor Warp", "No se pudo completar el enfriamiento: " + e.getMessage());
        }

        int totalSaltos = saltosNormales + saltosWarp;
        StringBuilder informe = new StringBuilder();
        informe.append("Misión: ").append(getNombre()).append("\n");
        informe.append("Resultado: ").append(exitosa ? "EXITOSA" : "FALLIDA").append("\n");
        if (!exitosa) {
            informe.append("Motivo: ").append(motivoFallo).append("\n");
        }
        if (objetivo != null) {
            informe.append("Objetivo: (").append(objetivo.getX()).append(", ").append(objetivo.getY()).append(")\n");
        }
        informe.append("Turnos: ").append(turnos)
                .append(" | Saltos: ").append(totalSaltos)
                .append(" (Normales: ").append(saltosNormales)
                .append(", Warp: ").append(saltosWarp).append(")")
                .append(" | Encuentros: ").append(encuentros).append("\n");
        informe.append(detalleInforme);
        informe.append("Estado final -> Combustible: ").append(ac.getCombustibleNave())
                .append(" | Energía: ").append(ac.getEnergiaNave())
                .append(" | Desgaste: ").append(ac.getDesgasteNave()).append("%\n");
        informe.append("Motor Warp disponible: ").append(ac.consultaMotorDisp() ? "SÍ" : "NO");

        this.informeFinal = informe.toString();
        ac.registrarEvento("Misión cerrada", getNombre() + (exitosa ? " finalizó con éxito" : " finalizó con fallas"));
    }

    /**
     * Agrega una línea al detalle propio de la misión dentro del informe.
     * Precondiciones: linea != null
     * @param linea texto a agregar, NotNull
     */
    protected void agregarAlInforme(String linea) {
        assert linea != null : "La línea del informe no puede ser nula";
        detalleInforme.append(linea).append("\n");
    }

    public List<Posicion> getRutaPlanificada() {
        return rutaPlanificada;
    }

    public String getInformeFinal() {
        return informeFinal;
    }

    public boolean isExitosa() {
        return exitosa;
    }
}