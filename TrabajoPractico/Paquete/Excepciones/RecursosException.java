package Paquete.Excepciones;

public class RecursosException extends Exception {
    public RecursosException(String message) {
        super("Excepción de recursos: " + message);
    }
}
