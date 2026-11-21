package src.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class DialogoInformeMision extends JDialog {

    public DialogoInformeMision(Frame owner, String tituloMision, String contenidoInforme) {
        super(owner, "Informe de Misión - " + tituloMision, true); // true = modal, cierre manual
        setSize(520, 380);
        setLocationRelativeTo(owner);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(new Color(18, 22, 36));
        root.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JLabel lblTitulo = new JLabel("INFORME FINAL: " + tituloMision.toUpperCase(), SwingConstants.CENTER);
        lblTitulo.setForeground(new Color(100, 180, 255));
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        root.add(lblTitulo, BorderLayout.NORTH);

        JTextArea txtInforme = new JTextArea(contenidoInforme);
        txtInforme.setEditable(false);
        txtInforme.setLineWrap(true);
        txtInforme.setWrapStyleWord(true);
        txtInforme.setBackground(new Color(30, 36, 54));
        txtInforme.setForeground(Color.WHITE);
        txtInforme.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtInforme.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(txtInforme);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(100, 180, 255)));
        root.add(scroll, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar Informe");
        btnCerrar.setBackground(new Color(40, 90, 150));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.addActionListener(e -> dispose());

        JPanel panelSur = new JPanel();
        panelSur.setBackground(new Color(18, 22, 36));
        panelSur.add(btnCerrar);
        root.add(panelSur, BorderLayout.SOUTH);

        setContentPane(root);
    }
}