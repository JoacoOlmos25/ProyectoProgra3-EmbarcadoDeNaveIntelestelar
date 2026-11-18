package src.MotorWarp;

public class CancelarMotorException extends MotorException {
    public CancelarMotorException(String message) {
        super("Salto cancelado. " + message);
    }
}
