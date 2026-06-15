public class MoveRecord {
    public final String playerName;
    public final int fromSquare;
    public final int toSquare;
    public final int diceRolled;
    public final String event;   // "", "SNAKE", "LADDER"

    public MoveRecord(String playerName, int fromSquare, int toSquare,
                      int diceRolled, String event) {
        this.playerName = playerName;
        this.fromSquare = fromSquare;
        this.toSquare   = toSquare;
        this.diceRolled = diceRolled;
        this.event      = event;
    }

    @Override
    public String toString() {
        String base = playerName + " rolled " + diceRolled
                + " | " + fromSquare + " → " + toSquare;
        if (!event.isEmpty()) base += "  [" + event + "]";
        return base;
    }
}
