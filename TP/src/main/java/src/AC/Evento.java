package src.AC;

import java.time.LocalDateTime;

/**
 * Representa un registro inmutable de un suceso ocurrido en la nave o su entorno.
 *
 * Invariante de clase:
 * - horario != null
 * - titulo != null && !titulo.isBlank()
 * - msj != null && !msj.isBlank()
 */
public class Evento {

    private final LocalDateTime horario;
    private final String titulo;
    private final String msj;

    /**
     * Crea un evento inmutable de bitácora.
     * Precondiciones: t != null, titulo != null && !titulo.isBlank(), msj != null && !msj.isBlank()
     * Postcondiciones: el evento queda construido cumpliendo el invariante de clase
     */
    public Evento(LocalDateTime t, String titulo, String msj) {
        assert t != null : "La fecha/hora del evento no puede ser nula";
        assert titulo != null && !titulo.isBlank() : "El título del evento no puede ser nulo ni vacío";
        assert msj != null && !msj.isBlank() : "El mensaje del evento no puede ser nulo ni vacío";

        this.horario = t;
        this.titulo = titulo;
        this.msj = msj;

        assert invariante() : "Invariante de Evento violado tras construcción";
    }

    private boolean invariante() {
        return horario != null
                && titulo != null && !titulo.isBlank()
                && msj != null && !msj.isBlank();
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMsj() {
        return msj;
    }

    @Override
    public String toString() {
        return "Evento{" + "horario=" + horario + ", titulo='" + titulo + '\'' + ", msj='" + msj + '\'' + "}\n";
    }
}