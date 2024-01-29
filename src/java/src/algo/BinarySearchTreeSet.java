package src.algo;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.Random;

public class BinarySearchTreeSet<E> implements Set<E> {
    private final Comparator<? super E> comparator;

    private Node<E> root;
    private int size;

    public BinarySearchTreeSet(Comparator<? super E> comparator) {
        this.comparator = comparator;
        this.root = null;
        this.size = 0;
    }

    public static void main(String[] args) {
        Random random = new Random(0L);
        BinarySearchTreeSet<Integer> set = new BinarySearchTreeSet<>(Comparator.naturalOrder());
        for (int i = 0; i < 30; i++) {
            int value = random.nextInt(30);
            set.add(value);
            System.out.println(String.format("Added %d: ", value));
            // System.out.println(set);
            set.debug(System.out);
        }
        // set.add(8);
        // set.add(4);
        // set.add(12);
        // set.add(2);
        // set.add(6);
        // set.add(10);
        // set.add(14);
        // set.add(1);
        // set.add(3);
        // set.add(5);
        // set.add(7);
        // set.add(9);
        // set.add(11);
        // set.add(13);
        // set.add(15);
        while (!set.isEmpty()) {
            int value = random.nextInt(30);
            if (set.remove(value)) {
                System.out.print("removed " + value + ": ");
                System.out.println(set);
                // System.out.println(set);
                // set.debug(System.out);
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
        Node<E> node = add(this.root, e);
        if (node != null) {
            this.root = node;
            this.size++;
        }
    }

    @Override
    public boolean remove(E e) {
        Node<E> node = unlink(node(this.root, e));
        if (node != null) {
            this.size--;
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(E e) {
        return node(this.root, e) != null;
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
        if (this.root != null) {
            Stack<Node<E>> stack = new LinkedStack<>();
            Node<E> current = this.root;
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
            while (!stack.isEmpty()) {
                Node<E> node = stack.pop();
                builder.append(node.item);
                Node<E> next = node.right;
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

    public void debug(PrintStream printStream) {
        debug(printStream, this.root, 0);
    }

    protected void debug(PrintStream printStream, Node<E> root, int level) {
        if (root == null) {
            return;
        }
        debug(printStream, root.right, level + 1);
        printStream.print("     ".repeat(level));
        printStream.print("|");
        printStream.print(root.item);
        printStream.println("|<");
        debug(printStream, root.left, level + 1);
    }

    protected Node<E> leftmost(Node<E> node) {
        if (node == null) {
            return null;
        }
        while (true) {
            Node<E> left = node.left;
            if (left == null) {
                break;
            }
            node = left;
        }
        return node;
    }

    protected Node<E> rightmost(Node<E> node) {
        if (node == null) {
            return null;
        }
        while (true) {
            Node<E> right = node.right;
            if (right == null) {
                break;
            }
            node = right;
        }
        return node;
    }

    protected Node<E> node(Node<E> root, E item) {
        while (root != null) {
            int comparison = this.comparator.compare(item, root.item);
            if (comparison == 0) {
                break;
            }
            root = comparison < 0 ? root.left : root.right;
        }
        return root;
    }

    protected Node<E> add(Node<E> root, E item) {
        if (root == null) {
            return new Node<>(item, null, null, null);
        }
        Node<E> node = root;
        while (true) {
            int comparison = this.comparator.compare(item, node.item);
            if (comparison == 0) {
                return null;
            }
            if (comparison < 0) {
                Node<E> left = node.left;
                if (left == null) {
                    node.left = new Node<>(item, node, null, null);
                    break;
                }
                node = node.left;
            } else {
                Node<E> right = node.right;
                if (right == null) {
                    node.right = new Node<>(item, node, null, null);
                    break;
                }
                node = node.right;
            }
        }
        return root;
    }

    protected Node<E> unlink(Node<E> node) {
        if (node == null) {
            return null;
        }
        Node<E> left = node.left;
        Node<E> right = node.right;
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
        Node<E> predecessor = leftmost(right);
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

    protected Node<E> shiftNode(Node<E> node, Node<E> predecessor) {
        assert node != null;
        Node<E> parent = node.parent;
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

    protected static class Node<E> {
        E item;
        Node<E> parent;
        Node<E> left;
        Node<E> right;

        Node(E item, Node<E> parent, Node<E> left, Node<E> right) {
            this.item = item;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return String.format(
                "{item: %s, parent: %s, left: %s, right: %s}",
                this.item,
                this.parent == null ? null : this.parent.item,
                this.left == null ? null : this.left.item,
                this.right == null ? null : this.right.item);
        }
    }
}
