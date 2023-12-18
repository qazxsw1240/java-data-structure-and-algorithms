package src.algo;

public interface Stack<E> extends DataStructure {
    public abstract void push(E e);

    public abstract E pop();

    public abstract E peek();
}
