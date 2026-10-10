package Paquete.Excepciones;

public class EstadoMotorInvalidoException extends Exception {
    public EstadoMotorInvalidoException(String message) {
        super("Estado de motor Warp inválido: " + message);
    }
}
