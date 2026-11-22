package src.MotorWarp;

public class TransicionInvalidaException extends MotorException {
    public TransicionInvalidaException(String message) {
        super("Transición de motor Warp inválida: " + message);
    }
}
