import java.util.Random;

public class CellFactory {
    private static final GemCell gemPrototype = new GemCell();
    private static final BonusCell bonusPrototype = new BonusCell();
    private static final TrapCell trapPrototype = new TrapCell();
    private static final EmptyCell emptyPrototype = new EmptyCell();

    public static Cell createCell() {
        Random rand = new Random();
        int choice = rand.nextInt(100);
        if (choice < 40) return gemPrototype.clone();
        else if (choice < 55) return bonusPrototype.clone();
        else if (choice < 70) return trapPrototype.clone();
        else return emptyPrototype.clone();
    }
}
