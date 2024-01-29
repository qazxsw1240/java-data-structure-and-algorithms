package src.algo;

public interface Map<K, V> extends DataStructure {
    public abstract boolean contains(K key);

    public abstract void add(K key, V value);

    public abstract V get(K key);

    public abstract void set(K key, V value);

    public abstract V remove(K key);
}
