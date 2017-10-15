import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int size;

    public RandomizedQueue() {
        if(items instanceof Object[])
            items = (Item[]) new Object[1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) throws IllegalArgumentException {
        if (item == null)
            throw new IllegalArgumentException("Cannot add null Item");

        if (items.length == size) {
            resize(items.length * 2);
            items[size] = item;
            size++;
        } else {
            items[size] = item;
            size++;
        }
    }

    public Item dequeue() throws NoSuchElementException {
        if (isEmpty())
            throw new NoSuchElementException("Cannot dequeue from an empty RandomizedQueue");
        int randomIndex = StdRandom.uniform(size);
        Item returnItem = items[randomIndex];
        if (size - 1 == randomIndex) {
            items[randomIndex] = null;
        } else {
            items[randomIndex] = items[size - 1];
            items[size - 1] = null;
        }
        if (size == items.length / 4) {
            resize(items.length / 2);
        }
        size--;
        return returnItem;
    }

    public Item sample() throws NoSuchElementException {
        if (isEmpty())
            throw new NoSuchElementException("Cannot sample an empty RandomizedQueue");

        return items[StdRandom.uniform(size)];

    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++)
            copy[i] = items[i];
        items = copy;
    }
    
    @Override
    public Iterator<Item> iterator() {
        return new RandomQueueIterator();
    }

    private class RandomQueueIterator implements Iterator<Item> {
        private int i = 0;
        private int[] indices;

        public RandomQueueIterator() {
            indices = new int[size];
            for (int j = 0; j < indices.length; j++)
                indices[j] = j;

            StdRandom.shuffle(indices);
        }
        @Override
        public boolean hasNext() {
            return i < size;
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("No more items in iteration.");

            return items[indices[i++]];
        }

        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("remove() operation is not supported");
        }
    }
}
