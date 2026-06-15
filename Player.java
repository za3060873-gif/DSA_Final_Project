import java.awt.Color;

public class Player {
    private String name;
    private int position;
    private Color color;
    public Player(String name, Color color) {
        this.name = name;
        this.position = 1;
        this.color = color;
    }
    public String getName()          { return name; }
    public int getPosition()         { return position; }
    public Color getColor()          { return color; }
    public void setPosition(int pos) { this.position = pos; }

    public void move(int diceValue)  { this.position += diceValue; }

    @Override
    public String toString() {
        return name + " is at square " + position;
    }
}
