package src.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import src.destinoytablero.Observador;
import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;

public class VentanaPrincipal extends JFrame implements IVistaPrincipal, Observador {
    private static final Color BG_OSCURO = new Color(15, 19, 32);
    private static final Color BG_PANEL = new Color(26, 33, 52);
    private static final Color BG_TARJETA = new Color(19, 25, 42);
    private static final Color ACENTO_CYAN = new Color(100, 180, 255);
    private static final Color ACENTO_VERDE = new Color(46, 213, 115);
    private static final Color ACENTO_MORADO = new Color(155, 89, 182);
    private static final Color ACENTO_AMARILLO = new Color(250, 204, 21);
    private static final Color ACENTO_ROJO = new Color(231, 76, 60);
    private static final Color TEXTO_BLANCO = new Color(245, 248, 255);

    // Escala tipográfica pensada para proyección en aula
    private static final Font FUENTE_TITULO_BORDE = new Font("SansSerif", Font.BOLD, 13);
    private static final Font FUENTE_ETIQUETA = new Font("SansSerif", Font.BOLD, 14);
    private static final Font FUENTE_CONTROL = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FUENTE_BOTON = new Font("SansSerif", Font.BOLD, 14);
    private static final Font FUENTE_TELEMETRIA = new Font("Monospaced", Font.BOLD, 16);
    private static final Font FUENTE_TURNO = new Font("Monospaced", Font.BOLD, 15);
    private static final Font FUENTE_CONSOLA = new Font("Monospaced", Font.PLAIN, 13);

    private Tablero tableroObservado;

    // --- BANNER NORTE: Flota + Tarjetas de Recursos ---
    private JComboBox<String> cmbTipoNave;
    private JButton btnCrearNave;
    private JComboBox<String> cmbNaveActiva;
    private JLabel lblCombustible;
    private JLabel lblEnergia;
    private JLabel lblDesgaste;
    private JLabel lblMotorWarp;

    // --- BANNER OESTE: Misiones y Tripulación ---
    private JComboBox<String> cmbMisiones;
    private JButton btnIniciarMision;
    private JButton btnMantenimiento;
    private JComboBox<String> cmbCargoTripulante;
    private JComboBox<String> cmbOrigenTripulante;
    private JTextField txtIdentTripulante;
    private JTextField txtAntiguedadTripulante;
    private JButton btnAgregarTripulante;

    // --- BANNER CENTRO: Indicador de Turno + Tablero Dinámico ---
    private JPanel panelIndicadorTurno;
    private JLabel lblIndicadorTurno;
    private PanelTablero panelTablero;

    // --- BANNER ESTE: Pestañas legibles (Bitácora, Tripulación, Informes) ---
    private JTabbedPane pestanasEste;
    private JLabel[] etiquetasPestanas;
    private JTextArea txtBitacora;
    private DefaultListModel<String> modeloListaTripulantes;
    private JTextArea txtHistorialInformes;

    // --- BANNER SUR: Trigger del Visor de Concurrencia ---
    private JButton btnAbrirConcurrencia;

    public VentanaPrincipal() {
        super("Centro de Comando Estelar - Control de Nave");
        setSize(1366, 820);
        setMinimumSize(new Dimension(1120, 720));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contenedor = new JPanel(new BorderLayout(10, 10));
        contenedor.setBackground(BG_OSCURO);
        contenedor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contenedor);

