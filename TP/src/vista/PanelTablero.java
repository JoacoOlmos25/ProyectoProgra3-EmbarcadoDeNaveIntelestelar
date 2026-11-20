package src.vista;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;
import src.destinoytablero.ElementoDelUniverso;
import src.destinoytablero.Posicion;
import src.destinoytablero.Tablero;
import java.awt.geom.AffineTransform;

public class PanelTablero extends JPanel {
    private static final Color COLOR_FONDO = new Color(15, 19, 32);
    private static final Color COLOR_GRILLA = new Color(38, 48, 74);
    private static final Color COLOR_MARCO = new Color(100, 180, 255, 170);

    private static final Color COLOR_RUTA_CELDA = new Color(155, 170, 195, 55);
    private static final Color COLOR_RUTA_LINEA = new Color(175, 190, 215, 150);

    private static final Color COLOR_ORIGEN = new Color(56, 189, 248);
    private static final Color COLOR_NAVE = new Color(46, 213, 115);
    private static final Color COLOR_OBJETIVO = new Color(250, 204, 21);
    private static final Color COLOR_DESCONOCIDO_BG = new Color(58, 42, 82);
    private static final Color COLOR_DESCONOCIDO_BORDE = new Color(192, 132, 252);

    private Posicion ultimaPosicionNave;
    private double anguloNave = 0.0;

    private Tablero tablero;
    private Posicion puntoOrigen;
    private List<Posicion> rutaPredefinida = Collections.emptyList();

    public PanelTablero() {
        setBackground(COLOR_FONDO);
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
        if (tablero != null && this.puntoOrigen == null) {
            this.puntoOrigen = tablero.getPosicionNave();
        }
        repaint();
    }

    public void setPuntoOrigen(Posicion origen) {
        this.puntoOrigen = origen;
        repaint();
    }

    public void setRutaPredefinida(List<Posicion> ruta) {
        this.rutaPredefinida = (ruta != null) ? ruta : Collections.emptyList();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (tablero == null) {
            dibujarEstadoSinTablero(g2);
            return;
        }

        int filas = tablero.getX();
        int columnas = tablero.getY();
        int margenHorizontal = 24;
        int margenSuperior = 18;
        int margenInferior = 44; // Espacio reservado para la leyenda

        int anchoDisponible = getWidth() - 2 * margenHorizontal;
        int altoDisponible = getHeight() - margenSuperior - margenInferior;

        int tamCelda = Math.max(16, Math.min(anchoDisponible / columnas, altoDisponible / filas));
        int offsetX = (getWidth() - tamCelda * columnas) / 2;
        int offsetY = margenSuperior + (altoDisponible - tamCelda * filas) / 2;

        // 1. Grilla minimalista
        dibujarGrilla(g2, filas, columnas, tamCelda, offsetX, offsetY);

        // 2. Ruta predefinida en gris sutil (por debajo de iconos y nave)
        dibujarRutaPredefinida(g2, tamCelda, offsetX, offsetY);

        // 3. Punto de origen de la travesía
        if (puntoOrigen != null) {
            dibujarPuntoOrigen(g2, puntoOrigen, tamCelda, offsetX, offsetY);
        }

        // 4. Elementos desconocidos del universo ("?")
        dibujarElementosDesconocidos(g2, tablero.verTablero(), tamCelda, offsetX, offsetY);

        // 5. Objetivo de la misión
        if (tablero.getObjetivo() != null) {
            dibujarObjetivo(g2, tablero.getObjetivo(), tamCelda, offsetX, offsetY);
        }

        // 6. Nave en movimiento (siempre arriba de todo)
        if (tablero.getPosicionNave() != null) {
            dibujarNave(g2, tablero.getPosicionNave(), tamCelda, offsetX, offsetY);
        }

        // 7. Leyenda inferior legible para proyector
        dibujarLeyenda(g2, getWidth(), getHeight() - 14);
    }

    private void dibujarEstadoSinTablero(Graphics2D g2) {
        g2.setColor(new Color(170, 185, 215));
        g2.setFont(new Font("SansSerif", Font.BOLD, 17));
        String msg = "Sin tablero activo — Inicie una misión o vincule un tablero";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
    }

