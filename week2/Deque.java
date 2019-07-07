import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int size;

    // construct an empty deque
    public Deque() {

    }

    // unit testing (required)
    public static void main(String[] args) {

    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        validateItem(item);
        if (first == null) {
            addFirstNode(item);
        } else {
            Node itemNode = new Node(item);
            itemNode.next = first;
            first.prev = itemNode;
            first = itemNode;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        validateItem(item);
        if (last == null) {
            addFirstNode(item);
        } else {
            Node itemNode = new Node(item);
            last.next = itemNode;
            itemNode.prev = last;
            last = itemNode;
        }
        size++;
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

    private void addFirstNode(Item item) {
        first = new Node(item);
        last = first;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        validateNotEmpty();
        Node itemNode = first;
        first = itemNode.next;
        itemNode.next = null;
        if (first == null) {
            last = null;
        } else {
            first.prev = null;
        }
        size--;
        return itemNode.data;
    }

    // remove and return the item from the back
    public Item removeLast() {
        validateNotEmpty();
        Node itemNode = last;
        last = itemNode.prev;
        itemNode.prev = null;
        if (last == null) {
            first = null;
        } else {
            last.next = null;
        }
        size--;
        return itemNode.data;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DeQueueIterator();
    }

    private class DeQueueIterator implements Iterator<Item> {

        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Q is empty");
            }
            Node item = current;
            current = current.next;
            return item.data;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported");
        }
    }

    private class Node {
        Item data;
        Node next;
        Node prev;

        Node(Item data) {
            this.data = data;
        }
    }
}
