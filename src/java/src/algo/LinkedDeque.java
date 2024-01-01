package src.algo;

import java.util.NoSuchElementException;

public class LinkedDeque<E> extends LinkedQueue<E> implements Deque<E> {
    public LinkedDeque() {
        super();
    }

    @Override
    public void enqueueFirst(E e) {
        this.list.addFirst(e);
    }

    @Override
    public E dequeueLast() {
        if (this.list.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.list.removeLast();
    }

    @Override
    public E peekLast() {
        if (this.list.isEmpty()) {
            throw new NoSuchElementException();
        }
        int top = this.list.size() - 1;
        return this.list.get(top);
    }
}
