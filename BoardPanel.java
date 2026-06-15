import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
public class BoardPanel extends JPanel {
    private static final int CELL_SIZE = 70;
    private static final int GRID = 10;
    // DS #2: HashMap — snake head → tail, ladder bottom → top
    private Map<Integer, Integer> snakes  = new HashMap<>();
    private Map<Integer, Integer> ladders = new HashMap<>();

    private Player player1, player2;

    public BoardPanel(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
        setPreferredSize(new Dimension(CELL_SIZE * GRID, CELL_SIZE * GRID));

        // --- Populate HashMaps ---
        snakes.put(98, 12);
        snakes.put(88, 30);
        snakes.put(74, 53);
        snakes.put(62, 19);
        snakes.put(47, 16);

        ladders.put(4,  56);
        ladders.put(13, 46);
        ladders.put(33, 74);
        ladders.put(64, 90);
        ladders.put(72, 92);
    }

    // ---- Coordinate helpers ----
    private int getX(int square) {
        int col = (square - 1) % GRID;
        int row = (square - 1) / GRID;
        if (row % 2 == 1) col = 9 - col;
        return col * CELL_SIZE;
    }
    private int getY(int square) {
        int row = (square - 1) / GRID;
        return (GRID - 1 - row) * CELL_SIZE;
    }
    private int getCenterX(int sq) { return getX(sq) + CELL_SIZE / 2; }
    private int getCenterY(int sq) { return getY(sq) + CELL_SIZE / 2; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        drawCells(g2);
        drawLadders(g2);
        drawSnakes(g2);
        drawPlayers(g2);
    }

    private void drawCells(Graphics2D g) {
        Color light = new Color(255, 248, 220);
        Color dark  = new Color(144, 195, 144);
        for (int sq = 1; sq <= 100; sq++) {
            int x   = getX(sq);
            int y   = getY(sq);
            int row = (sq - 1) / GRID;
            int col = (sq - 1) % GRID;
            g.setColor((row + col) % 2 == 0 ? light : dark);
            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            g.setColor(Color.GRAY);
            g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            g.setColor(new Color(60, 60, 60));
            g.setFont(new Font("Arial", Font.BOLD, 11));
            g.drawString(String.valueOf(sq), x + 4, y + 14);
        }
    }

    private void drawLadders(Graphics2D g) {
        g.setColor(new Color(34, 139, 34));
        g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (Map.Entry<Integer, Integer> e : ladders.entrySet()) {
            int from = e.getKey(), to = e.getValue();
            int x1 = getCenterX(from), y1 = getCenterY(from);
            int x2 = getCenterX(to),   y2 = getCenterY(to);
            g.drawLine(x1 - 5, y1, x2 - 5, y2);
            g.drawLine(x1 + 5, y1, x2 + 5, y2);
            g.setStroke(new BasicStroke(3));
            int steps = 5;
            for (int i = 1; i <= steps; i++) {
                int rx1 = x1 - 5 + (x2 - x1 - 10) * i / (steps + 1);
                int ry1 = y1       + (y2 - y1)       * i / (steps + 1);
                int rx2 = x1 + 5 + (x2 - x1 + 10) * i / (steps + 1);
                g.drawLine(rx1, ry1, rx2, ry1);
            }
            g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(new Color(0, 100, 0));
            g.setFont(new Font("Arial", Font.BOLD, 10));
            g.drawString(from + "→" + to, Math.min(x1, x2), Math.min(y1, y2) - 4);
            g.setColor(new Color(34, 139, 34));
        }
        g.setStroke(new BasicStroke(1));
    }

    private void drawSnakes(Graphics2D g) {
        for (Map.Entry<Integer, Integer> e : snakes.entrySet()) {
            int head = e.getKey(), tail = e.getValue();
            int x1 = getCenterX(head), y1 = getCenterY(head);
            int x2 = getCenterX(tail), y2 = getCenterY(tail);
            g.setColor(new Color(200, 50, 50));
            g.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int cx = (x1 + x2) / 2 + 40, cy = (y1 + y2) / 2 - 40;
            java.awt.geom.QuadCurve2D curve =
                    new java.awt.geom.QuadCurve2D.Float(x1, y1, cx, cy, x2, y2);
            ((Graphics2D) g).draw(curve);
            g.setColor(new Color(180, 0, 0));
            g.fillOval(x1 - 10, y1 - 10, 20, 20);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 9));
            g.drawString("S", x1 - 4, y1 + 4);
            g.setColor(new Color(150, 0, 0));
            g.setFont(new Font("Arial", Font.BOLD, 10));
            g.drawString(head + "→" + tail,
                    Math.min(x1, x2) + 5, Math.max(y1, y2) + 14);
            g.setStroke(new BasicStroke(1));
        }
    }

    private void drawPlayers(Graphics2D g) {
        drawToken(g, player1, -12, 0);
        drawToken(g, player2,  12, 0);
    }

    private void drawToken(Graphics2D g, Player player, int offsetX, int offsetY) {
        int sq = player.getPosition();
        if (sq < 1 || sq > 100) return;
        int cx = getCenterX(sq) + offsetX;
        int cy = getCenterY(sq) + offsetY;
        g.setColor(new Color(0, 0, 0, 60));
        g.fillOval(cx - 13, cy - 11, 26, 26);
        g.setColor(player.getColor());
        g.fillOval(cx - 14, cy - 14, 28, 28);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2));
        g.drawOval(cx - 14, cy - 14, 28, 28);
        g.setFont(new Font("Arial", Font.BOLD, 13));
        g.drawString(player.getName().substring(0, 1).toUpperCase(), cx - 5, cy + 5);
    }
    public void refresh() { repaint(); }

    // O(1) lookup — used by GameFrame to check snakes/ladders after each move
    public Map<Integer, Integer> getSnakes()  { return snakes; }
    public Map<Integer, Integer> getLadders() { return ladders; }
}
