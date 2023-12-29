package src.algo;

import java.util.NoSuchElementException;
import java.util.Random;

public class ArrayQueue<E> implements Queue<E> {
    private static final int DECREASE_BOUND = 16;

    private transient Object[] es;
    private transient int capacity;
    private transient int size;
    private transient int head;
    private transient int tail;

    public ArrayQueue(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = Math.max(capacity, 1);
        this.size = 0;
        this.head = 0;
        this.tail = 0;
        this.es = new Object[this.capacity];
    }

    public static void main(String[] args) {
        Queue<Integer> integerQueue = new ArrayQueue<>(4);
        Random random = new Random(0L);
        for (int i = 0; i < 30; i++) {
            int value = random.nextInt(30);
            integerQueue.enqueue(value);
            System.out.print("enqueued: " + value + ": ");
            System.out.println(integerQueue);
        }
        while (!integerQueue.isEmpty()) {
            int dequeue = integerQueue.dequeue();
            System.out.print("dequeued: " + dequeue + ": ");
            System.out.println(integerQueue);
        }
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void enqueue(E e) {
        // increase capacity
        if (this.size == this.capacity) {
            int newCapacity = this.capacity * 2;
            // overflow check
            if (newCapacity < this.capacity) {
                newCapacity = Integer.MAX_VALUE;
            }
            if (newCapacity == this.capacity) {
                throw new RuntimeException("Queue is full.");
            }
            replace(newCapacity);
        }
        this.es[this.tail] = e;
        this.size += 1;
        this.tail = (this.tail + 1) % this.capacity;
    }

    @Override
    public E dequeue() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        @SuppressWarnings("unchecked")
        E ret = (E) this.es[this.head];
        this.es[this.head] = null; // faster GC
        this.size -= 1;
        this.head = (this.head + 1) % this.capacity;
        if ((this.size << 2) <= this.capacity) {
            int newCapacity = Math.max(this.capacity / 2, DECREASE_BOUND);
            if (newCapacity >= this.capacity) {
                return ret;
            }
            replace(this.capacity);
        }
        return ret;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peek() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return (E) this.es[this.head];
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < this.size;) {
            int index = (this.head + i) % this.capacity;
            builder.append(this.es[index]);
            if (++i != this.size) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    private void replace(int newCapacity) {
        int previousCapacity = this.es.length;
        Object[] newEs = new Object[newCapacity];
        for (int i = 0; i < this.size; i++) {
            int index = (this.head + i) % previousCapacity;
            newEs[i] = this.es[index];
            this.es[index] = null; // faster GC
        }
        this.capacity = newCapacity;
        this.es = newEs;
        this.head = 0;
        this.tail = this.size;
    }
}
