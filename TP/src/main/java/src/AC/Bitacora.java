package src.AC;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase Bitacora: almacena la secuencia de eventos de la nave
 * y permite agregar nuevos registros y consultarlos de forma protegida.
 */
public class Bitacora {

    private final List<Evento> bitacora = new ArrayList<>();

    public Bitacora() {
    }

    public synchronized void agregaEvento(LocalDateTime t, String titulo, String msj) {
        assert t != null : "La fecha/hora del evento no puede ser nula";
        assert titulo != null && !titulo.isBlank() : "El título no puede ser nulo o vacío";
        assert msj != null : "El mensaje no puede ser nulo";

        Evento evento = new Evento(t, titulo, msj);
        bitacora.add(evento);
    }

    /**
     * Devuelve una instantánea de solo lectura de los eventos registrados hasta el momento.
     * Precondiciones: ninguna.
     * Postcondiciones: modificar externamente la lista devuelta lanza UnsupportedOperationException;
     *                  operación segura ante accesos concurrentes desde otros hilos.
     */
    public synchronized List<Evento> getEventos() {
        return Collections.unmodifiableList(new ArrayList<>(this.bitacora));
    }
}