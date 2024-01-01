package src.algo;

import java.util.NoSuchElementException;

public class LinkedStack<E> implements Stack<E> {
    private final DoublyLinkedList<E> list;

    public LinkedStack() {
        this.list = new DoublyLinkedList<>();
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

    @Override
    public String toString() {
        return this.list.toString();
    }
}
