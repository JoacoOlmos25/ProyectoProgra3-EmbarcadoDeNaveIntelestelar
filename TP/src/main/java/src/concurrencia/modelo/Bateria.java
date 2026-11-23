package src.concurrencia.modelo;

/**
 * Recurso producido por { GeneradoraDeEnergia} y consumido por {NaveConsumidora}
 * a través de la { PlataformaDeCarga}.
 *
 * <p><b>Invariante de clase:</b> {@code id > 0}.</p>
 */
public class Bateria {
    private static int contGlobal = 0;
    private final int id;

    /**
     * Crea una batería con un identificador único autoincremental seguro frente a concurrencia.
     * <p><b>Precondiciones:</b> ninguna.</p>
     * <p><b>Postcondiciones:</b> {@code getId() > 0} y único respecto de instancias previas.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     */
    public Bateria() {
        this.id = incrementarContador();
        assert this.id > 0 : "Invariante violado: el identificador de batería debe ser positivo";
    }

    private static synchronized int incrementarContador() {
        return ++contGlobal;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Batería #" + id;
    }
}