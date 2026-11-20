package src.Misiones;

import src.AC.Asistente;
import src.destinoytablero.Destino;
import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;

import java.util.ArrayList;

public abstract class Mision {
    protected int combustibleBase;
    protected int energiaBase;
    protected int desgasteBase;
    protected Asistente ac;
    protected Destino destino;
    protected Tablero tablero;
    protected Posicion objetivo;
    protected ArrayList<Posicion> ruta;


    /**
     * Crea una misión con valores determinados
     * @param combustible: cantidad de combustible que consumirá la misión; combustible > 0, combustible < capacidad maxima de combustible
     * @param energia: cantidad de energía que consumirá la misión; energía > 0, energía < capacidad maxima de energia
     * @param desgaste: desgaste esperado en la mision; desgaste > 0, desgaste < 100
     * @param ac: referencia al asistente de comandos que se utilizará para ejecutar la misión
     *
     * Postcondiciones: la misión queda creada
     */
    public Mision(int combustible, int energia, int desgaste, Asistente ac, Destino destino, Tablero tablero){
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
     * Método template con los pasos necesarios para realizar una misión
     * Postcondiciones: la misión se realiza con éxito o se interrumpe por una excepción; ambos casos quegan registrados en la bitácora y el método termina de forma controlada
     */
    public final void realizarMision(){
        try {
            this.preparar();
            ac.registrarEvento( "Misión preparada", "Recursos suficientes, mantenimiento correcto y motor disponible");
            this.ejecutar();
            this.evaluarResultado();
            this.cerrar();
            this.informe();
            //se deberían registrar los pasos en la bitácora
        }
        catch (MisionException e){
            ac.registrarEvento("Misión fallida", e.getMessage());
        }
    }

    /**
     * Prepara la misión, verificando recursos, posicionando el objetivo y generando la ruta
     * Precondiciones: el tablero ya debe estar creado
     * Postcondiciones: el atributo "objetivo" adquiere una referencia a la posición del objetivo generada por el tablero, y "ruta" se llena con las posiciones por las cuales
     *                  se debe mover la nave
     * @throws PrepMisionException si no se pudo preparar la mision
     */
    protected void preparar() throws PrepMisionException{
        //Verificación inicial
        if (!(ac.getCombustibleNave() >= combustibleBase && ac.getEnergiaNave() >= energiaBase))
            throw new PrepMisionException("Recursos insuficientes");
        if (ac.consultaReqMantenimiento())
            throw new PrepMisionException("La nave necesita mantenimiento");
        if (!ac.consultaMotorDisp())
            throw new PrepMisionException("El motor no está disponible");

        //Cálculo de ruta
        this.objetivo = tablero.getObjetivo();
        this.ruta = calcularRuta(this.tablero.getPosicionNave(), this.objetivo);
    }


    /**
     * Calcula la ruta en forma de L (primero, movimientos solo en X; luego, movimientos solo en Y) que une un puntoDepartida con un objetivo
     *
     * @param puntoDePartida el punto de partida de la ruta, debe ser válida dentro del Tablero, NotNull
     * @param objetivo el punto de llegada la ruta, debe ser válida dentro del Tablero, NotNull
     * @return devuelve la ruta que une la salida y la llegada ya calculada, como una lista de Posiciones
     *
     * Precondiciones: las dimensiones del Tablero deben ser conocidas y las Posiciones deben estar dentro de las mismas
     * Postcondiciones: se devuelve la ruta conformada
     */
    private ArrayList<Posicion> calcularRuta(Posicion puntoDePartida, Posicion objetivo){

        ArrayList<Posicion> rutaCalculada = new ArrayList<>();

        int deltaX = objetivo.getX() - puntoDePartida.getX();
        int deltaY = objetivo.getY() - puntoDePartida.getY();

        //calculo movimientos en X
        int cantMovimientosX = Math.abs(deltaX);
        int sentidoX = deltaX / cantMovimientosX;
        for (int i = 1 ; i <= cantMovimientosX ; i++){
            rutaCalculada.add(new Posicion(puntoDePartida.getX() + sentidoX * i, puntoDePartida.getY()));
        }

        //calculo movimientos en Y desde la última posición calculada, donde la ruta cambia de dirección (de moverse en X pasa a moverse en Y)
        Posicion puntoCambioDireccion = new Posicion(puntoDePartida.getX() + deltaX, puntoDePartida.getY());
        int cantMovimientosY = Math.abs(deltaY);
        int sentidoY = deltaY / cantMovimientosY;
        for (int j = 1 ; j <= cantMovimientosY ; j++){
            rutaCalculada.add((new Posicion(puntoCambioDireccion.getX(), puntoCambioDireccion.getY() + sentidoY * j)));
        }

        return rutaCalculada;
    }

    /**
     * Ejecuta la mision
     */
    protected final void ejecutar() {
        ac.despegue(this.combustibleBase, this.energiaBase, this.desgasteBase);
        this.cicloDeTravesia();
    }

    private void cicloDeTravesia() {
        try {
            while (!tablero.getPosicionNave().equals(objetivo)){
                ac.inspeccionarPosicionActual(this.tablero);
                ac.movimiento(this.tablero, this.ruta);
                destino.turnoDestino();
                Thread.sleep(1000);
            }
        } catch (Exception e){

        }
    }


    private void turnoDeLaNave(){

    }

    /**
     * Evalúa los resultados de la misión
     */
    protected abstract void evaluarResultado();

    /**
     * Cierra el proceso
     */
    protected abstract void cerrar();

    protected void informe(){
        System.out.println("Informe : la mision se realizo correctamente y demas ");
    }
}
