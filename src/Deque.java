import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int size;

    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    //Return the number of items in Deque
    public int size() {
        return size;
    }

    //Add an item to the front of the queue
    public void addFirst(Item item) throws IllegalArgumentException {
        if (item == null)
            throw new IllegalArgumentException("Cannot add an empty item.");

        Node newFirst = new Node();
        if (first == null) {
            newFirst.next = null;
        } else {
            newFirst.next = first;
            first.prev = newFirst;
        }

        newFirst.prev = null;
        first = newFirst;
        first.item = item;
        size++;

        if (last == null)
            last = first;
    }

    //Add item to the end of the Deque
    public void addLast(Item item) throws IllegalArgumentException {
        if (item == null)
            throw new IllegalArgumentException("Cannot add an an empty item.");

        Node newLast = new Node();
        if (last == null) {
            newLast.prev = null;
        } else {
            newLast.prev = last;
            last.next = newLast;
        }

        newLast.next = null;
        last = newLast;
        last.item = item;
        size++;

        if (first == null)
            first = last;
    }

    //Delete items starting from the front of the Deque
    public Item removeFirst() throws NoSuchElementException {
        if (isEmpty())
            throw new NoSuchElementException("Cannot remove element from an empty Deque");

        Item returnItem = first.item;
        if (size == 1) {
            first = null;
            last = null;
        } else {
            first = first.next;
            first.prev = null;
        }

        size--;
        return returnItem;
    }

    //Delete and return item at the end of the Deque
    public Item removeLast() throws NoSuchElementException {
        if (isEmpty())
            throw new NoSuchElementException("Cannot remove element from an empty Deque. ");

        Item returnItem = last.item;
        if (size == 1) {
            first = null;
            last = null;
        } else {
            last = last.next;
            last.next = null;
        }
        size--;
        return returnItem;
    }

    @Override
    public Iterator<Item> iterator() throws NoSuchElementException {

        return new DequeIterator();
    }

    private class Node {
        Node prev = null;
        Node next = null;
        Item item = null;

        //Default constructor
        public Node() {

        }

        public Node(Node that) {
            this.prev = that.prev;
            this.next = that.next;
            this.item = that.item;
        }

    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() throws NoSuchElementException {
            if (!hasNext())
                throw new NoSuchElementException("No more items in Deque");
            Item item = current.item;
            current = current.next;

            return item;
        }

        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Function remove() is not supported");
        }
    }
}
