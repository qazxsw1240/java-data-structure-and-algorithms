package src.algo;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class LinkedQueue<E> implements Queue<E> {
    protected final DoublyLinkedList<E> list;

    public LinkedQueue() {
        this.list = new DoublyLinkedList<>();
    }

    public static void main(String[] args) {
        Queue<Integer> integerQueue = new LinkedQueue<>();
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
        return this.list.isEmpty();
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    public void enqueue(E e) {
        this.list.addLast(e);
    }

    @Override
    public E dequeue() {
        if (this.list.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.list.removeFirst();
    }

    @Override
    public E peek() {
        if (this.list.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.list.get(0);
    }

    @Override
    public String toString() {
        return this.list.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return this.list.iterator();
    }
}
