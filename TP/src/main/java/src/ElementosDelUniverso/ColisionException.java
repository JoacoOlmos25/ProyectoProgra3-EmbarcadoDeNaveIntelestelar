package src.ElementosDelUniverso;

public class ColisionException extends Exception {
    public ColisionException(String message) {
        super("Condición crítica en la colisión: " + message);
    }
}