        contenedor.add(construirBannerNorte(), BorderLayout.NORTH);
        contenedor.add(construirBannerOeste(), BorderLayout.WEST);
        contenedor.add(construirBannerCentro(), BorderLayout.CENTER);
        contenedor.add(construirBannerEste(), BorderLayout.EAST);
        contenedor.add(construirBannerSurConcurrencia(), BorderLayout.SOUTH);
    }

    private JPanel construirBannerNorte() {
        JPanel banner = new JPanel(new BorderLayout(8, 8));
        banner.setBackground(BG_PANEL);
        banner.setBorder(crearBordeFuturista("FLOTA Y TELEMETRÍA EN VIVO", ACENTO_CYAN));

        JPanel filaFlota = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        filaFlota.setBackground(BG_PANEL);

        cmbTipoNave = crearComboEstilizado(new String[]{"Explorador", "Carguero", "Ataque"});
        btnCrearNave = crearBotonEstilizado("Crear Nave", new Color(40, 110, 175));
        btnCrearNave.setActionCommand(CMD_CREAR_NAVE);

        cmbNaveActiva = crearComboEstilizado(new String[]{});
        cmbNaveActiva.setPreferredSize(new Dimension(230, 32));
        cmbNaveActiva.setActionCommand(CMD_SELECCIONAR_NAVE);

        filaFlota.add(crearEtiqueta("Nueva Nave:"));
        filaFlota.add(cmbTipoNave);
        filaFlota.add(btnCrearNave);
        filaFlota.add(Box.createHorizontalStrut(20));
        filaFlota.add(crearEtiqueta("Nave en Uso:"));
        filaFlota.add(cmbNaveActiva);

        JPanel filaTelemetria = new JPanel(new GridLayout(1, 4, 12, 0));
        filaTelemetria.setBackground(BG_PANEL);
        filaTelemetria.setBorder(BorderFactory.createEmptyBorder(2, 8, 6, 8));

        lblCombustible = crearEtiquetaTelemetria("COMBUSTIBLE: -- / 100", ACENTO_CYAN);
        lblEnergia = crearEtiquetaTelemetria("ENERGÍA: -- / 100", ACENTO_AMARILLO);
        lblDesgaste = crearEtiquetaTelemetria("DESGASTE: --%", ACENTO_VERDE);
        lblMotorWarp = crearEtiquetaTelemetria("MOTOR WARP: --", ACENTO_CYAN);

        filaTelemetria.add(envolverTarjetaTelemetria(lblCombustible, ACENTO_CYAN));
        filaTelemetria.add(envolverTarjetaTelemetria(lblEnergia, ACENTO_AMARILLO));
        filaTelemetria.add(envolverTarjetaTelemetria(lblDesgaste, ACENTO_VERDE));
        filaTelemetria.add(envolverTarjetaTelemetria(lblMotorWarp, ACENTO_CYAN));

        banner.add(filaFlota, BorderLayout.NORTH);
        banner.add(filaTelemetria, BorderLayout.SOUTH);
        return banner;
    }

    private JPanel envolverTarjetaTelemetria(JLabel etiqueta, Color colorBorde) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(BG_TARJETA);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBorde, 2),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        tarjeta.add(etiqueta, BorderLayout.CENTER);
        return tarjeta;
    }

    private JScrollPane construirBannerOeste() {
        JPanel oeste = new JPanel();
        oeste.setLayout(new BoxLayout(oeste, BoxLayout.Y_AXIS));
        oeste.setBackground(BG_OSCURO);

        // Sub-banner 1: Control de Misiones
        JPanel panelMision = new JPanel(new GridLayout(4, 1, 6, 8));
        panelMision.setBackground(BG_PANEL);
        panelMision.setBorder(BorderFactory.createCompoundBorder(
                crearBordeFuturista("CONTROL DE MISIONES", ACENTO_CYAN),
                BorderFactory.createEmptyBorder(6, 8, 8, 8)
        ));
        panelMision.setAlignmentX(LEFT_ALIGNMENT);

        cmbMisiones = crearComboEstilizado(new String[]{
                "M-01: Intercepción y Asistencia",
                "M-02: Recolección",
                "M-03: Retorno a Punto Seguro"
        });
        btnIniciarMision = crearBotonEstilizado("Iniciar Misión", new Color(39, 174, 96));
        btnIniciarMision.setActionCommand(CMD_INICIAR_MISION);

        btnMantenimiento = crearBotonEstilizado("Realizar Mantenimiento", new Color(211, 84, 0));
        btnMantenimiento.setActionCommand(CMD_MANTENIMIENTO);

        panelMision.add(crearEtiqueta("Seleccionar Misión:"));
        panelMision.add(cmbMisiones);
        panelMision.add(btnIniciarMision);
        panelMision.add(btnMantenimiento);
        panelMision.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelMision.getPreferredSize().height));

        // Sub-banner 2: Alta de Tripulación compacto (4x2 + botón)
        JPanel panelTrip = new JPanel(new BorderLayout(0, 10));
        panelTrip.setBackground(BG_PANEL);
        panelTrip.setBorder(BorderFactory.createCompoundBorder(
                crearBordeFuturista("ALTA DE TRIPULACIÓN", ACENTO_CYAN),
                BorderFactory.createEmptyBorder(6, 8, 8, 8)
        ));
        panelTrip.setAlignmentX(LEFT_ALIGNMENT);

        JPanel grillaTrip = new JPanel(new GridLayout(4, 2, 8, 8));
        grillaTrip.setBackground(BG_PANEL);

        txtIdentTripulante = crearCampoTextoEstilizado("");
        txtAntiguedadTripulante = crearCampoTextoEstilizado("1");
        cmbCargoTripulante = crearComboEstilizado(new String[]{"Capitan", "Teniente", "Alferez", "Consejero"});
        cmbOrigenTripulante = crearComboEstilizado(new String[]{"Terricola", "Vulcano", "Marciano"});

        grillaTrip.add(crearEtiqueta("Identidad:"));
        grillaTrip.add(txtIdentTripulante);
        grillaTrip.add(crearEtiqueta("Antigüedad:"));
        grillaTrip.add(txtAntiguedadTripulante);
        grillaTrip.add(crearEtiqueta("Cargo:"));
        grillaTrip.add(cmbCargoTripulante);
        grillaTrip.add(crearEtiqueta("Origen:"));
        grillaTrip.add(cmbOrigenTripulante);

        btnAgregarTripulante = crearBotonEstilizado("Asignar Tripulante", new Color(41, 128, 185));
        btnAgregarTripulante.setActionCommand(CMD_AGREGAR_TRIPULANTE);

        panelTrip.add(grillaTrip, BorderLayout.CENTER);
        panelTrip.add(btnAgregarTripulante, BorderLayout.SOUTH);
        panelTrip.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelTrip.getPreferredSize().height));

        oeste.add(panelMision);
        oeste.add(Box.createVerticalStrut(10));
        oeste.add(panelTrip);
        oeste.add(Box.createVerticalGlue());

        JScrollPane scrollOeste = new JScrollPane(oeste);
        scrollOeste.setBorder(BorderFactory.createEmptyBorder());
        scrollOeste.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollOeste.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollOeste.getViewport().setBackground(BG_OSCURO);
        scrollOeste.setPreferredSize(new Dimension(300, 0));
        scrollOeste.getVerticalScrollBar().setUnitIncrement(16);

        return scrollOeste;
    }

    private JPanel construirBannerCentro() {
        JPanel centro = new JPanel(new BorderLayout(0, 6));
        centro.setBackground(BG_OSCURO);
        centro.setBorder(crearBordeFuturista("RADAR DEL UNIVERSO DINÁMICO", ACENTO_CYAN));

        // Barra superior de estado de turno (NAVE vs. UNIVERSO / DESTINO)
        panelIndicadorTurno = new JPanel(new BorderLayout());
        panelIndicadorTurno.setBackground(BG_TARJETA);
        panelIndicadorTurno.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, ACENTO_CYAN),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));

        lblIndicadorTurno = new JLabel("ESTADO DE TURNO: EN ESPERA DE ÓRDENES", SwingConstants.CENTER);
        lblIndicadorTurno.setForeground(ACENTO_CYAN);
        lblIndicadorTurno.setFont(FUENTE_TURNO);
        panelIndicadorTurno.add(lblIndicadorTurno, BorderLayout.CENTER);

        panelTablero = new PanelTablero();
        centro.add(panelIndicadorTurno, BorderLayout.NORTH);
        centro.add(panelTablero, BorderLayout.CENTER);
        return centro;
    }

    private JPanel construirBannerEste() {
        JPanel este = new JPanel(new BorderLayout());
        este.setBackground(BG_OSCURO);
        este.setPreferredSize(new Dimension(380, 0));

        pestanasEste = new JTabbedPane();
        pestanasEste.setBackground(BG_OSCURO);
        pestanasEste.setForeground(TEXTO_BLANCO);

        txtBitacora = crearAreaTextoOscura();
        pestanasEste.addTab("Bitácora", new JScrollPane(txtBitacora));

        modeloListaTripulantes = new DefaultListModel<>();
        JList<String> listaTripulantes = new JList<>(modeloListaTripulantes);
        listaTripulantes.setBackground(BG_PANEL);
        listaTripulantes.setForeground(TEXTO_BLANCO);
        listaTripulantes.setFont(FUENTE_CONSOLA);
        pestanasEste.addTab("Tripulación", new JScrollPane(listaTripulantes));

        txtHistorialInformes = crearAreaTextoOscura();
        pestanasEste.addTab("Informes", new JScrollPane(txtHistorialInformes));

        String[] titulos = {"Bitácora", "Tripulación", "Informes"};
        etiquetasPestanas = new JLabel[titulos.length];
        for (int i = 0; i < titulos.length; i++) {
            JLabel lblTab = new JLabel(titulos[i], SwingConstants.CENTER);
            lblTab.setOpaque(true);
            lblTab.setFont(new Font("SansSerif", Font.BOLD, 14));
            lblTab.setForeground(TEXTO_BLANCO);
            etiquetasPestanas[i] = lblTab;
            pestanasEste.setTabComponentAt(i, lblTab);
        }
        actualizarEstiloPestanas();
        pestanasEste.addChangeListener(e -> actualizarEstiloPestanas());

        este.add(pestanasEste, BorderLayout.CENTER);
        return este;
    }

    private void actualizarEstiloPestanas() {
        int activa = pestanasEste.getSelectedIndex();
        for (int i = 0; i < etiquetasPestanas.length; i++) {
            boolean esActiva = (i == activa);
            etiquetasPestanas[i].setBackground(esActiva ? new Color(38, 62, 102) : BG_PANEL);
            etiquetasPestanas[i].setForeground(TEXTO_BLANCO);
            etiquetasPestanas[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, esActiva ? 3 : 0, 0, ACENTO_CYAN),
                    BorderFactory.createEmptyBorder(6, 14, 6, 14)
            ));
        }
    }

    private JPanel construirBannerSurConcurrencia() {
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        sur.setBackground(new Color(30, 22, 52));
        sur.setBorder(crearBordeFuturista("SECCIÓN RECÓNDITA — UNIVERSO PARALELO (CONCURRENCIA)", ACENTO_MORADO));

        JLabel lblDesc = crearEtiqueta("Monitoreo en vivo de Generadoras de Energía y Naves en la Plataforma de Carga");
        btnAbrirConcurrencia = crearBotonEstilizado("Abrir Visor del Universo Paralelo", ACENTO_MORADO);
        btnAbrirConcurrencia.setActionCommand(CMD_ABRIR_CONCURRENCIA);

        sur.add(lblDesc);
        sur.add(btnAbrirConcurrencia);
        return sur;
    }

    // =========================================================================
    // IMPLEMENTACIÓN DE Observador (src.destinoytablero.Observador)
    // =========================================================================

    @Override
    public void actualizar(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            panelTablero.repaint();
            if (mensaje != null && !mensaje.isBlank()) {
                txtBitacora.append("[TABLERO] " + mensaje + "\n");
                txtBitacora.setCaretPosition(txtBitacora.getDocument().getLength());
            }
        });
    }

    // =========================================================================
    // IMPLEMENTACIÓN DE IVistaPrincipal
    // =========================================================================

    @Override
    public void setActionListener(ActionListener controlador) {
        btnCrearNave.addActionListener(controlador);
        cmbNaveActiva.addActionListener(controlador);
        btnAgregarTripulante.addActionListener(controlador);
        btnIniciarMision.addActionListener(controlador);
        btnMantenimiento.addActionListener(controlador);
        btnAbrirConcurrencia.addActionListener(controlador);
    }

    @Override
    public void vincularTablero(Tablero tablero) {
        if (this.tableroObservado != null) {
            this.tableroObservado.quitarObservador(this);
        }
        this.tableroObservado = tablero;
        if (this.tableroObservado != null) {
            this.tableroObservado.agregarObservador(this);
        }
        panelTablero.setTablero(tablero);
    }

    @Override
    public void actualizarPuntoOrigen(Posicion origen) {
        SwingUtilities.invokeLater(() -> panelTablero.setPuntoOrigen(origen));
    }

    @Override
    public void actualizarRutaPredefinida(List<Posicion> ruta) {
        SwingUtilities.invokeLater(() -> panelTablero.setRutaPredefinida(ruta));
    }

    @Override
    public void actualizarIndicadorTurno(String descripcionTurno, String tipoTurno) {
        SwingUtilities.invokeLater(() -> {
            Color colorAcento;
            Color colorFondo;
            if (TURNO_NAVE.equals(tipoTurno)) {
                colorAcento = ACENTO_VERDE;
                colorFondo = new Color(18, 42, 34);
            } else if (TURNO_UNIVERSO.equals(tipoTurno)) {
                colorAcento = ACENTO_MORADO;
                colorFondo = new Color(36, 22, 54);
            } else {
                colorAcento = ACENTO_CYAN;
                colorFondo = BG_TARJETA;
            }
            panelIndicadorTurno.setBackground(colorFondo);
            panelIndicadorTurno.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, colorAcento),
                    BorderFactory.createEmptyBorder(6, 12, 6, 12)
            ));
            lblIndicadorTurno.setForeground(colorAcento);
            lblIndicadorTurno.setText(descripcionTurno);
        });
    }

    @Override
    public void setControlesOperacionHabilitados(boolean habilitado) {
        SwingUtilities.invokeLater(() -> {
            btnIniciarMision.setEnabled(habilitado);
            btnMantenimiento.setEnabled(habilitado);
            cmbNaveActiva.setEnabled(habilitado);
            btnCrearNave.setEnabled(habilitado);
            btnAgregarTripulante.setEnabled(habilitado);
        });
    }

    @Override
    public void actualizarFlota(List<String> nombresNaves, int indiceSeleccionado) {
        SwingUtilities.invokeLater(() -> {
            ActionListener[] listeners = cmbNaveActiva.getActionListeners();
            for (ActionListener l : listeners) {
                cmbNaveActiva.removeActionListener(l);
            }
            cmbNaveActiva.removeAllItems();
            for (String nombre : nombresNaves) {
                cmbNaveActiva.addItem(nombre);
            }
            if (indiceSeleccionado >= 0 && indiceSeleccionado < nombresNaves.size()) {
                cmbNaveActiva.setSelectedIndex(indiceSeleccionado);
            }
            for (ActionListener l : listeners) {
                cmbNaveActiva.addActionListener(l);
            }
        });
    }

    @Override
    public void actualizarRecursos(int combustible, int energia, int desgaste, boolean motorDisponible, boolean reqMantenimiento) {
        SwingUtilities.invokeLater(() -> {
            lblCombustible.setText("COMBUSTIBLE: " + combustible + " / 100");
            lblEnergia.setText("ENERGÍA: " + energia + " / 100");
            lblDesgaste.setText("DESGASTE: " + desgaste + "%");
            lblDesgaste.setForeground(reqMantenimiento ? ACENTO_ROJO : ACENTO_VERDE);
            lblMotorWarp.setText("WARP: " + (motorDisponible ? "DISPONIBLE" : "NO DISP."));
            lblMotorWarp.setForeground(motorDisponible ? ACENTO_VERDE : ACENTO_AMARILLO);
        });
    }

    @Override
    public void actualizarListadoTripulantes(List<String> detalleTripulantesConSalario) {
        SwingUtilities.invokeLater(() -> {
            modeloListaTripulantes.clear();
            for (String item : detalleTripulantesConSalario) {
                modeloListaTripulantes.addElement(item);
            }
        });
    }

    @Override
    public void agregarEntradaBitacora(String texto) {
        SwingUtilities.invokeLater(() -> {
            txtBitacora.append(texto + "\n");
            txtBitacora.setCaretPosition(txtBitacora.getDocument().getLength());
        });
    }

    @Override
    public void agregarInformeHistorial(String resumenInforme) {
        SwingUtilities.invokeLater(() -> {
            txtHistorialInformes.append("================================\n");
            txtHistorialInformes.append(resumenInforme + "\n\n");
            txtHistorialInformes.setCaretPosition(txtHistorialInformes.getDocument().getLength());
        });
    }

    @Override
    public void mostrarPopupColision(String titulo, String detalle, int duracionMs) {
        SwingUtilities.invokeLater(() -> new PopupColision(this, titulo, detalle, duracionMs).setVisible(true));
    }

    @Override
    public void mostrarPopupInformeMision(String tituloMision, String contenidoInforme) {
        SwingUtilities.invokeLater(() -> new DialogoInformeMision(this, tituloMision, contenidoInforme).setVisible(true));
    }

    @Override
    public void mostrarMensajeError(String mensaje) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, mensaje, "Aviso de Comando", JOptionPane.WARNING_MESSAGE));
    }

    @Override public String getTipoNaveACrear() { return (String) cmbTipoNave.getSelectedItem(); }
    @Override public int getIndiceNaveSeleccionada() { return cmbNaveActiva.getSelectedIndex(); }
    @Override public String getMisionSeleccionada() { return (String) cmbMisiones.getSelectedItem(); }
    @Override public String getCargoTripulante() { return (String) cmbCargoTripulante.getSelectedItem(); }
    @Override public String getOrigenTripulante() { return (String) cmbOrigenTripulante.getSelectedItem(); }
    @Override public String getIdentTripulante() { return txtIdentTripulante.getText().trim(); }
    @Override public String getAntiguedadTripulante() { return txtAntiguedadTripulante.getText().trim(); }

    @Override public void mostrar() { setVisible(true); }

    @Override
    public void cerrar() {
        if (this.tableroObservado != null) {
            this.tableroObservado.quitarObservador(this);
        }
        dispose();
    }

    // =========================================================================
    // HELPERS DE ESTILO PARA PROYECTOR
    // =========================================================================

    private TitledBorder crearBordeFuturista(String titulo, Color colorBorde) {
        TitledBorder borde = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(colorBorde, 2), titulo);
        borde.setTitleColor(colorBorde);
        borde.setTitleFont(FUENTE_TITULO_BORDE);
        return borde;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(TEXTO_BLANCO);
        lbl.setFont(FUENTE_ETIQUETA);
        return lbl;
    }

    private JLabel crearEtiquetaTelemetria(String texto, Color color) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setForeground(color);
        lbl.setFont(FUENTE_TELEMETRIA);
        return lbl;
    }

    private JButton crearBotonEstilizado(String texto, Color fondo) {
        JButton btn = new JButton(texto);
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setFont(FUENTE_BOTON);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        return btn;
    }

    private JComboBox<String> crearComboEstilizado(String[] items) {
        JComboBox<String> cmb = new JComboBox<>(items);
        cmb.setFont(FUENTE_CONTROL);
        return cmb;
    }

    private JTextField crearCampoTextoEstilizado(String valorInicial) {
        JTextField txt = new JTextField(valorInicial);
        txt.setFont(FUENTE_CONTROL);
        return txt;
    }

    private JTextArea crearAreaTextoOscura() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(BG_PANEL);
        area.setForeground(TEXTO_BLANCO);
        area.setFont(FUENTE_CONSOLA);
        area.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return area;
    }
}