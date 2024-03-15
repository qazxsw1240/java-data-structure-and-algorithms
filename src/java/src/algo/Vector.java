package src.algo;

public interface Vector<E> extends DataStructure, Iterable<E> {
    public abstract void addLast(E e);

    public abstract void addFirst(E e);

    public abstract void add(int index, E e);

    public abstract E removeLast();

    public abstract E removeFirst();

    public abstract E remove(int index);

    public abstract E get(int index);

    public abstract void set(int index, E e);
}
