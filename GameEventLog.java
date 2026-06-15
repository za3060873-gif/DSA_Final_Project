public class GameEventLog {

    // Inner Node class — building block of our LinkedList
    private static class Node {
        MoveRecord data;
        Node next;
        Node(MoveRecord data) {
            this.data = data;
            this.next = null;
        }
    }
    private Node head;   // first event
    private Node tail;   // last  event (for O(1) append)
    private int  size;

    public GameEventLog() {
        head = null;
        tail = null;
        size = 0;
    }

    // Append new event at the end — O(1)
    public void addEvent(MoveRecord record) {
        Node newNode = new Node(record);
        if (tail == null) {          // list is empty
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail       = newNode;
        }
        size++;
    }

    // Traverse list and return all events as a formatted String
    public String getFullLog() {
        if (head == null) return "No moves yet.";
        StringBuilder sb = new StringBuilder();
        Node current = head;
        int  num     = 1;
        while (current != null) {
            sb.append(num++).append(". ").append(current.data).append("\n");
            current = current.next;
        }
        return sb.toString();
    }

    public int getSize() { return size; }

    // Get last N events (for the live log panel — no need to show all 100)
    public String getLastN(int n) {
        if (head == null) return "No moves yet.";

        // Collect all in array for reverse-slice (LinkedList has no index)
        MoveRecord[] all = new MoveRecord[size];
        Node cur = head;
        int i = 0;
        while (cur != null) { all[i++] = cur.data; cur = cur.next; }

        int from = Math.max(0, size - n);
        StringBuilder sb = new StringBuilder();
        for (int j = from; j < size; j++) {
            sb.append("• ").append(all[j]).append("\n");
        }
        return sb.toString();
    }
}
