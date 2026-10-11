package src.MotorWarp;

public class TransicionRedundanteException extends MotorException {
    public TransicionRedundanteException(String message) {
        super("Transición de motor Warp redundante: " + message);
    }
}
