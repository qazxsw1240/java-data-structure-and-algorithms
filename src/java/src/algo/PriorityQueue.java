package src.algo;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class PriorityQueue<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 16;

    private final Comparator<? super E> comparator;
    private final Vector<E> vector;

    public PriorityQueue(Comparator<? super E> comparator) {
        this.comparator = comparator;
        this.vector = new ArrayVector<>(DEFAULT_CAPACITY);
    }

    public static void main(String[] args) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(Comparator.naturalOrder());
        Random random = new Random(0L);
        for (int i = 0; i < 20; i++) {
            priorityQueue.enqueue(random.nextInt(30));
        }
        System.out.println("----------------");
        while (!priorityQueue.isEmpty()) {
            int top = priorityQueue.dequeue();
            System.out.println("top is " + top);
        }
    }

    @Override
    public boolean isEmpty() {
        return this.vector.isEmpty();
    }

    @Override
    public int size() {
        return this.vector.size();
    }

    @Override
    public void enqueue(E e) {
        int size = this.vector.size();
        this.vector.addLast(e);
        siftUp(size);
    }

    @Override
    public E dequeue() {
        if (this.vector.isEmpty()) {
            throw new NoSuchElementException();
        }
        int top = this.vector.size() - 1;
        swap(0, top);
        E topElement = this.vector.removeLast();
        siftDown(top - 1);
        return topElement;
    }

    @Override
    public E peek() {
        if (this.vector.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.vector.get(0);
    }

    @Override
    public Iterator<E> iterator() {
        return this.vector.iterator();
    }

    private void siftUp(int size) {
        int current = size;
        while (current != 0) {
            int parent = (current - 1) / 2;
            E currentItem = this.vector.get(current);
            E parentItem = this.vector.get(parent);
            if (this.comparator.compare(currentItem, parentItem) >= 0) {
                break;
            }
            swap(current, parent);
            current = parent;
        }
    }

    private void siftDown(int size) {
        int current = 0;
        while (current <= size) {
            int child = current * 2 + 1;
            if (child > size) {
                break;
            }
            E currentItem = this.vector.get(current);
            E childItem = this.vector.get(child);
            if (child + 1 <= size) {
                int nextChild = child + 1;
                E nextChildItem = this.vector.get(nextChild);
                if (this.comparator.compare(nextChildItem, childItem) < 0) {
                    child = nextChild;
                    childItem = nextChildItem;
                }
            }
            if (this.comparator.compare(currentItem, childItem) <= 0) {
                break;
            }
            swap(current, child);
            current = child;
        }
    }

    private void swap(int i, int j) {
        if (i == j) {
            return;
        }
        E itemI = this.vector.get(i);
        E itemJ = this.vector.get(j);
        this.vector.set(i, itemJ);
        this.vector.set(j, itemI);
    }
}
