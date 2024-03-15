package src.algo;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayVector<E> implements Vector<E> {
    private static final int DECREASE_BOUND = 16;

    private Object[] es;
    private int capacity;
    private int size;

    public ArrayVector(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = Math.max(capacity, 1);
        this.size = 0;
        this.es = new Object[this.capacity];
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
    public void addLast(E e) {
        add(this.size, e);
    }

    @Override
    public void addFirst(E e) {
        add(0, e);
    }

    @Override
    public void add(int index, E e) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException(index);
        }
        // increase capacity
        if (this.size == this.capacity) {
            int newCapacity = this.capacity * 2;
            // overflow check
            if (newCapacity < this.capacity) {
                newCapacity = Integer.MAX_VALUE;
            }
            if (newCapacity == this.capacity) {
                throw new RuntimeException("Vector is full.");
            }
            replace(newCapacity);
        }
        for (int i = this.size; i > index; i--) {
            this.es[i] = this.es[i - 1];
        }
        this.es[index] = e;
        this.size++;
    }

    @Override
    public E removeLast() {
        return remove(this.size - 1);
    }

    @Override
    public E removeFirst() {
        return remove(0);
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException(index);
        }
        @SuppressWarnings("unchecked")
        E ret = (E) this.es[index];
        int sizeM1 = this.size - 1;
        for (int i = index; i < sizeM1; i++) {
            this.es[i] = this.es[i + 1];
        }
        this.size--;
        // decrease capacity;
        if ((this.size << 2) <= this.capacity) {
            int newCapacity = Math.max(this.capacity / 2, DECREASE_BOUND);
            if (newCapacity >= this.capacity) {
                return ret;
            }
            replace(newCapacity);
        }
        return ret;
    }

    @Override
    public E get(int index) {
        @SuppressWarnings("unchecked")
        E ret = (E) this.es[index];
        return ret;
    }

    @Override
    public void set(int index, E e) {
        this.es[index] = e;
    }

    @Override
    public Iterator<E> iterator() {
        return new VectorIterator<>(this, 0);
    }

    private void replace(int newCapacity) {
        Object[] newEs = new Object[newCapacity];
        for (int i = 0; i < this.size; i++) {
            newEs[i] = this.es[i];
            this.es[i] = null; // faster GC
        }
        this.capacity = newCapacity;
        this.es = newEs;
    }

    private static class VectorIterator<E> implements Iterator<E> {
        final ArrayVector<E> vector;
        int index;

        VectorIterator(ArrayVector<E> vector, int index) {
            this.vector = vector;
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            return this.index != this.vector.size;
        }

        @Override
        public E next() {
            if (this.index == this.vector.size) {
                throw new NoSuchElementException();
            }
            @SuppressWarnings("unchecked")
            E item = (E) this.vector.es[this.index++];
            return item;
        }
    }
}
