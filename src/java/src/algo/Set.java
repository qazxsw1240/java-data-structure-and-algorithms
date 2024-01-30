package src.algo;

public interface Set<E> extends DataStructure {
    public abstract void add(E e);

    public abstract boolean remove(E e);

    public abstract boolean contains(E e);
}
