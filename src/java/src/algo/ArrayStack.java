package src.algo;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayStack<E> implements Stack<E> {
    private static final int DEFAULT_CAPACITY = 8;

    private final Vector<E> vector;

    public ArrayStack() {
        this.vector = new ArrayVector<>(DEFAULT_CAPACITY);
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
    public void push(E e) {
        this.vector.addLast(e);
    }

    @Override
    public E pop() {
        if (this.vector.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.vector.removeLast();
    }

    @Override
    public E peek() {
        if (this.vector.isEmpty()) {
            throw new NoSuchElementException();
        }
        int top = this.vector.size() - 1;
        return this.vector.get(top);
    }

    @Override
    public Iterator<E> iterator() {
        return this.vector.iterator();
    }
}
