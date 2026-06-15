import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GameFrame extends JFrame {

    // ── Players
    private Player player1, player2;

    // ── Board (Array inside) ─ DS #1
    private Board board = new Board();

    // ── Panels
    private BoardPanel boardPanel;

    // ── DS #3: Stack — move history for UNDO
    private MoveHistory moveHistory = new MoveHistory();

    // ── DS #4: Queue — round-robin turn management
    private TurnQueue turnQueue = new TurnQueue();

    // ── DS #5: LinkedList — full game event log
    private GameEventLog eventLog = new GameEventLog();

    // ── UI components
    private JLabel  statusLabel, p1Label, p2Label, turnLabel;
    private JButton rollButton, undoButton, restartButton, logButton;
    private JTextArea logArea;

    private Random  random   = new Random();
    private boolean gameOver = false;

    // Game Frame
    public GameFrame() {
        player1 = new Player("Player 1", new Color(220, 50, 50));
        player2 = new Player("Player 2", new Color(50, 100, 220));

        // DS #4: enqueue both players — P1 goes first (FIFO)
        turnQueue.enqueue(player1);
        turnQueue.enqueue(player2);

        setupWindow();
        setupUI();
    }

    // ── Window setup
    private void setupWindow() {
        setTitle("Snake & Ladder — DSA Project (5 DS)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(30, 30, 50));
    }

    // ── UI layout
    private void setupUI() {
        boardPanel = new BoardPanel(player1, player2);
        boardPanel.setBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 200), 3));
        add(boardPanel, BorderLayout.CENTER);

        // ── Right panel
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBackground(new Color(40, 40, 65));
        right.setPreferredSize(new Dimension(220, 700));
        right.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        // Title
        JLabel title = new JLabel(" Snake & Ladder");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 15));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        right.add(title);

        // DS labels
        JLabel dsLabel = new JLabel(""
                + "DS: Array · HashMap · Stack · Queue · LinkedList"
                );
        dsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        right.add(Box.createVerticalStrut(4));
        right.add(dsLabel);
        right.add(Box.createVerticalStrut(12));

        // Player info
        p1Label = makeLabel(" " + player1.getName() + ": Sq 1", player1.getColor());
        p2Label = makeLabel("" + player2.getName() + ": Sq 1", player2.getColor());
        right.add(p1Label);
        right.add(Box.createVerticalStrut(6));
        right.add(p2Label);
        right.add(Box.createVerticalStrut(10));

        // Turn indicator (Queue peek)
        turnLabel = makeLabel("▶ Player 1's Turn", Color.YELLOW);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 13));
        right.add(turnLabel);

        // Status
        statusLabel = makeLabel("Roll to start!", Color.LIGHT_GRAY);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        right.add(Box.createVerticalStrut(4));
        right.add(statusLabel);
        right.add(Box.createVerticalStrut(14));

        // Roll button
        rollButton = new JButton(" Roll Dice");
        styleButton(rollButton, new Color(70, 130, 180));
        rollButton.addActionListener(e -> rollDice());
        rollButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        right.add(rollButton);
        right.add(Box.createVerticalStrut(8));

        // Undo button (uses Stack)
        undoButton = new JButton(" Undo Last Move");
        styleButton(undoButton, new Color(160, 90, 30));
        undoButton.addActionListener(e -> undoMove());
        undoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        right.add(undoButton);
        right.add(Box.createVerticalStrut(8));

        // Show full log button (uses LinkedList)
        logButton = new JButton("  Full Event Log");
        styleButton(logButton, new Color(60, 120, 100));
        logButton.addActionListener(e -> showFullLog());
        logButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        right.add(logButton);
        right.add(Box.createVerticalStrut(8));

        // Restart
        restartButton = new JButton(" Restart");
        styleButton(restartButton, new Color(100, 100, 100));
        restartButton.addActionListener(e -> restartGame());
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        right.add(restartButton);

        // Live log area (last 5 moves from LinkedList)
        right.add(Box.createVerticalStrut(14));
        JLabel logTitle = makeLabel("— Recent Moves —", new Color(180, 180, 255));
        logTitle.setFont(new Font("Arial", Font.BOLD, 11));
        right.add(logTitle);
        right.add(Box.createVerticalStrut(4));

        logArea = new JTextArea(8, 18);
        logArea.setEditable(false);
        logArea.setBackground(new Color(20, 20, 40));
        logArea.setForeground(new Color(200, 200, 255));
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setMaximumSize(new Dimension(200, 160));
        scroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        right.add(scroll);

        // Legend
        right.add(Box.createVerticalStrut(12));
        right.add(makeLabel(" Ladder = Go UP",   new Color(100, 220, 100)));
        right.add(Box.createVerticalStrut(4));
        right.add(makeLabel(" Snake  = Go DOWN", new Color(220, 100, 100)));

        add(right, BorderLayout.EAST);
        pack();
        setLocationRelativeTo(null);
    }

    // ── Roll Dice
    private void rollDice() {
        if (gameOver) return;

        // DS #4: Queue — peek at who's turn it is
        Player current = turnQueue.peek();
        int dice       = random.nextInt(6) + 1;
        int oldPos     = current.getPosition();
        int newPos     = oldPos + dice;

        // DS #1: Array — validate using Board
        if (!board.isValidPosition(newPos) || newPos > 100) {
            statusLabel.setText(current.getName() + " rolled " + dice + " — can't move!");
            // Still rotate turn
            turnQueue.nextTurn();
            turnLabel.setText(" " + turnQueue.peek().getName() + "'s Turn");
            return;
        }

        current.setPosition(newPos);
        String event = "";

        // DS #2: HashMap — O(1) snake/ladder check
        if (boardPanel.getSnakes().containsKey(newPos)) {
            int snakeTail = boardPanel.getSnakes().get(newPos);
            current.setPosition(snakeTail);
            event = "SNAKE → " + snakeTail;
        } else if (boardPanel.getLadders().containsKey(newPos)) {
            int ladderTop = boardPanel.getLadders().get(newPos);
            current.setPosition(ladderTop);
            event = "LADDER → " + ladderTop;
        }

        MoveRecord record = new MoveRecord(
                current.getName(), oldPos, current.getPosition(), dice, event);

        // DS #3: Stack — push this move (enables UNDO)
        moveHistory.push(record);

        // DS #5: LinkedList — append to full event log
        eventLog.addEvent(record);

        // Update UI
        p1Label.setText(" " + player1.getName() + ": Sq " + player1.getPosition());
        p2Label.setText(" " + player2.getName() + ": Sq " + player2.getPosition());
        statusLabel.setText(record.toString());
        updateLogArea();
        boardPanel.refresh();

        // Win check
        if (current.getPosition() >= 100) {
            gameOver = true;
            rollButton.setEnabled(false);
            JOptionPane.showMessageDialog(this,
                    " " + current.getName() + " WINS!\n\nTotal moves: " + eventLog.getSize(),
                    "Game Over", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // DS #4: Queue — move to next turn
        turnQueue.nextTurn();
        turnLabel.setText(" " + turnQueue.peek().getName() + "'s Turn");
    }

    // ── Undo (Stack pop)
    private void undoMove() {
        if (gameOver) return;

        // DS #3: Stack — pop last move
        MoveRecord last = moveHistory.pop();
        if (last == null) {
            JOptionPane.showMessageDialog(this,
                    "No moves to undo!", "Undo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Restore player position
        Player toUndo = last.playerName.equals(player1.getName()) ? player1 : player2;
        toUndo.setPosition(last.fromSquare);

        turnQueue.nextTurn();  // undo the advance
        turnLabel.setText(" " + turnQueue.peek().getName() + "'s Turn");

        p1Label.setText("" + player1.getName() + ": Sq " + player1.getPosition());
        p2Label.setText(" " + player2.getName() + ": Sq " + player2.getPosition());
        statusLabel.setText("Undid: " + last);
        updateLogArea();
        boardPanel.refresh();
    }

    // ── Show full LinkedList log in dialog
    private void showFullLog() {
        // DS #5: LinkedList — traverse and display all nodes
        String log = eventLog.getFullLog();
        JTextArea area = new JTextArea(log);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane sp = new JScrollPane(area);
        sp.setPreferredSize(new Dimension(420, 300));
        JOptionPane.showMessageDialog(this, sp,
                "Full Game Log (LinkedList — " + eventLog.getSize() + " events)",
                JOptionPane.PLAIN_MESSAGE);
    }

    // ── Update live log area (last 5 from LinkedList)
    private void updateLogArea() {
        logArea.setText(eventLog.getLastN(5));
    }

    // ── Restart
    private void restartGame() {
        player1.setPosition(1);
        player2.setPosition(1);
        gameOver = false;
        rollButton.setEnabled(true);

        // Reset all DS
        moveHistory = new MoveHistory();    // clear Stack
        eventLog    = new GameEventLog();   // clear LinkedList
        turnQueue   = new TurnQueue();      // rebuild Queue
        turnQueue.enqueue(player1);
        turnQueue.enqueue(player2);

        turnLabel.setText(" Player 1's Turn");
        statusLabel.setText("Roll to start!");
        p1Label.setText(" " + player1.getName() + ": Sq 1");
        p2Label.setText("" + player2.getName() + ": Sq 1");
        logArea.setText("");
        boardPanel.refresh();
    }

    // ── Helpers
    private JLabel makeLabel(String text, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(color);
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(180, 38));
    }
}
