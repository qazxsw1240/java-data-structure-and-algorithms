package src.algo;

import java.util.Comparator;
import java.util.NoSuchElementException;

public class BinarySearchTreeMap<K, V> implements Map<K, V> {
    private final Comparator<? super K> comparator;

    private Node<K, V> root;
    private int size;

    public BinarySearchTreeMap(Comparator<? super K> comparator) {
        this.comparator = comparator;
        this.root = null;
        this.size = 0;
    }

    public static void main(String... args) {
        Map<String, String> map = new BinarySearchTreeMap<>(Comparator.naturalOrder());
        map.add("Seoul", "Korea");
        map.add("Tokyo", "Japan");
        map.add("Beijing", "China");
        map.add("Moscow", "Russia");
        map.add("Berlin", "Germany");
        map.add("Paris", "France");
        map.add("Madrid", "Spain");
        map.add("London", "UK");

        System.out.println(map.get("Seoul"));
        System.out.println(map.get("London"));
        System.out.println(map);
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
    public boolean contains(K key) {
        return node(this.root, key) != null;
    }

    @Override
    public void add(K key, V value) {
        Node<K, V> node = add(this.root, key, value);
        if (node != null) {
            this.root = node;
            this.size++;
        }
    }

    @Override
    public V get(K key) {
        Node<K, V> node = node(this.root, key);
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.value;
    }

    @Override
    public void set(K key, V value) {
        Node<K, V> node = node(this.root, key);
        if (node == null) {
            throw new NoSuchElementException();
        }
        node.value = value;
    }

    @Override
    public V remove(K key) {
        Node<K, V> node = unlink(node(this.root, key));
        if (node == null) {
            throw new NoSuchElementException();
        }
        V value = node.value;
        node.key = null;
        node.value = null;
        this.size--;
        return value;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if (this.root != null) {
            Stack<Node<K, V>> stack = new LinkedStack<>();
            Node<K, V> current = this.root;
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
            while (!stack.isEmpty()) {
                Node<K, V> node = stack.pop();
                builder.append(String.format("{%s: %s}", node.key, node.value));
                Node<K, V> next = node.right;
                while (next != null) {
                    stack.push(next);
                    next = next.left;
                }
                if (!stack.isEmpty()) {
                    builder.append(", ");
                }
            }
        }
        builder.append("]");
        return builder.toString();
    }

    private Node<K, V> leftmost(Node<K, V> node) {
        if (node == null) {
            return null;
        }
        while (true) {
            Node<K, V> left = node.left;
            if (left == null) {
                break;
            }
            node = left;
        }
        return node;
    }

    private Node<K, V> node(Node<K, V> root, K key) {
        while (root != null) {
            int comparison = this.comparator.compare(key, root.key);
            if (comparison == 0) {
                break;
            }
            root = comparison < 0 ? root.left : root.right;
        }
        return root;
    }

    private Node<K, V> add(Node<K, V> root, K key, V value) {
        if (root == null) {
            return new Node<>(key, value, null, null, null);
        }
        Node<K, V> node = root;
        while (true) {
            int comparison = this.comparator.compare(key, node.key);
            if (comparison == 0) {
                return null;
            }
            if (comparison < 0) {
                Node<K, V> left = node.left;
                if (left == null) {
                    node.left = new Node<>(key, value, node, null, null);
                    break;
                }
                node = node.left;
            } else {
                Node<K, V> right = node.right;
                if (right == null) {
                    node.right = new Node<>(key, value, node, null, null);
                    break;
                }
                node = node.right;
            }
        }
        return root;
    }

    private Node<K, V> unlink(Node<K, V> node) {
        if (node == null) {
            return null;
        }
        Node<K, V> left = node.left;
        Node<K, V> right = node.right;
        if (left == null) {
            node = shiftNode(node, right);
            // faster GC
            node.parent = null;
            node.left = null;
            node.right = null;
            return node;
        }
        if (right == null) {
            node = shiftNode(node, left);
            // faster GC
            node.parent = null;
            node.left = null;
            node.right = null;
            return node;
        }
        Node<K, V> predecessor = leftmost(right);
        if (predecessor.parent != node) {
            // if the predecessor node is not the child node of the node
            shiftNode(predecessor, predecessor.right);
            predecessor.right = right;
            right.parent = predecessor;
        }
        shiftNode(node, predecessor);
        predecessor.left = left;
        left.parent = predecessor;
        // faster GC
        node.parent = null;
        node.left = null;
        node.right = null;
        return node;
    }

    private Node<K, V> shiftNode(Node<K, V> node, Node<K, V> predecessor) {
        assert node != null;
        Node<K, V> parent = node.parent;
        if (parent == null) { // if the node is the root node
            this.root = predecessor;
        } else if (parent.left == node) {
            parent.left = predecessor;
        } else {
            parent.right = predecessor;
        }
        if (predecessor != null) {
            predecessor.parent = parent;
        }
        return node;
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> parent;
        Node<K, V> left;
        Node<K, V> right;

        Node(K key, V value, Node<K, V> parent, Node<K, V> left, Node<K, V> right) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }
    }
}
