public class MoveHistory {
    private static class Node {
        MoveRecord data;
        Node       next;
        Node(MoveRecord data, Node next) {
            this.data = data;
            this.next = next;
        }
    }private Node top;   // top of stack
    private int  size;

    public MoveHistory() {
        top  = null;
        size = 0;
    }
    public void push(MoveRecord record) {
        top = new Node(record, top);
        size++;
    }
    // Pop last move off top — O(1) — returns null if empty
    public MoveRecord pop() {
        if (isEmpty()) return null;
        MoveRecord data = top.data;
        top = top.next;
        size--;
        return data;
    }

    // Peek without removing
    public MoveRecord peek() {
        return isEmpty() ? null : top.data;
    }

    public boolean isEmpty() { return top == null; }
    public int getSize()     { return size; }
}
