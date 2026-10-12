package src.Misiones;

import src.AC.Asistente;

public abstract class Mision {
    protected int combNecesario = 4;
    protected int energiaNecesaria;
    protected int desgasteEsperado = 4;
    protected Asistente ac;


    /**
     * Construye una misión base con valores preestablecidos
     */
    public Mision() {
    }

    /**
     * Crea una misión con valores determinados
     * @param combustible: cantidad de combustible que consumirá la misión; combustible > 0, combustible < capacidad maxima de combustible
     * @param energia: cantidad de energía que consumirá la misión; energía > 0, energía < capacidad maxima de energia
     * @param desgaste: desgaste esperado en la mision; desgaste > 0, desgaste < 100
     * @param asistente: referencia al asistente de comandos que se utilizará para ejecutar la misión
     *
     * Postcondiciones: la misión queda creada
     */
    public Mision(int combustible, int energia, int desgaste, Asistente asistente){
        super();
        this.combNecesario= combustible;
        this.energiaNecesaria = energia;
        this.desgasteEsperado = desgaste;
        this.ac = asistente;
    }

    @Override
    public String toString() {
        return "Mision{Combustible necesario=" + combNecesario +", Energia necesaria=" + energiaNecesaria +", Desgaste=" + desgasteEsperado + '}';
    }


    /**
     * Método template con los pasos necesarios para realizar una misión
     * Postcondiciones: la misión se realiza con éxito o se interrumpe por una excepción; ambos casos quegan registrados en la bitácora y el método termina de forma controlada
     */
    public void realizarMision(){
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
     * Prepara la misión
     * Post: si se dan las condiciones necesarias para iniciar la misión, no ejecuta cambios
     * @throws PrepMisionException si no se pudo preparar la mision
     */
    protected void preparar() throws PrepMisionException{
        if (!(ac.getCombustibleNave() >= combNecesario && ac.getEnergiaNave() >= energiaNecesaria))
            throw new PrepMisionException("Recursos insuficientes");
        if (ac.consultaReqMantenimiento())
            throw new PrepMisionException("La nave necesita mantenimiento");
        if (!ac.consultaMotorDisp())
            throw new PrepMisionException("El motor no está disponible");
    }

    /**
     * Ejecuta la mision
     */
    protected abstract void ejecutar();

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
