import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private static final int INTERVAL = 8;
    private int actualSize;
    private Item[] arr;

    // construct an empty randomized queue
    public RandomizedQueue() {
        arr = (Item[]) new Object[INTERVAL];
    }

    // unit testing (required)
    public static void main(String[] args) {

    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return actualSize == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return actualSize;
    }

    // add the item
    public void enqueue(Item item) {
        validateItem(item);
        if (!hasSpace()) {
            resizeUp();
        }
        arr[actualSize++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        validateNotEmpty();
        int idx = getRandomIdx();
        Item item = this.arr[idx];
        shiftLeft(idx, actualSize);
        actualSize--;
        // TODO : Is resizeDown needed?
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        validateNotEmpty();
        return this.arr[getRandomIdx()];
    }

    private int getRandomIdx() {
        return size() == 0 ? 0 : StdRandom.uniform(actualSize);
    }

    private void resizeUp() {
        Item[] newArr = (Item[]) new Object[actualSize + INTERVAL];
        copyArr(this.arr, newArr, actualSize);
        this.arr = newArr;
    }

    private void shiftLeft(int from, int range) {
        if (range - 1 - from >= 0) {
            System.arraycopy(this.arr, from + 1, this.arr, from, range - 1 - from);
        }
        this.arr[range - 1] = null;
    }

    private void copyArr(Item[] src, Item[] dest, int range) {
        if (range >= 0) {
            System.arraycopy(src, 0, dest, 0, range);
        }
    }

    private void validateItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Element cannot be null");
        }
    }

    private void validateNotEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("Q is empty");
        }
    }

    private boolean hasSpace() {
        return actualSize < INTERVAL - 1;
    }

    // return an independent iterator over items in random order
    @Override
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator(createRandomArr());
    }

    private Item[] createRandomArr() {
        Item[] randomisedArr = (Item[]) new Object[actualSize];
        for (int i = 0; i < actualSize; i++) {
            int idx = getRandomIdx(); // Duplicate index?
            randomisedArr[i] = this.arr[idx];
        }
        return randomisedArr;
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private Item[] items;
        private int currentPos;

        public RandomizedQueueIterator(Item[] items) {
            this.items = items;
        }

        @Override
        public boolean hasNext() {
            return currentPos < items.length;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Q is empty");
            }
            return items[currentPos++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported");
        }
    }
}
