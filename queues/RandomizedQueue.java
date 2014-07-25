
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size;
    private Item[] array;
    private class MyIterator implements Iterator<Item> {
        private int currentPos = 0;
        private Item[] randomizedArray;
        public MyIterator(Item[] array) {
            randomizedArray = (Item[]) new Object[size()];
            for (int i = 0; i < size(); i++) {
                randomizedArray[i] = array[i];
            }
            StdRandom.shuffle(randomizedArray);
        }
        public boolean hasNext() {
            return currentPos != size();
        }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return randomizedArray[currentPos++];
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }   
    }
    private void resize(int newCapacity) {
        Item[] oldArray = array;
        array = (Item[]) new Object[newCapacity];
        for (int i = 0; i < size(); i++) {
            array[i] = oldArray[i];
        }
    }
    public RandomizedQueue() {
        // construct an empty randomized queue
        array = (Item[]) new Object[1];
    }
    public boolean isEmpty() {
        // is the queue empty?
        return 0 == size;
    }
    public int size() {
        // return the number of items on the queue
        return size;
    }
    public void enqueue(Item item) {
        // add the item
        if (item == null) throw new NullPointerException();
        if (size() == array.length) resize(2 * array.length);
        array[size++] = item;
    }
    public Item dequeue() {
        // delete and return a random item
        if (size() == 0) throw new NoSuchElementException();
        int index = StdRandom.uniform(0, size);
        Item tmp = array[index];
        array[index] = array[--size];
        array[size] = null;
        if (size > 0 && size == array.length / 4) resize(array.length / 2);
        return tmp;
    }
    public Item sample() {
        // return (but do not delete) a random item
        if (size() == 0) throw new NoSuchElementException();
        return array[StdRandom.uniform(0, size)];
    }
    
    public Iterator<Item> iterator() {
        // return an independent iterator over items in random order
        return new MyIterator(array);
    }
    public static void main(String[] args) {
        // unit testing
    }
 }
