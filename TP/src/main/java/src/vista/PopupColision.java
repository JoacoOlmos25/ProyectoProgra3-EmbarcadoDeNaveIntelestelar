package src.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class PopupColision extends JDialog {

    public PopupColision(Frame owner, String titulo, String detalle, int duracionMs) {
        super(owner, false); // false = no modal, no bloquea el hilo principal
        setUndecorated(true);
        setFocusableWindowState(false);

        JPanel panel = new JPanel(new BorderLayout(6, 4));
        panel.setBackground(new Color(34, 24, 42));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(241, 196, 15), 2),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setForeground(new Color(241, 196, 15));
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 13));

        JLabel lblDetalle = new JLabel("<html><center>" + detalle + "</center></html>", SwingConstants.CENTER);
        lblDetalle.setForeground(Color.WHITE);
        lblDetalle.setFont(new Font("SansSerif", Font.PLAIN, 12));

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblDetalle, BorderLayout.CENTER);
        setContentPane(panel);
        pack();

        // Posicionar sobre la parte superior central de la ventana dueña
        if (owner != null) {
            int x = owner.getX() + (owner.getWidth() - getWidth()) / 2;
            int y = owner.getY() + 95;
            setLocation(x, y);
        } else {
            setLocationRelativeTo(null);
        }

        // Autocierre después de duracionMs sin bloquear Swing
        Timer timer = new Timer(duracionMs, e -> dispose());
        timer.setRepeats(false);
        timer.start();
    }
}