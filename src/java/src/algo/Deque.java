package src.algo;

public interface Deque<E> extends Queue<E>, DataStructure {
    public default void enqueueLast(E e) {
        enqueue(e);
    }

    public abstract void enqueueFirst(E e);

    public abstract E dequeueLast();

    public default E dequeueFirst() {
        return dequeue();
    }

    public abstract E peekLast();

    public default E peekFirst() {
        return peek();
    }
}
