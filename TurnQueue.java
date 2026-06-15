public class TurnQueue {

    private static class Node {
        Player data;
        Node   next;
        Node(Player data) {
            this.data = data;
            this.next = null;
        }
    }private Node front; // dequeue end
    private Node rear;  // enqueue end
    private int  size;

    public TurnQueue() {
        front = rear = null;
        size  = 0;
    }
    // Enqueue player at rear — O(1)
    public void enqueue(Player player) {
        Node newNode = new Node(player);
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear       = newNode;
        }
        size++;
    }

    // Dequeue from front — O(1)
    public Player dequeue() {
        if (isEmpty()) return null;
        Player data = front.data;
        front = front.next;
        if (front == null) rear = null;
        size--;
        return data;
    }

    // Look at whose turn it is without removing
    public Player peek() {
        return isEmpty() ? null : front.data;
    }

    // After a turn: dequeue player and re-enqueue them at the back
    public void nextTurn() {
        Player current = dequeue();
        if (current != null) enqueue(current);
    }

    public boolean isEmpty() { return front == null; }
    public int getSize()     { return size; }
}
