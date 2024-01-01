package src.algo;

import java.util.NoSuchElementException;

public class ArrayDeque<E> extends ArrayQueue<E> implements Deque<E> {
    public ArrayDeque(int capacity) {
        super(capacity);
    }

    @Override
    public void enqueueFirst(E e) {
        if (this.size == this.capacity) {
            increase();
        }
        this.head = before(this.head);
        this.es[this.head] = e;
        this.size++;
    }

    @Override
    public E dequeueLast() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        int last = before(this.tail);
        @SuppressWarnings("unchecked")
        E ret = (E) this.es[last];
        this.tail = before(last);
        this.size--;
        if ((this.size << 2) <= this.capacity) {
            decrease();
        }
        return ret;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peekLast() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return (E) this.es[before(this.tail)];
    }

    protected int before(int index) {
        return (index - 1 + this.capacity) % this.capacity;
    }
}
