import javax.swing.*;

public class ScoreBoard implements Observer {
    private Player p1, p2;
    private JLabel label;

    public ScoreBoard(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
        label = new JLabel("", SwingConstants.CENTER);
        label.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.BOLD, 24));
    }

    public JLabel getLabel() { return label; }

    @Override
    public void update() {
        label.setText("<html>" + p1.getName() + ": " + p1.getScore() + "<br>" +
                      p2.getName() + ": " + p2.getScore() + "</html>");
    }
}
