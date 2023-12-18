package src.algo;

import java.util.NoSuchElementException;

public class ArrayStack<E> implements Stack<E> {
    private static final int DEFAULT_CAPACITY = 8;

    private final transient List<E> list;

    public ArrayStack() {
        this.list = new ArrayList<>(DEFAULT_CAPACITY);
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
    public void push(E e) {
        this.list.addLast(e);
    }

    @Override
    public E pop() {
        if (this.list.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.list.removeLast();
    }

    @Override
    public E peek() {
        if (this.list.isEmpty()) {
            throw new NoSuchElementException();
        }
        int top = this.list.size() - 1;
        return this.list.get(top);
    }
}
