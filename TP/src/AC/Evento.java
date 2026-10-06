package src.AC;

import java.time.LocalDateTime;

/**
 * clase evento para registrar cada suceso,la botacora va a tener una lista de eventos.
 */
public class Evento {

    public LocalDateTime horario;
    public String titulo;
    public String msj;

    public Evento(LocalDateTime t,String titulo,String msj){
        this.horario = t;
        this.titulo = titulo;
        this.msj = msj;
    }

    @Override
    public String toString() {
        return "Evento{" + "horario=" + horario + ", titulo='" + titulo + '\'' + ", msj='" + msj + '\'' + "}\n";}
}

