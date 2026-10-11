package src.MotorWarp;

public class MotorWarp {

    private MotorState estado = new DisponibleState(this);

    public void setEstado(MotorState estado) {
        this.estado = estado;
    }

    public void saltoWarp() throws EstadoMotorInvalidoException {
        estado.prepararSalto();
        estado.saltar();
        estado.enfriar();
        estado.hacerDisponible();
    }

    public void cancelarSalto() throws EstadoMotorInvalidoException {
        estado.cancelarSalto();
    } //qué pasa con el combustible si se cancela el salto?
}
