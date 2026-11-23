package src.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

/**
 * Ventana Swing de control de acceso al sistema estelar.
 * Provee las vistas de Inicio de Sesión y Registro de Nuevos Operadores.
 */
public class VentanaLogin extends JFrame implements IVistaLogin {

    private static final Color BG_OSCURO = new Color(15, 19, 32);
    private static final Color BG_PANEL = new Color(26, 33, 52);
    private static final Color ACENTO_CYAN = new Color(100, 180, 255);
    private static final Color ACENTO_VERDE = new Color(39, 174, 96);
    private static final Color ACENTO_AZUL = new Color(40, 110, 175);
    private static final Color TEXTO_BLANCO = new Color(245, 248, 255);

    private static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 16);
    private static final Font FUENTE_ETIQUETA = new Font("SansSerif", Font.BOLD, 14);
    private static final Font FUENTE_CAMPO = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FUENTE_BOTON = new Font("SansSerif", Font.BOLD, 14);

    private JTabbedPane pestanasAcceso;
    private JLabel[] etiquetasPestanas;

    // Campos de pestaña Login
    private JTextField txtUsuarioLogin;
    private JPasswordField txtPasswordLogin;
    private JButton btnLogin;

    // Campos de pestaña Registro
    private JTextField txtUsuarioRegistro;
    private JPasswordField txtPasswordRegistro;
    private JButton btnRegistrar;

    /**
     * Construye la ventana de acceso al sistema con sus paneles de Login y Registro.
     * <p><b>Precondiciones:</b> ninguna.</p>
     * <p><b>Postcondiciones:</b> la ventana queda inicializada y centrada en pantalla, lista para mostrarse.</p>
     * <p><b>Excepciones declaradas:</b> ninguna.</p>
     */
    public VentanaLogin() {
        super("Control de Acceso — Centro de Comando Estelar");
        setSize(490, 360);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10, 12));
        root.setBackground(BG_OSCURO);
        root.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        JLabel lblEncabezado = new JLabel("IDENTIFICACIÓN DE OPERADOR", SwingConstants.CENTER);
        lblEncabezado.setForeground(ACENTO_CYAN);
        lblEncabezado.setFont(FUENTE_TITULO);
        root.add(lblEncabezado, BorderLayout.NORTH);

        pestanasAcceso = new JTabbedPane();
        pestanasAcceso.setBackground(BG_OSCURO);
        pestanasAcceso.setForeground(TEXTO_BLANCO);
        pestanasAcceso.addTab("Iniciar Sesión", construirPanelLogin());
        pestanasAcceso.addTab("Registrar Usuario", construirPanelRegistro());

        // Encabezados propios en blanco sobre fondo oscuro (legibles en Windows y proyector)
        String[] titulos = {"Iniciar Sesión", "Registrar Usuario"};
        etiquetasPestanas = new JLabel[titulos.length];
        for (int i = 0; i < titulos.length; i++) {
            JLabel lblTab = new JLabel(titulos[i], SwingConstants.CENTER);
            lblTab.setOpaque(true);
            lblTab.setFont(FUENTE_ETIQUETA);
            lblTab.setForeground(TEXTO_BLANCO);
            etiquetasPestanas[i] = lblTab;
            pestanasAcceso.setTabComponentAt(i, lblTab);
        }
        actualizarEstiloPestanas();
        pestanasAcceso.addChangeListener(e -> actualizarEstiloPestanas());

        root.add(pestanasAcceso, BorderLayout.CENTER);
        setContentPane(root);
    }

    private void actualizarEstiloPestanas() {
        int activa = pestanasAcceso.getSelectedIndex();
        for (int i = 0; i < etiquetasPestanas.length; i++) {
            boolean esActiva = (i == activa);
            etiquetasPestanas[i].setBackground(esActiva ? new Color(38, 62, 102) : BG_PANEL);
            etiquetasPestanas[i].setForeground(TEXTO_BLANCO);
            etiquetasPestanas[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, esActiva ? 3 : 0, 0, ACENTO_CYAN),
                    BorderFactory.createEmptyBorder(6, 16, 6, 16)
            ));
        }
    }

    private JPanel construirPanelLogin() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(BG_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                crearBorde("CREDENCIALES DE ACCESO"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JPanel grilla = new JPanel(new GridLayout(2, 2, 10, 12));
        grilla.setBackground(BG_PANEL);

        txtUsuarioLogin = crearCampoTexto();
        txtPasswordLogin = crearCampoPassword();

        grilla.add(crearEtiqueta("Usuario:"));
        grilla.add(txtUsuarioLogin);
        grilla.add(crearEtiqueta("Contraseña:"));
        grilla.add(txtPasswordLogin);

        btnLogin = crearBoton("Ingresar al Puente de Mando", ACENTO_VERDE);
        btnLogin.setActionCommand(CMD_LOGIN);

        panel.add(grilla, BorderLayout.CENTER);
        panel.add(btnLogin, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel construirPanelRegistro() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(BG_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                crearBorde("ALTA DE NUEVO OPERADOR"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JPanel grilla = new JPanel(new GridLayout(2, 2, 10, 12));
        grilla.setBackground(BG_PANEL);

        txtUsuarioRegistro = crearCampoTexto();
        txtPasswordRegistro = crearCampoPassword();

        grilla.add(crearEtiqueta("Nuevo Usuario:"));
        grilla.add(txtUsuarioRegistro);
        grilla.add(crearEtiqueta("Nueva Contraseña:"));
        grilla.add(txtPasswordRegistro);

        btnRegistrar = crearBoton("Registrar Operador", ACENTO_AZUL);
        btnRegistrar.setActionCommand(CMD_REGISTRAR);

        panel.add(grilla, BorderLayout.CENTER);
        panel.add(btnRegistrar, BorderLayout.SOUTH);
        return panel;
    }

    @Override
    public void setActionListener(ActionListener controlador) {
        assert controlador != null : "El controlador de login no puede ser nulo";

        ActionListener validadorFormato = e -> {
            if (getUsername().isBlank() || getPassword().isBlank()) {
                mostrarError("Debe completar usuario y contraseña.");
                return;
            }
            controlador.actionPerformed(e);
        };

        btnLogin.addActionListener(validadorFormato);
        btnRegistrar.addActionListener(validadorFormato);
        getRootPane().setDefaultButton(btnLogin);
        pestanasAcceso.addChangeListener(e -> {
            if (pestanasAcceso.getSelectedIndex() == 0) {
                getRootPane().setDefaultButton(btnLogin);
            } else {
                getRootPane().setDefaultButton(btnRegistrar);
            }
        });
    }

    @Override
    public String getUsername() {
        if (pestanasAcceso.getSelectedIndex() == 0) {
            return txtUsuarioLogin.getText().trim();
        }
        return txtUsuarioRegistro.getText().trim();
    }

    @Override
    public String getPassword() {
        if (pestanasAcceso.getSelectedIndex() == 0) {
            return new String(txtPasswordLogin.getPassword());
        }
        return new String(txtPasswordRegistro.getPassword());
    }

    @Override
    public void mostrarError(String mensaje) {
        assert mensaje != null : "El mensaje de error no puede ser nulo";
        JOptionPane.showMessageDialog(this, mensaje, "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void mostrarInfo(String mensaje) {
        assert mensaje != null : "El mensaje informativo no puede ser nulo";
        JOptionPane.showMessageDialog(this, mensaje, "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void limpiarPassword() {
        txtPasswordLogin.setText("");
        txtPasswordRegistro.setText("");
    }

    @Override
    public void mostrar() {
        setVisible(true);
    }

    @Override
    public void cerrar() {
        dispose();
    }

    private TitledBorder crearBorde(String titulo) {
        TitledBorder borde = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ACENTO_CYAN, 2), titulo);
        borde.setTitleColor(ACENTO_CYAN);
        borde.setTitleFont(new Font("SansSerif", Font.BOLD, 13));
        return borde;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(TEXTO_BLANCO);
        lbl.setFont(FUENTE_ETIQUETA);
        return lbl;
    }

    private JTextField crearCampoTexto() {
        JTextField txt = new JTextField();
        txt.setFont(FUENTE_CAMPO);
        txt.setPreferredSize(new Dimension(190, 32));
        return txt;
    }

    private JPasswordField crearCampoPassword() {
        JPasswordField txt = new JPasswordField();
        txt.setFont(FUENTE_CAMPO);
        txt.setPreferredSize(new Dimension(190, 32));
        return txt;
    }

    private JButton crearBoton(String texto, Color fondo) {
        JButton btn = new JButton(texto);
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setFont(FUENTE_BOTON);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        return btn;
    }
}