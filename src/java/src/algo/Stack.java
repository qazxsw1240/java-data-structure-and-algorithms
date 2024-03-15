package src.algo;

public interface Stack<E> extends DataStructure, Iterable<E> {
    public abstract void push(E e);

    public abstract E pop();

    public abstract E peek();
}
