package src.Misiones;

public class PrepMisionException extends MisionException {
    public PrepMisionException(String message) {
        super("Problemas al preparar la misión: " + message);
    }
}
