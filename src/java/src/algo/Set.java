package src.algo;

public interface Set<E> extends DataStructure {
    public void add(E e);

    public boolean remove(E e);

    public boolean contains(E e);

    public Set<E> union(Set<E> s);

    public Set<E> intersection(Set<E> s);

    public Set<E> difference(Set<E> s);
}
