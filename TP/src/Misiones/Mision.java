package src.Misiones;

import src.AC.Asistente;
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

public abstract class Mision {
    /** Pausa entre medio turno y medio turno, para que el movimiento sea visible en el proyector. */
    private static final int PAUSA_TURNO_MS = 600;

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


    /**
     * Crea una misión con valores determinados
     * @param combustible: cantidad de combustible que consumirá la misión al despegar; combustible >= 0
     * @param energia: cantidad de energía que consumirá la misión al despegar; energía >= 0
     * @param desgaste: desgaste que genera el despegue; desgaste >= 0, desgaste < 100
     * @param ac: referencia al asistente de comandos que se utilizará para ejecutar la misión, NotNull
     * @param destino: coordinador de los turnos del universo, NotNull
     * @param tablero: tablero sobre el que se desarrolla la misión, NotNull
     *
     * Postcondiciones: la misión queda creada, sin objetivo ni ruta calculados
     */
    public Mision(int combustible, int energia, int desgaste, Asistente ac, Destino destino, Tablero tablero){
        assert combustible >= 0 && energia >= 0 : "Los recursos de la misión no pueden ser negativos";
        assert desgaste >= 0 && desgaste < 100 : "El desgaste de la misión debe estar entre 0 y 99";
        assert ac != null && destino != null && tablero != null : "El asistente, el destino y el tablero no pueden ser nulos";

        this.combustibleBase = combustible;
        this.energiaBase = energia;
        this.desgasteBase = desgaste;
        this.ac = ac;
        this.destino = destino;
        this.tablero = tablero;
    }

    @Override
    public String toString() {
        return "Mision{Combustible necesario=" + combustibleBase +", Energia necesaria=" + energiaBase +", Desgaste=" + desgasteBase + '}';
    }


    /**
     * Método template con los pasos necesarios para realizar una misión: preparar -> ejecutar -> evaluar -> cerrar
     * Precondiciones: debe invocarse desde un hilo secundario, ya que el ciclo de travesía incluye pausas (Thread.sleep)
     * Postcondiciones: la misión se realiza con éxito o se interrumpe por una MisionException; en ambos casos
     *                  queda registrada en la bitácora, se arma el informe final y el método termina de forma controlada
     */
    public final void realizarMision(){
        try {
            this.preparar();
            ac.registrarEvento("Misión preparada", getNombre() + ": recursos suficientes, mantenimiento correcto y motor disponible");
            this.ejecutar();
            this.evaluarResultado();
            this.exitosa = true;
        }
        catch (MisionException e){
            this.exitosa = false;
            this.motivoFallo = e.getMessage();
            ac.registrarEvento("Misión fallida", getNombre() + ": " + e.getMessage());
        }
        this.cerrar();
    }

    /**
     * Prepara la misión, verificando recursos, definiendo el objetivo y generando la ruta
     * Precondiciones: el tablero ya debe estar creado
     * Postcondiciones: el atributo "objetivo" adquiere la posición definida por la misión, "ruta" se llena con las
     *                  posiciones por las cuales se debe mover la nave y "rutaPlanificada" guarda una copia inmodificable
     * @throws PrepMisionException si no se pudo preparar la mision
     */
    protected void preparar() throws PrepMisionException{
        //Verificación inicial
        if (ac.getCombustibleNave() < combustibleBase || ac.getEnergiaNave() < energiaBase)
            throw new PrepMisionException("Recursos insuficientes");
        if (ac.consultaReqMantenimiento())
            throw new PrepMisionException("La nave necesita mantenimiento");
        if (!ac.consultaMotorDisp())
            throw new PrepMisionException("El motor no está disponible");

        //Objetivo y cálculo de ruta
        this.objetivo = this.definirObjetivo();
        if (this.objetivo.equals(tablero.getPosicionNave()))
            throw new PrepMisionException("La nave ya se encuentra en el objetivo");

        this.ruta = calcularRuta(this.tablero.getPosicionNave(), this.objetivo);
        this.rutaPlanificada = Collections.unmodifiableList(new ArrayList<>(this.ruta));
    }

    /**
     * Define el objetivo de la misión. Por defecto, el tablero elige uno al azar.
     * Las misiones con destino conocido (ej. retorno a la base) sobrescriben este método.
     * Precondiciones: el tablero ya debe estar creado
     * Postcondiciones: el objetivo devuelto queda establecido en el tablero
     * @return posición del objetivo, NotNull
     */
    protected Posicion definirObjetivo(){
        return tablero.obtenerObjetivo();
    }


