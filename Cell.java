public abstract class Cell implements Cloneable {
    protected String symbol;
    protected int points;

    public String getSymbol() { return symbol; }
    public int getPoints() { return points; }

    @Override
    public Cell clone() {
        try { return (Cell) super.clone(); }
        catch (CloneNotSupportedException e) { return null; }
    }
}
