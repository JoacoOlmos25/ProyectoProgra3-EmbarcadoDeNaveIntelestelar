package src.concurrencia.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import src.concurrencia.modelo.PlataformaDeCarga;
import src.concurrencia.vista.IVistaVisor;

public class ControladorVisor implements ActionListener {
    private final PlataformaDeCarga modelo;
    private final IVistaVisor vista;

    public ControladorVisor(PlataformaDeCarga modelo, IVistaVisor vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.vista.setActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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