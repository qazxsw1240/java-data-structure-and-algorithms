package src.algo;

public interface Queue<E> extends DataStructure {
    public abstract void enqueue(E e);
    public abstract E dequeue();
    public abstract E peek();
}
