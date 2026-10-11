package src.AC;

import java.time.LocalDateTime;

/**
 * Clase evento es para crear un registro dde cada suceso de la nave o que afecta a la misma.
 * Invariantes de Clase:
 *  titulo!="" AND titulo!=null
 *  msj!="" AND msj!=null
 *  por lo tanto un evento no puede ser nulo o vacio
 *  el horario producto de la libreria LocalDateTime devolvera la hora actual del dispositivo
 *  con el formato 2026-10-11T15:50:34.123456789
 **/
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

