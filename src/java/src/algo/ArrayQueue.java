package src.algo;

import java.util.NoSuchElementException;
import java.util.Random;

public class ArrayQueue<E> implements Queue<E> {
    protected static final int DECREASE_BOUND = 16;

    protected Object[] es;
    protected int capacity;
    protected int size;
    protected int head;
    protected int tail;

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
            increase();
        }
        this.es[this.tail] = e;
        this.size++;
        this.tail = next(this.tail);
    }

    @Override
    public E dequeue() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        @SuppressWarnings("unchecked")
        E ret = (E) this.es[this.head];
        this.es[this.head] = null; // faster GC
        this.size--;
        this.head = next(this.head);
        if ((this.size << 2) <= this.capacity) {
            decrease();
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
        for (int i = 0; i < this.size; ) {
            int index = next(this.head, i);
            builder.append(this.es[index]);
            if (++i != this.size) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    protected int next(int index) {
        return next(index, 1);
    }

    protected int next(int index, int offset) {
        return (index + offset) % this.capacity;
    }

    protected void increase() {
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

    protected void decrease() {
        int newCapacity = Math.max(this.capacity / 2, DECREASE_BOUND);
        if (newCapacity >= this.capacity) {
            return;
        }
        replace(this.capacity);
    }

    protected void replace(int newCapacity) {
        Object[] newEs = new Object[newCapacity];
        for (int i = 0; i < this.size; i++) {
            int index = next(this.head, i);
            newEs[i] = this.es[index];
            this.es[index] = null; // faster GC
        }
        this.capacity = newCapacity;
        this.es = newEs;
        this.head = 0;
        this.tail = this.size;
    }
}