    /**
     * Calcula la ruta en forma de L (primero, movimientos solo en X; luego, movimientos solo en Y) que une un puntoDepartida con un objetivo
     *
     * @param puntoDePartida el punto de partida de la ruta, debe ser válida dentro del Tablero, NotNull
     * @param objetivo el punto de llegada la ruta, debe ser válida dentro del Tablero, NotNull
     * @return devuelve la ruta que une la salida y la llegada ya calculada, como una lista de Posiciones (sin incluir el punto de partida)
     *
     * Precondiciones: las dimensiones del Tablero deben ser conocidas y las Posiciones deben estar dentro de las mismas
     * Postcondiciones: se devuelve la ruta conformada; si alguna de las coordenadas coincide, ese tramo de la L queda vacío
     */
    private ArrayList<Posicion> calcularRuta(Posicion puntoDePartida, Posicion objetivo){
        assert puntoDePartida != null && objetivo != null : "El punto de partida y el objetivo no pueden ser nulos";

        ArrayList<Posicion> rutaCalculada = new ArrayList<>();
        int sentidoX = Integer.signum(objetivo.getX() - puntoDePartida.getX());
        int sentidoY = Integer.signum(objetivo.getY() - puntoDePartida.getY());

        int x = puntoDePartida.getX();
        int y = puntoDePartida.getY();

        //movimientos en X
        while (x != objetivo.getX()){
            x += sentidoX;
            rutaCalculada.add(new Posicion(x, y));
        }
        //movimientos en Y desde el punto donde la ruta cambia de dirección
        while (y != objetivo.getY()){
            y += sentidoY;
            rutaCalculada.add(new Posicion(x, y));
        }

        assert rutaCalculada.get(rutaCalculada.size() - 1).equals(objetivo) : "La ruta calculada no termina en el objetivo";
        return rutaCalculada;
    }

    /**
     * Ejecuta la misión: aplica el costo de despegue y realiza el ciclo de turnos hasta llegar al objetivo
     * Precondiciones: la misión fue preparada
     * Postcondiciones: la nave se encuentra en el objetivo
     * @throws MisionException si la nave no puede despegar o la travesía se interrumpe
     */
    protected final void ejecutar() throws MisionException {
        try {
            ac.despegue(this.combustibleBase, this.energiaBase, this.desgasteBase);
        } catch (RecursosInsuficientesException | MantenimientoException e) {
            throw new MisionException("No se pudo despegar: " + e.getMessage());
        }
        ac.registrarEvento("Despegue", getNombre() + " en curso hacia (" + objetivo.getX() + ", " + objetivo.getY() + ")");
        this.cicloDeTravesia();
    }

    /**
     * Alterna turnos de la nave y del universo hasta que la nave llega al objetivo
     * Precondiciones: la ruta no está vacía
     * Postcondiciones: la nave se encuentra en el objetivo
     * @throws MisionException si la nave no sobrevive, se agotan los turnos o la ejecución es interrumpida
     */
    private void cicloDeTravesia() throws MisionException {
        int maxTurnos = tablero.getX() * tablero.getY();

        while (!llegoAlObjetivo()){
            if (turnos >= maxTurnos)
                throw new MisionException("Se agotaron los turnos sin alcanzar el objetivo");
            turnos++;

            this.turnoDeLaNave();
            this.verificarCondicionDeVida();
            this.esperarTurno();

            if (!llegoAlObjetivo()){
                this.turnoDelUniverso();
                this.esperarTurno();
            }
        }
    }

    /**
     * Turno de la nave: inspecciona su casilla, se mueve (salto normal o warp según el radar) e inspecciona la nueva casilla
     * Precondiciones: la ruta no está vacía
     * Postcondiciones: la nave avanzó 0, 1 o 2 casilleros e interactuó con los elementos encontrados
     * @throws MisionException si una colisión, el motor o los recursos impiden continuar
     */
    private void turnoDeLaNave() throws MisionException {
        try {
            //por si en el turno del universo un elemento se movió sobre la nave
            this.inspeccionarPosicion();

            Posicion antes = tablero.getPosicionNave();
            ac.movimiento(this.tablero, this.ruta);
            if (!tablero.getPosicionNave().equals(antes))
                this.inspeccionarPosicion();
        }
        catch (ColisionException e){
            throw new MisionException(e.getMessage());
        }
        catch (MotorException e){
            throw new MisionException("Falla del motor: " + e.getMessage());
        }
        catch (RecursosInsuficientesException | MantenimientoException e){
            throw new MisionException(e.getMessage());
        }
    }