    private void dibujarGrilla(Graphics2D g2, int filas, int columnas, int tam, int ox, int oy) {
        g2.setColor(COLOR_GRILLA);
        g2.setStroke(new BasicStroke(1f));
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                g2.drawRect(ox + c * tam, oy + f * tam, tam, tam);
            }
        }
        g2.setColor(COLOR_MARCO);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRect(ox, oy, columnas * tam, filas * tam);
    }

    private void dibujarRutaPredefinida(Graphics2D g2, int tam, int ox, int oy) {
        if (rutaPredefinida.isEmpty()) {
            return;
        }
        // Sombreado gris tenue sobre las casillas planificadas
        g2.setColor(COLOR_RUTA_CELDA);
        for (Posicion p : rutaPredefinida) {
            int px = ox + p.getY() * tam;
            int py = oy + p.getX() * tam;
            g2.fillRect(px + 2, py + 2, tam - 4, tam - 4);
        }

        // Trazo punteado gris conectando el recorrido previsto
        float[] patron = {6f, 6f};
        g2.setColor(COLOR_RUTA_LINEA);
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, patron, 0f));
        for (int i = 0; i < rutaPredefinida.size() - 1; i++) {
            Posicion a = rutaPredefinida.get(i);
            Posicion b = rutaPredefinida.get(i + 1);
            int x1 = ox + a.getY() * tam + tam / 2;
            int y1 = oy + a.getX() * tam + tam / 2;
            int x2 = ox + b.getY() * tam + tam / 2;
            int y2 = oy + b.getX() * tam + tam / 2;
            g2.drawLine(x1, y1, x2, y2);
        }
        g2.setStroke(new BasicStroke(1f));
    }

    private void dibujarPuntoOrigen(Graphics2D g2, Posicion origen, int tam, int ox, int oy) {
        int px = ox + origen.getY() * tam;
        int py = oy + origen.getX() * tam;
        int pad = Math.max(4, tam / 6);

        // Anillo celeste minimalista marcando el punto de partida
        g2.setColor(new Color(56, 189, 248, 45));
        g2.fillOval(px + pad, py + pad, tam - 2 * pad, tam - 2 * pad);

        g2.setColor(COLOR_ORIGEN);
        g2.setStroke(new BasicStroke(2.4f));
        g2.drawOval(px + pad, py + pad, tam - 2 * pad, tam - 2 * pad);

        // Punto central fijo
        int radioCentro = Math.max(3, tam / 10);
        g2.fillOval(px + tam / 2 - radioCentro, py + tam / 2 - radioCentro, radioCentro * 2, radioCentro * 2);
    }

    private void dibujarElementosDesconocidos(Graphics2D g2, ElementoDelUniverso[][] matriz, int tam, int ox, int oy) {
        int fontSize = Math.max(13, (int) (tam * 0.55));
        g2.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        FontMetrics fm = g2.getFontMetrics();

        for (int f = 0; f < matriz.length; f++) {
            for (int c = 0; c < matriz[f].length; c++) {
                if (matriz[f][c] != null) {
                    int px = ox + c * tam;
                    int py = oy + f * tam;
                    int pad = Math.max(4, tam / 7);
                    int diametro = tam - 2 * pad;

                    // Ficha circular oscura con borde violeta y signo "?"
                    g2.setColor(COLOR_DESCONOCIDO_BG);
                    g2.fillOval(px + pad, py + pad, diametro, diametro);

                    g2.setColor(COLOR_DESCONOCIDO_BORDE);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawOval(px + pad, py + pad, diametro, diametro);

                    String simbolo = "?";
                    g2.setColor(Color.WHITE);
                    int tx = px + (tam - fm.stringWidth(simbolo)) / 2;
                    int ty = py + (tam + fm.getAscent() - fm.getDescent()) / 2;
                    g2.drawString(simbolo, tx, ty);
                }
            }
        }
    }

    private void dibujarObjetivo(Graphics2D g2, Posicion obj, int tam, int ox, int oy) {
        int px = ox + obj.getY() * tam;
        int py = oy + obj.getX() * tam;
        int pad = Math.max(4, tam / 6);

        g2.setColor(new Color(250, 204, 21, 40));
        g2.fillOval(px + pad, py + pad, tam - 2 * pad, tam - 2 * pad);

        g2.setColor(COLOR_OBJETIVO);
        g2.setStroke(new BasicStroke(2.5f));
        g2.drawOval(px + pad, py + pad, tam - 2 * pad, tam - 2 * pad);
        g2.drawLine(px + tam / 2, py + 2, px + tam / 2, py + tam - 2);
        g2.drawLine(px + 2, py + tam / 2, px + tam - 2, py + tam / 2);
    }

    private void actualizarAnguloNave(Posicion posActual) {
        // 1. Prioridad: mirar hacia la próxima casilla de la ruta planificada
        if (rutaPredefinida != null && !rutaPredefinida.isEmpty()) {
            int idx = rutaPredefinida.indexOf(posActual);
            if (idx >= 0 && idx < rutaPredefinida.size() - 1) {
                Posicion siguiente = rutaPredefinida.get(idx + 1);
                calcularAngulo(posActual, siguiente);
                ultimaPosicionNave = posActual;
                return;
            }
        }
        // 2. Respaldo: si ya llegó al final o no hay ruta, orientar según el último movimiento
        if (ultimaPosicionNave != null && !ultimaPosicionNave.equals(posActual)) {
            calcularAngulo(ultimaPosicionNave, posActual);
        }
        ultimaPosicionNave = posActual;
    }

    private void calcularAngulo(Posicion desde, Posicion hacia) {
        int dxPantalla = hacia.getY() - desde.getY(); // Columnas -> eje X visual
        int dyPantalla = hacia.getX() - desde.getX(); // Filas    -> eje Y visual
        if (dxPantalla != 0 || dyPantalla != 0) {
            // + PI/2 porque el polígono base apunta hacia arriba (-PI/2)
            this.anguloNave = Math.atan2(dyPantalla, dxPantalla) + Math.PI / 2.0;
        }
    }

    private void dibujarNave(Graphics2D g2, Posicion pos, int tam, int ox, int oy) {
        actualizarAnguloNave(pos);

        int px = ox + pos.getY() * tam;
        int py = oy + pos.getX() * tam;
        int pad = Math.max(3, tam / 7);
        int cx = px + tam / 2;
        int cy = py + tam / 2;

        // Halo suave (sin rotar, es circular)
        g2.setColor(new Color(46, 213, 115, 55));
        g2.fillOval(px + 2, py + 2, tam - 4, tam - 4);

        // Guardar transformación, rotar sobre el centro de la celda y dibujar
        AffineTransform transformOriginal = g2.getTransform();
        g2.rotate(anguloNave, cx, cy);

        int[] xs = {cx, px + tam - pad, cx, px + pad};
        int[] ys = {py + pad, py + tam - pad, py + tam - pad - tam / 5, py + tam - pad};

        g2.setColor(COLOR_NAVE);
        g2.fillPolygon(xs, ys, 4);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1.8f));
        g2.drawPolygon(xs, ys, 4);

        // Restaurar para que lo que se dibuje después (ej. la leyenda) no quede girado
        g2.setTransform(transformOriginal);
    }

    private void dibujarLeyenda(Graphics2D g2, int w, int y) {
        g2.setFont(new Font("SansSerif", Font.BOLD, 13));
        String[] textos = {
                "▲ Nave",
                "◎ Origen",
                "⊕ Destino",
                "? Desconocido",
                "┄ Ruta prevista"
        };
        Color[] colores = {
                COLOR_NAVE,
                COLOR_ORIGEN,
                COLOR_OBJETIVO,
                COLOR_DESCONOCIDO_BORDE,
                new Color(195, 205, 225)
        };

        FontMetrics fm = g2.getFontMetrics();
        int separacion = 26;
        int anchoTotal = 0;
        for (String t : textos) {
            anchoTotal += fm.stringWidth(t) + separacion;
        }
        anchoTotal -= separacion;

        int x = Math.max(16, (w - anchoTotal) / 2);
        for (int i = 0; i < textos.length; i++) {
            g2.setColor(colores[i]);
            g2.drawString(textos[i], x, y);
            x += fm.stringWidth(textos[i]) + separacion;
        }
    }
}