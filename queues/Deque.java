
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first, last;
    private int size;
    private static class Node<Item> {
        private Item item;
        private Node<Item> prev;
        private Node<Item> next;
        public Node(Item item, Node<Item> prevNode, Node<Item> nextNode) {
            this.item = item;
            prev = prevNode;
            next = nextNode;
        }
    }
    private class MyIterator implements Iterator<Item> {
        private Node<Item> currentPos;
        public MyIterator(Node<Item> node) {
            currentPos = node;
        }
        public boolean hasNext() {
            return currentPos != null;
        }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = currentPos.item;
            currentPos = currentPos.next;
            return item;
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    public Deque() {
        // construct an empty deque
        first = null;
    }
    public boolean isEmpty() {
        // is the deque empty?
        return first == null;
    }
    public int size() {
        // return the number of items on the deque
        return size;
    }
    public void addFirst(Item item) {
        // insert the item at the front
        if (item == null) throw new NullPointerException();
        if (size++ == 0) {
            first = new Node<Item>(item, null, null);
            last = first;
        }
        else {
            first = new Node<Item>(item, null, first);
            first.next.prev = first;
        }
    }
    public void addLast(Item item) {
        // insert the item at the end
        if (item == null) throw new NullPointerException();
        if (size++ == 0) {
            last = new Node<Item>(item, null, null);
            first = last;
        }
        else {
            last = new Node<Item>(item, last, null);
            last.prev.next = last;
        }
    }
    public Item removeFirst() {
        // delete and return the item at the front
        if (0 == size) throw new NoSuchElementException();
        Item item = first.item;
        first = first.next;
        if (--size != 0) first.prev = null;
        else {
            last = first;
        }
        return item;
    }
    public Item removeLast() {
        // delete and return the item at the end
        if (0 == size) throw new NoSuchElementException();
        Item item = last.item;
        last = last.prev;
        if (--size != 0) last.next = null;
        else {
            first = last;
        }
        return item;
    }
    public Iterator<Item> iterator() {
        // return an iterator over items in order from front to end
        return new MyIterator(first);
    }
    public static void main(String[] args) {
        // unit testing
    }
 }
