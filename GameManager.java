import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameManager implements Subject {
    private static GameManager instance;
    private Player player1, player2, currentPlayer;
    private Cell[][] grid = new Cell[4][4];
    private JButton[][] buttons = new JButton[4][4];
    private JLabel turnLabel;
    private boolean gameOver = false;

    private List<Observer> observers = new ArrayList<>();

    private GameManager() {}

    public static GameManager getInstance() {
        if (instance == null) instance = new GameManager();
        return instance;
    }

    // Subject methods
    @Override
    public void registerObserver(Observer o) { observers.add(o); }
    @Override
    public void removeObserver(Observer o) { observers.remove(o); }
    @Override
    public void notifyObservers() {
        for (Observer o : observers) o.update();
    }

    public void startGame() {
        setupPlayers();
        createGrid();
        createGUI();
    }

    private void setupPlayers() {
        String p1Name = JOptionPane.showInputDialog("Enter Player 1 name:");
        if (p1Name == null || p1Name.isBlank()) p1Name = "Player1";

        String p2Name = JOptionPane.showInputDialog("Enter Player 2 name:");
        if (p2Name == null || p2Name.isBlank()) p2Name = "Player2";

        player1 = new Player.PlayerBuilder().setName(p1Name).build();
        player2 = new Player.PlayerBuilder().setName(p2Name).build();
        currentPlayer = player1;
    }

    private void createGrid() {
        for (int r = 0; r < 4; r++)
            for (int c = 0; c < 4; c++)
                grid[r][c] = CellFactory.createCell();
    }

    private void createGUI() {
        JFrame frame = new JFrame("💎 Gem Collector");
        frame.setSize(600, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Top panel
        JPanel topPanel = new JPanel(new GridLayout(2,1));
        turnLabel = new JLabel(currentPlayer.getName() + "'s turn", SwingConstants.CENTER);
        turnLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 36));
        turnLabel.setOpaque(true);
        turnLabel.setBackground(new Color(255,228,225));
        topPanel.add(turnLabel);

        // Scoreboard as Observer
        ScoreBoard scoreBoard = new ScoreBoard(player1, player2);
        registerObserver(scoreBoard);
        topPanel.add(scoreBoard.getLabel());
        notifyObservers(); // initial

        frame.add(topPanel, BorderLayout.NORTH);

        // Grid
        JPanel gridPanel = new JPanel(new GridLayout(4,4,5,5));
        gridPanel.setBackground(new Color(255,228,225));
        for (int r=0;r<4;r++){
            for (int c=0;c<4;c++){
                JButton btn = new JButton("❓");
                btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
                int row = r, col = c;
                btn.addActionListener(e -> clickCell(row,col));
                buttons[r][c] = btn;
                gridPanel.add(btn);
            }
        }
        frame.add(gridPanel, BorderLayout.CENTER);

        // Restart button
        JPanel bottomPanel = new JPanel();
        JButton restartBtn = new JButton("Restart");
        restartBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        restartBtn.addActionListener(e -> restartGame(frame));
        bottomPanel.add(restartBtn);
        bottomPanel.setBackground(new Color(255,228,225));
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void clickCell(int r, int c) {
        if (gameOver || !buttons[r][c].getText().equals("❓")) return;

        Cell cell = grid[r][c];
        buttons[r][c].setText(cell.getSymbol());
        currentPlayer.addScore(cell.getPoints());

        notifyObservers(); // Observer pattern

        String msg = "";
        if (cell instanceof TrapCell) msg = " hit a Trap!";
        else if (cell instanceof BonusCell) msg = " got a Bonus! Extra turn!";
        else if (cell instanceof GemCell) msg = " collected a Gem!";
        else msg = " found nothing.";
        JOptionPane.showMessageDialog(null, currentPlayer.getName() + msg);

        if (!(cell instanceof BonusCell)) swapPlayer();
        checkGameOver();
    }

    private void swapPlayer() {
        currentPlayer = (currentPlayer == player1)? player2: player1;
        if (!gameOver) turnLabel.setText(currentPlayer.getName() + "'s turn");
    }

    private void checkGameOver() {
        boolean finished = true;
        for (int r=0;r<4;r++)
            for (int c=0;c<4;c++)
                if (buttons[r][c].getText().equals("❓")) finished = false;

        if (finished){
            gameOver = true;
            String winner;
            if (player1.getScore() > player2.getScore()) winner = player1.getName()+" wins!";
            else if (player2.getScore() > player1.getScore()) winner = player2.getName()+" wins!";
            else winner = "Tie!";
            turnLabel.setText(winner);
        }
    }

    private void restartGame(JFrame frame){
        frame.dispose();
        instance = null;
        getInstance().startGame();
    }
}
