package src.concurrencia.vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import src.concurrencia.modelo.PlataformaDeCarga;
import src.concurrencia.modelo.EventoVisorDTO;

@SuppressWarnings("deprecation")
public class VentanaVisorUniverso extends JFrame implements IVistaVisor, Observer {
    private final PlataformaDeCarga modeloObservado;

    // Contadores en vivo (NORTH)
    private final JLabel lblGeneradoras;
    private final JLabel lblNaves;
    private final JLabel lblBaterias;

    // Lienzo de animación (CENTER)
    private final PanelAnimacionVisor panelAnimacion;

    // Bitácora de eventos y botones de control (SOUTH)
    private final JTextArea txtLogEventos;
    private final JButton btnCrearGeneradora;
    private final JButton btnCrearNave;
    private final JButton btnDetener;

    public VentanaVisorUniverso(PlataformaDeCarga plataforma) {
        super("Visor del Universo Paralelo - Plataforma de Carga");
        this.modeloObservado = plataforma;
        this.modeloObservado.addObserver(this); // Suscripción al Observable

        setSize(900, 620);
        setLocationRelativeTo(null);
        // DISPOSE_ON_CLOSE para no matar la ventana principal de la nave al cerrar el visor
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // 1. Panel Superior: Contadores en vivo
        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.CENTER, 35, 10));
        Font fuenteContadores = new Font("SansSerif", Font.BOLD, 14);
        lblGeneradoras = new JLabel("Generadoras Activas: 0");
        lblNaves = new JLabel("Naves Consumidoras: 0");
        lblBaterias = new JLabel("Baterías en Plataforma: 0");
        lblGeneradoras.setFont(fuenteContadores);
        lblNaves.setFont(fuenteContadores);
        lblBaterias.setFont(fuenteContadores);
        panelNorte.add(lblGeneradoras);
        panelNorte.add(lblBaterias);
        panelNorte.add(lblNaves);
        add(panelNorte, BorderLayout.NORTH);

        // 2. Panel Central: Animación de Naves, Plataforma y Conexiones
        panelAnimacion = new PanelAnimacionVisor();
        add(panelAnimacion, BorderLayout.CENTER);

        // 3. Panel Inferior: Registro de eventos (Observer) + Botones (MVC)
        JPanel panelSur = new JPanel(new BorderLayout(5, 5));
        panelSur.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        txtLogEventos = new JTextArea(6, 60);
        txtLogEventos.setEditable(false);
        txtLogEventos.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollLog = new JScrollPane(txtLogEventos);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Registro de Eventos en Tiempo Real"));
        panelSur.add(scrollLog, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnCrearGeneradora = new JButton("Crear Generadora de Energía");
        btnCrearNave = new JButton("Crear Nave Consumidora");
        btnDetener = new JButton("Detener Simulación");

        btnCrearGeneradora.setActionCommand(CMD_CREAR_GENERADORA);
        btnCrearNave.setActionCommand(CMD_CREAR_NAVE);
        btnDetener.setActionCommand(CMD_DETENER);

        panelBotones.add(btnCrearGeneradora);
        panelBotones.add(btnCrearNave);
        panelBotones.add(btnDetener);
        panelSur.add(panelBotones, BorderLayout.SOUTH);

        add(panelSur, BorderLayout.SOUTH);
    }

    @Override
    public void setActionListener(ActionListener controlador) {
        btnCrearGeneradora.addActionListener(controlador);
        btnCrearNave.addActionListener(controlador);
        btnDetener.addActionListener(controlador);

        // Si el usuario cierra la ventana con la 'X', delegamos la detención al controlador
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                btnDetener.doClick();
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        // Validación defensiva enseñada en Clase 11 / Clase 13
        if (o != this.modeloObservado || !(arg instanceof EventoVisorDTO)) {
            return;
        }

        EventoVisorDTO evento = (EventoVisorDTO) arg;

        // Delegamos la actualización de componentes al hilo de interfaz gráfica (EDT)
        SwingUtilities.invokeLater(() -> {
            lblGeneradoras.setText("Generadoras Activas: " + evento.getCantidadGeneradoras());
            lblNaves.setText("Naves Consumidoras: " + evento.getCantidadNaves());
            lblBaterias.setText("Baterías en Plataforma: "
                    + evento.getBateriasActuales() + " / " + evento.getCapacidadMaxima());

            txtLogEventos.append(evento.getMensajeLog() + "\n");
            txtLogEventos.setCaretPosition(txtLogEventos.getDocument().getLength());

            panelAnimacion.actualizarSnapshot(evento);
        });
    }

    @Override
    public void mostrar() {
        setVisible(true);
    }

    @Override
    public void cerrar() {
        this.modeloObservado.deleteObserver(this);
        dispose();
    }
}