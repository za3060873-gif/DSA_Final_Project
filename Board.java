public class Board {
    private static final int SIZE = 100;

    // DS #1: Array — holds each square's "base" number
    private int[] squares;

    public Board() {
        squares = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            squares[i] = i + 1;   // square[0] = 1, square[99] = 100
        }
    }public boolean isValidPosition(int position) {
        return position >= 1 && position <= SIZE;
    }

    // O(1) access — direct index lookup
    public int getSquare(int position) {
        return squares[position - 1];
    }

    public int getSize() { return SIZE; }
}
