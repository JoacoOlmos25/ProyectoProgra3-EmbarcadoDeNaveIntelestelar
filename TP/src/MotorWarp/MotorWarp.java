package src.MotorWarp;

import src.AC.Bitacora;

import java.time.LocalDateTime;

public class MotorWarp {

    private MotorState estado;

    public MotorWarp() {
        this.estado = new DisponibleState(this);
    }

    public void setEstado(MotorState estado) {
        this.estado = estado;
    }

    public void saltoWarp() throws MotorException {
        estado.prepararSalto();
        estado.saltar();
        estado.finalizarSalto();
        estado.hacerDisponible();
    }

    public void cancelarSalto() throws MotorException {
        estado.cancelarSalto();
    } //qué pasa con el combustible si se cancela el salto?

    public boolean consultaMotorDisponible(){
        return estado.motorDisponible();
    }
}
