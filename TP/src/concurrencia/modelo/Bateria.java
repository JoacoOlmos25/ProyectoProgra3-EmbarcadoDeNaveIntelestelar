package src.concurrencia.modelo;

public class Bateria {
    private static int contGlobal = 0;
    private final int id;

    public Bateria() {
        this.id = incrementarContador();
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