    /**
     * Interactúa con el elemento de la casilla actual, una única vez por encuentro
     * Postcondiciones: si hay un elemento distinto al último con el que se interactuó, se registra el encuentro y se interactúa
     * @throws ColisionException si la interacción deja a la nave en condición crítica
     */
    private void inspeccionarPosicion() throws ColisionException {
        ElementoDelUniverso elemento = tablero.inspeccionarPosicion();

        if (elemento != null && elemento != ultimoElementoInteractuado){
            encuentros++;
            Posicion pos = tablero.getPosicionNave();
            ac.registrarEvento("Encuentro", "La nave encontró un " + elemento.getClass().getSimpleName()
                    + " en (" + pos.getX() + ", " + pos.getY() + ")");
            ac.inspeccionarPosicionActual(this.tablero);
        }
        ultimoElementoInteractuado = elemento;
    }

    /**
     * Verifica que la nave pueda seguir operando
     * Postcondiciones: no modifica el estado
     * @throws MisionException si desgaste >= 100, energía <= 0 o combustible <= 0
     */
    private void verificarCondicionDeVida() throws MisionException {
        if (ac.getDesgasteNave() >= 100)
            throw new MisionException("La nave alcanzó el desgaste máximo");
        if (ac.getEnergiaNave() <= 0)
            throw new MisionException("La nave se quedó sin energía");
        if (ac.getCombustibleNave() <= 0)
            throw new MisionException("La nave se quedó sin combustible");
    }

    /**
     * Turno del universo: el destino mueve los elementos del tablero y notifica a los observadores
     */
    private void turnoDelUniverso(){
        destino.turnoDestino();
    }

    /**
     * Pausa la ejecución para que el turno sea visible en la interfaz
     * @throws MisionException si el hilo de la misión es interrumpido
     */
    private void esperarTurno() throws MisionException {
        try {
            Thread.sleep(PAUSA_TURNO_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new MisionException("La misión fue interrumpida");
        }
    }

    private boolean llegoAlObjetivo(){
        return tablero.getPosicionNave().equals(this.objetivo);
    }

    /**
     * Aplica las entregas o recompensas propias de cada misión al llegar al objetivo
     * Precondiciones: la nave se encuentra en el objetivo
     * @throws MisionException si no se puede completar la operación final
     */
    protected abstract void evaluarResultado() throws MisionException;

    /**
     * Nombre de la misión para la bitácora y el informe
     * @return nombre de la misión, NotNull
     */
    protected abstract String getNombre();

    /**
     * Cierra la misión: devuelve el motor a disponible, registra el cierre en la bitácora y arma el informe final
     * Postcondiciones: el motor queda disponible (salvo desgaste excedido, que queda registrado) y getInformeFinal()
     *                  devuelve el informe con el resultado, los turnos, los encuentros, el detalle propio de la misión
     *                  y el estado final de la nave
     */
    protected void cerrar(){
        try {
            ac.reiniciarMotor();
        } catch (MantenimientoException e) {
            ac.registrarEvento("Motor Warp", "No se pudo completar el enfriamiento: " + e.getMessage());
        }

        StringBuilder informe = new StringBuilder();
        informe.append("Misión: ").append(getNombre()).append("\n");
        informe.append("Resultado: ").append(exitosa ? "EXITOSA" : "FALLIDA").append("\n");
        if (!exitosa)
            informe.append("Motivo: ").append(motivoFallo).append("\n");
        if (objetivo != null)
            informe.append("Objetivo: (").append(objetivo.getX()).append(", ").append(objetivo.getY()).append(")\n");
        informe.append("Turnos: ").append(turnos).append(" | Encuentros: ").append(encuentros).append("\n");
        informe.append(detalleInforme);
        informe.append("Estado final -> Combustible: ").append(ac.getCombustibleNave())
                .append(" | Energía: ").append(ac.getEnergiaNave())
                .append(" | Desgaste: ").append(ac.getDesgasteNave()).append("%\n");
        informe.append("Motor Warp disponible: ").append(ac.consultaMotorDisp() ? "SÍ" : "NO");

        this.informeFinal = informe.toString();
        ac.registrarEvento("Misión cerrada", getNombre() + (exitosa ? " finalizó con éxito" : " finalizó con fallas"));
    }

    /**
     * Agrega una línea al detalle propio de la misión dentro del informe
     * @param linea texto a agregar, NotNull
     */
    protected void agregarAlInforme(String linea){
        assert linea != null : "La línea del informe no puede ser nula";
        detalleInforme.append(linea).append("\n");
    }

    /**
     * @return la ruta calculada en la preparación, inmodificable (vacía si la misión no se preparó)
     */
    public List<Posicion> getRutaPlanificada(){
        return rutaPlanificada;
    }

    /**
     * @return el informe armado al cerrar la misión (vacío si todavía no se cerró)
     */
    public String getInformeFinal(){
        return informeFinal;
    }

    public boolean isExitosa(){
        return exitosa;
    }
}