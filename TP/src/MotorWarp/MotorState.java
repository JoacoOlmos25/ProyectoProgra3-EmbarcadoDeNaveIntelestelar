package src.MotorWarp;

public interface MotorState {

    void prepararSalto() throws EstadoMotorInvalidoException;
    void saltar() throws EstadoMotorInvalidoException;
    void enfriar() throws EstadoMotorInvalidoException;
    void hacerDisponible() throws EstadoMotorInvalidoException;
    void cancelarSalto() throws EstadoMotorInvalidoException;
}
