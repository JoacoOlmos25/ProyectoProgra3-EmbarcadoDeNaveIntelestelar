package src.concurrencia.vista;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;
import src.concurrencia.modelo.EstadoEntidad;
import src.concurrencia.modelo.EstadoEntidadDTO;
import src.concurrencia.modelo.EventoVisorDTO;

public class PanelAnimacionVisor extends JPanel {
    private int bateriasActuales = 0;
    private int capacidadMaxima = 5;
    private List<EstadoEntidadDTO> generadoras = Collections.emptyList();
    private List<EstadoEntidadDTO> naves = Collections.emptyList();

    public PanelAnimacionVisor() {
        setBackground(new Color(18, 22, 36)); // Fondo espacial oscuro
    }

    public void actualizarSnapshot(EventoVisorDTO dto) {
        this.bateriasActuales = dto.getBateriasActuales();
        this.capacidadMaxima = dto.getCapacidadMaxima();
        this.generadoras = dto.getGeneradorasSnapshot();
        this.naves = dto.getNavesSnapshot();
        repaint(); // Solicita a Swing redibujar el panel
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int centroX = w / 2;
        int centroY = h / 2;

        // 1. Dibujar líneas de conexión primero (para que queden por detrás de las cajas)
        dibujarConexiones(g2, generadoras, 90, centroX, centroY, h);
        dibujarConexiones(g2, naves, w - 90, centroX, centroY, h);

        // 2. Dibujar la Plataforma de Carga en el centro
        dibujarPlataformaCentral(g2, centroX, centroY);

        // 3. Dibujar columna de Generadoras (Izquierda) y Naves (Derecha)
        dibujarEntidades(g2, generadoras, 30, h);
        dibujarEntidades(g2, naves, w - 150, h);

        // 4. Dibujar leyenda de colores abajo
        dibujarLeyenda(g2, w, h);
    }

    private void dibujarConexiones(Graphics2D g2, List<EstadoEntidadDTO> lista, int origenX, int destX, int destY, int altoPanel) {
        int n = lista.size();
        for (int i = 0; i < n; i++) {
            EstadoEntidadDTO ent = lista.get(i);
            if (ent.getEstado() == EstadoEntidad.TRABAJANDO) {
                continue; // Fuera del monitor no hay línea de conexión
            }

            int origenY = calcularPosicionY(i, n, altoPanel);
            g2.setColor(obtenerColorEstado(ent.getEstado()));

            if (ent.getEstado() == EstadoEntidad.OPERANDO) {
                // Línea verde gruesa cuando tiene el acceso exclusivo al monitor
                g2.setStroke(new BasicStroke(3.5f));
            } else {
                // Línea punteada cuando está solicitando (Amarillo) o en wait (Rojo)
                float[] patron = {6f, 6f};
                g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, patron, 0.0f));
            }

            g2.drawLine(origenX, origenY, destX, destY);
        }
        g2.setStroke(new BasicStroke(1.0f));
    }

    private void dibujarPlataformaCentral(Graphics2D g2, int cx, int cy) {
        int anchoPlat = 130;
        int altoPlat = 170;
        int x = cx - anchoPlat / 2;
        int y = cy - altoPlat / 2;

        g2.setColor(new Color(40, 50, 75));
        g2.fillRoundRect(x, y, anchoPlat, altoPlat, 16, 16);
        g2.setColor(new Color(100, 180, 255));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(x, y, anchoPlat, altoPlat, 16, 16);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2.drawString("PLATAFORMA", x + 22, y + 22);
        g2.drawString("Baterías: " + bateriasActuales + "/" + capacidadMaxima, x + 22, y + 40);

        // Dibujar las ranuras de baterías
        int altoSlot = 18;
        int espacio = 5;
        int baseY = y + altoPlat - 15;
        for (int i = 0; i < capacidadMaxima; i++) {
            int slotY = baseY - (i + 1) * (altoSlot + espacio);
            if (i < bateriasActuales) {
                g2.setColor(new Color(46, 213, 115)); // Batería cargada
                g2.fillRoundRect(x + 20, slotY, anchoPlat - 40, altoSlot, 6, 6);
            } else {
                g2.setColor(new Color(70, 80, 105)); // Ranura vacía
                g2.drawRoundRect(x + 20, slotY, anchoPlat - 40, altoSlot, 6, 6);
            }
        }
    }

    private void dibujarEntidades(Graphics2D g2, List<EstadoEntidadDTO> lista, int x, int altoPanel) {
        int n = lista.size();
        int anchoCaja = 120;
        int altoCaja = 42;

        for (int i = 0; i < n; i++) {
            EstadoEntidadDTO ent = lista.get(i);
            int centroY = calcularPosicionY(i, n, altoPanel);
            int y = centroY - altoCaja / 2;

            Color colorEstado = obtenerColorEstado(ent.getEstado());
            g2.setColor(new Color(30, 36, 54));
            g2.fillRoundRect(x, y, anchoCaja, altoCaja, 10, 10);

            g2.setColor(colorEstado);
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawRoundRect(x, y, anchoCaja, altoCaja, 10, 10);

            // Indicador tipo semáforo a la izquierda del nombre
            g2.fillOval(x + 8, y + 13, 14, 14);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            g2.drawString(ent.getNombre(), x + 28, y + 18);

            g2.setColor(colorEstado);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
            g2.drawString(ent.getEstado().name(), x + 28, y + 33);
        }
    }

    private void dibujarLeyenda(Graphics2D g2, int w, int h) {
        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        int y = h - 12;
        g2.setColor(new Color(75, 156, 211));
        g2.drawString("● TRABAJANDO (Fuera)", 20, y);
        g2.setColor(new Color(241, 196, 15));
        g2.drawString("● SOLICITANDO (Pidiendo Lock)", 175, y);
        g2.setColor(new Color(231, 76, 60));
        g2.drawString("● ESPERANDO (En wait())", 365, y);
        g2.setColor(new Color(46, 204, 113));
        g2.drawString("● OPERANDO (En Sección Crítica)", 530, y);
    }

    private int calcularPosicionY(int indice, int total, int altoPanel) {
        int margenSuperior = 40;
        int altoUtil = altoPanel - 80;
        return margenSuperior + (altoUtil * (indice + 1)) / (total + 1);
    }

    private Color obtenerColorEstado(EstadoEntidad estado) {
        switch (estado) {
            case SOLICITANDO:
                return new Color(241, 196, 15); // Amarillo
            case ESPERANDO:
                return new Color(231, 76, 60);  // Rojo
            case OPERANDO:
                return new Color(46, 204, 113); // Verde
            case TRABAJANDO:
            default:
                return new Color(75, 156, 211); // Azul
        }
    }
}