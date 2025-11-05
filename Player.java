public class Player {
    private String name;
    private int score = 0;

    private Player(PlayerBuilder builder) {
        this.name = builder.name;
    }

    public String getName() { return name; }
    public int getScore() { return score; }
    public void addScore(int points) { score += points; }

    public static class PlayerBuilder {
        private String name;
        public PlayerBuilder setName(String name) { this.name = name; return this; }
        public Player build() { return new Player(this); }
    }
}
