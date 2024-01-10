package src.algo;

import java.util.Random;

public class HashSet<E> implements Set<E> {
    private static final int DECREASE_BOUND = 16;

    private int capacity;
    private Node<E>[] buckets;
    private int size;

    @SuppressWarnings("unchecked")
    public HashSet(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
        this.buckets = new Node[this.capacity];
        this.size = 0;
    }

    private static int hash(Object o) {
        int h;
        return (o == null) ? 0 : (h = o.hashCode()) ^ (h >>> 16);
    }

    public static void main(String... args) {
        String[] data = new String[]{
            "apple",
            "avocado",
            "banana",
            "peach",
            "banana",
            "apple",
            "grape",
            "blackberry",
            "cherry",
            "blackberry",
            "lemon",
            "lime"
        };
        Random random = new Random(0L);
        HashSet<String> set = new HashSet<>(3);
        for (int i = 0, bound = data.length * 2; i < bound; i++) {
            String element = data[random.nextInt(data.length)];
            System.out.printf("added \"%s\": ", element);
            set.add(element);
            System.out.println(set);
            set.debug();
        }
        while (!set.isEmpty()) {
            String element = data[random.nextInt(data.length)];
            if (set.remove(element)) {
                System.out.printf("removed \"%s\": ", element);
                System.out.println(set);
                set.debug();
            }
        }
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
    public void add(E e) {
        if (this.size == this.capacity) {
            increase();
        }
        int entry = entry(e);
        if (insert(entry, e)) {
            this.size++;
        }
    }

    @Override
    public boolean remove(E e) {
        int entry = entry(e);
        Node<E> node = find(entry, e);
        if (node == null) {
            return false;
        }
        Node<E> result = unlink(entry, node);
        result.previous = null;
        result.next = null;
        this.size--;
        if ((this.size << 2) <= this.capacity) {
            decrease();
        }
        return true;
    }

    @Override
    public boolean contains(E e) {
        int entry = entry(e);
        return find(entry, e) != null;
    }

    @Override
    public Set<E> union(Set<E> s) {
        return null;
    }

    @Override
    public Set<E> intersection(Set<E> s) {
        return null;
    }

    @Override
    public Set<E> difference(Set<E> s) {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        int count = 0;
        for (int i = 0; i < this.capacity; i++) {
            Node<E> bucket = this.buckets[i];
            if (bucket == null) {
                continue;
            }
            while (bucket != null) {
                if (0 < count++ && count <= this.size) {
                    builder.append(", ");
                }
                builder.append(bucket.item);
                bucket = bucket.next;
            }
        }
        builder.append("]");
        return builder.toString();
    }

    public void debug() {
        System.out.println("{");
        for (int i = 0; i < this.capacity; i++) {
            Node<E> bucket = this.buckets[i];
            System.out.printf("%3d: ", i);
            while (bucket != null) {
                System.out.print(bucket.item);
                if (bucket.next != null) {
                    System.out.print(", ");
                }
                bucket = bucket.next;
            }
            System.out.println();
        }
        System.out.println("}");
    }

    private int entry(E e) {
        return (this.capacity - 1) & hash(e);
    }

    private void increase() {
        int newCapacity = this.capacity * 2;
        // overflow check
        if (newCapacity < this.capacity) {
            newCapacity = Integer.MAX_VALUE;
        }
        if (newCapacity == this.capacity) {
            throw new RuntimeException("Set is full.");
        }
        replace(newCapacity);
        for (int i = 0; i < this.capacity; i++) {
            Node<E> bucket = this.buckets[i];
            if (bucket == null) {
                continue;
            }
            while (bucket != null) {
                int entry = entry(bucket.item);
                Node<E> next = bucket.next;
                if (i != entry) {
                    unlink(i, bucket);
                    rehashInsert(entry, bucket);
                }
                bucket = next;
            }
        }
    }

    private void decrease() {
        int newCapacity = Math.max(this.capacity / 2, DECREASE_BOUND);
        if (newCapacity >= this.capacity) {
            return;
        }
        for (int i = newCapacity; i < this.capacity; i++) {
            Node<E> bucket = this.buckets[i];
            if (bucket == null) {
                continue;
            }
            while (bucket != null) {
                int entry = entry(bucket.item) % newCapacity;
                Node<E> next = bucket.next;
                if (i != entry) {
                    unlink(i, bucket);
                    rehashInsert(entry, bucket);
                }
                bucket = next;
            }
        }
        replace(newCapacity);
    }

    private boolean insert(int entry, E e) {
        Node<E> node = this.buckets[entry];
        for (Node<E> n = node; n != null; n = n.next) {
            E item = n.item;
            if (item.equals(e)) {
                return false;
            }
        }
        Node<E> newNode = new Node<>(e, null, node);
        if (node != null) {
            node.previous = newNode;
        }
        this.buckets[entry] = newNode;
        return true;
    }

    private void rehashInsert(int entry, Node<E> node) {
        Node<E> successor = this.buckets[entry];
        node.next = successor;
        node.previous = null;
        if (successor != null) {
            successor.previous = node;
        }
        this.buckets[entry] = node;
    }

    @SuppressWarnings("unchecked")
    private void replace(int newCapacity) {
        Node<E>[] newBuckets = new Node[newCapacity];
        for (int i = 0; i < this.size; i++) {
            newBuckets[i] = this.buckets[i];
            this.buckets[i] = null; // faster GC
        }
        this.capacity = newCapacity;
        this.buckets = newBuckets;
    }

    private Node<E> find(int entry, E e) {
        Node<E> node = this.buckets[entry];
        while (node != null) {
            E item = node.item;
            if (item.equals(e)) {
                break;
            }
            node = node.next;
        }
        return node;
    }

    private Node<E> unlink(int entry, Node<E> node) {
        Node<E> previous = node.previous;
        Node<E> next = node.next;
        if (previous != null) {
            previous.next = next;
        } else {
            this.buckets[entry] = next;
        }
        if (next != null) {
            next.previous = previous;
        }
        return node;
    }

    private static class Node<E> {
        final E item;
        Node<E> previous;
        Node<E> next;

        Node(E item, Node<E> previous, Node<E> next) {
            this.item = item;
            this.previous = previous;
            this.next = next;
        }
    }
}
