package src.concurrencia.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import src.concurrencia.modelo.PlataformaDeCarga;
import src.concurrencia.vista.IVistaVisor;

/**
 * Controlador MVC del Visor del Universo Paralelo.
 * Recibe los eventos de {@link IVistaVisor} y delega la creación de hilos y el cierre en {@link PlataformaDeCarga}.
 */
public class ControladorVisor implements ActionListener {
    private final PlataformaDeCarga modelo;
    private final IVistaVisor vista;

    /**
     * Vincula la vista del visor concurrente con su plataforma de carga.
     * <p><b>Precondiciones:</b> {@code modelo != null && vista != null}.</p>
     * <p><b>Postcondiciones:</b> el controlador queda registrado como {@link ActionListener} de {@code vista}.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     */
    public ControladorVisor(PlataformaDeCarga modelo, IVistaVisor vista) {
        assert modelo != null : "La plataforma de carga no puede ser nula";
        assert vista != null : "La vista del visor no puede ser nula";

        this.modelo = modelo;
        this.vista = vista;
        this.vista.setActionListener(this);
    }

    /**
     * Despacha la creación dinámica de productores/consumidores o la detención de la simulación.
     * <p><b>Precondiciones:</b> {@code e != null}.</p>
     * <p><b>Postcondiciones:</b> ejecuta en el modelo la operación correspondiente al comando recibido.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        assert e != null : "El evento de acción no puede ser nulo";
        String comando = e.getActionCommand();

        if (IVistaVisor.CMD_CREAR_GENERADORA.equals(comando)) {
            modelo.crearGeneradora();
        } else if (IVistaVisor.CMD_CREAR_NAVE.equals(comando)) {
            modelo.crearNaveConsumidora();
        } else if (IVistaVisor.CMD_DETENER.equals(comando)) {
            modelo.detenerSimulacion();
        }
    }
}