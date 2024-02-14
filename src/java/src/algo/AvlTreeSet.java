package src.algo;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.Random;

public class AvlTreeSet<E> implements Set<E> {
    private final Comparator<? super E> comparator;

    private Node<E> root;
    private int size;

    public AvlTreeSet(Comparator<? super E> comparator) {
        this.comparator = comparator;
        this.root = null;
        this.size = 0;
    }

    public static void main(String[] args) {
        Random random = new Random(0L);
        AvlTreeSet<Integer> set = new AvlTreeSet<>(Comparator.naturalOrder());
        for (int i = 0; i < 30; i++) {
            int value = random.nextInt(30);
            int previousSize = set.size();
            set.add(value);
            int nextSize = set.size();
            if (previousSize != nextSize) {
                System.out.printf("Added %d: %n", value);
                set.debug(System.out);
            }
        }
        while (!set.isEmpty()) {
            int value = random.nextInt(30);
            if (set.remove(value)) {
                System.out.println("removed " + value + ": ");
                set.debug(System.out);
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
            if (node.parent == null) {
                this.root = node;
            }
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

    public void debug(PrintStream printStream) {
        debug(printStream, this.root, 0);
    }

    private void debug(PrintStream printStream, Node<E> root, int level) {
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

    private int compare(E e1, E e2) {
        return this.comparator.compare(e1, e2);
    }

    private int heightOf(Node<E> node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(Node<E> node) {
        if (node == null) {
            return;
        }
        node.height = Math.max(heightOf(node.left), heightOf(node.right)) + 1;
    }

    private int balanceFactor(Node<E> node) {
        if (node == null) {
            return 0;
        }
        return heightOf(node.left) - heightOf(node.right);
    }

    private Node<E> parentOf(Node<E> node) {
        return node == null ? null : node.parent;
    }

    private Node<E> leftmost(Node<E> node) {
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

    private void takeBalance(Node<E> node) {
        if (node == null) {
            return;
        }
        while (node != null) {
            Node<E> parent = parentOf(node);
            updateHeight(node);
            int balanceFactor = balanceFactor(node);
            if (Math.abs(balanceFactor) < 2) {
                node = parent;
                continue;
            }
            if (balanceFactor >= 2) {
                int childBalanceFactor = balanceFactor(node.left);
                if (childBalanceFactor < 0) { // LR rotation to LL rotation
                    rotateLeft(node.left);
                }
                rotateRight(node);
            } else {
                int childBalanceFactor = balanceFactor(node.right);
                if (childBalanceFactor > 0) { // RL rotation to RR rotation
                    rotateRight(node.right);
                }
                rotateLeft(node);
            }
            node = parent;
        }
    }

    private void rotateLeft(Node<E> node) {
        assert node != null && node.right != null;
        Node<E> parent = node.parent;
        Node<E> right = node.right;
        node.right = right.left;
        if (node.right != null) {
            node.right.parent = node;
        }
        node.parent = right;
        right.left = node;
        right.parent = parent;
        if (parent == null) {
            this.root = right;
        } else {
            if (node == parent.left) {
                parent.left = right;
            } else {
                parent.right = right;
            }
        }
        // recalculate height
        updateHeight(node);
        updateHeight(right);
    }

    private void rotateRight(Node<E> node) {
        assert node != null && node.left != null;
        Node<E> parent = node.parent;
        Node<E> left = node.left;
        node.left = left.right;
        if (node.left != null) {
            node.left.parent = node;
        }
        node.parent = left;
        left.right = node;
        left.parent = parent;
        if (parent == null) {
            this.root = left;
        } else {
            if (node == parent.left) {
                parent.left = left;
            } else {
                parent.right = left;
            }
        }
        // recalculate height
        updateHeight(node);
        updateHeight(left);
    }

    private Node<E> node(Node<E> root, E e) {
        if (root == null) {
            return null;
        }
        Node<E> node = root;
        while (node != null) {
            int comparison = compare(e, node.item);
            if (comparison == 0) {
                break;
            }
            node = comparison < 0 ? node.left : node.right;
        }
        return node;
    }

    private Node<E> add(Node<E> root, E item) {
        if (root == null) {
            return new Node<>(item, null, null, null, 1);
        }
        Node<E> node = root;
        while (true) {
            int comparison = compare(item, node.item);
            if (comparison == 0) {
                return null;
            }
            if (comparison < 0) {
                Node<E> left = node.left;
                if (left == null) {
                    node.left = new Node<>(item, node, null, null, 1);
                    node = node.left;
                    break;
                }
                node = node.left;
            } else {
                Node<E> right = node.right;
                if (right == null) {
                    node.right = new Node<>(item, node, null, null, 1);
                    node = node.right;
                    break;
                }
                node = node.right;
            }
        }
        takeBalance(node);
        return root;
    }

    private Node<E> unlinkLeafOrSingleChildNode(Node<E> node) {
        if (node == null) {
            return null;
        }
        Node<E> parent = node.parent;
        Node<E> child = node.left != null ? node.left : node.right;
        if (child != null) {
            child.parent = parent;
        }
        if (parent == null) {
            this.root = child;
        } else {
            if (parent.left == node) {
                parent.left = child;
            } else {
                parent.right = child;
            }
        }
        // faster GC
        node.parent = null;
        node.left = null;
        node.right = null;
        takeBalance(child != null ? child : parent);
        return node;
    }

    private Node<E> unlink(Node<E> node) {
        if (node == null) {
            return null;
        }
        Node<E> left = node.left;
        Node<E> right = node.right;
        if (left == null || right == null) {
            return unlinkLeafOrSingleChildNode(node);
        }
        Node<E> successor = leftmost(right);
        // swaps the values with each other
        E temp = node.item;
        node.item = successor.item;
        successor.item = temp;
        return unlinkLeafOrSingleChildNode(successor);
    }

    private static class Node<E> {
        E item;
        Node<E> parent;
        Node<E> left;
        Node<E> right;
        int height;

        Node(E item, Node<E> parent, Node<E> left, Node<E> right, int height) {
            this.item = item;
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.height = height;
        }

        @Override
        public String toString() {
            return String.format("Node{ value: %s }", this.item);
        }
    }
}
