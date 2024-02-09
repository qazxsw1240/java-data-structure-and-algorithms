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

    private void siftUpHeight(Node<E> node) {
        assert node.left == null && node.right == null;
        if (node == null) {
            return;
        }
        Node<E> parent = node.parent;
        while (parent != null) {
            Node<E> sibling = parent.left == node ? parent.right : parent.left;
            parent.height = Math.max(heightOf(sibling), heightOf(node)) + 1;
            node = parent;
            parent = parent.parent;
        }
    }

    private int siftDownHeight(Node<E> root) {
        if (root == null) {
            return 0;
        }
        return root.height = Math.max(siftDownHeight(root.left), siftDownHeight(root.right)) + 1;
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

    private Node<E> takeBalanceAfterInsertion(Node<E> node) {
        Node<E> parent = parentOf(node);
        while (parent != null) {
            Node<E> grandparent = parentOf(parent);
            if (grandparent == null) {
                break;
            }
            int balanceFactor = heightOf(grandparent.left) - heightOf(grandparent.right);
            if (Math.abs(balanceFactor) < 2) {
                parent = grandparent;
                continue;
            }
            if (grandparent.left == parent) {
                if (parent.right == node) {// LR rotation to LL rotation
                    rotateLeft(parent);
                }
                rotateRight(grandparent);
            } else {
                if (parent.left == node) { // RL rotation to RR rotation
                    rotateRight(parent);
                }
                rotateLeft(grandparent);
            }
            siftUpHeight(node);
            parent = parentOf(node);
        }
        return this.root;
    }

    private Node<E> takeBalanceAfterDeletion(Node<E> node) {
        while (node != null) {
            Node<E> parent = parentOf(node);
            int balanceFactor = heightOf(node.left) - heightOf(node.right);
            if (Math.abs(balanceFactor) < 2) {
                node = parent;
                continue;
            }
            if (balanceFactor == 2) {
                Node<E> child = node.left;
                int childBalanceFactor = heightOf(child.left) - heightOf(child.right);
                if (childBalanceFactor < 0) { // LR rotation to LL rotation
                    rotateLeft(child);
                }
                rotateRight(node);
            } else {
                Node<E> child = node.right;
                int childBalanceFactor = heightOf(child.left) - heightOf(child.right);
                if (childBalanceFactor > 0) { // RL rotation to RR rotation
                    rotateRight(child);
                }
                rotateLeft(node);
            }
            siftUpHeight(node);
            node = parent;
        }
        return this.root;
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
        node.height = Math.max(heightOf(node.left), heightOf(node.right)) + 1;
        right.height = Math.max(heightOf(node), heightOf(right.right)) + 1;
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
        node.height = Math.max(heightOf(node.left), heightOf(node.right)) + 1;
        left.height = Math.max(heightOf(node), heightOf(left.left)) + 1;
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
        siftUpHeight(node);
        return takeBalanceAfterInsertion(node);
    }

    private Node<E> unlink(Node<E> node) {
        if (node == null) {
            return null;
        }
        Node<E> parent = node.parent;
        Node<E> left = node.left;
        Node<E> right = node.right;
        if (left == null && right == null) {
            if (parent == null) {
                this.root = null;
            } else {
                if (parent.left == node) {
                    parent.left = null;
                } else {
                    parent.right = null;
                }
            }
            // faster GC
            node.parent = null;
            node.left = null;
            node.right = null;
            siftUpHeight(parent);
            takeBalanceAfterDeletion(parent);
            return node;
        }
        if (left == null) {
            right.parent = parent;
            if (parent == null) {
                this.root = right;
            } else {
                if (parent.left == node) {
                    parent.left = right;
                } else {
                    parent.right = right;
                }
            }
            // faster GC
            node.parent = null;
            node.left = null;
            node.right = null;
            siftUpHeight(right);
            takeBalanceAfterDeletion(right);
            return node;
        }
        if (right == null) {
            left.parent = parent;
            if (parent == null) {
                this.root = left;
            } else {
                if (parent.left == node) {
                    parent.left = left;
                } else {
                    parent.right = left;
                }
            }
            // faster GC
            node.parent = null;
            node.left = null;
            node.right = null;
            siftUpHeight(left);
            takeBalanceAfterDeletion(left);
            return node;
        }
        Node<E> successor = leftmost(right);
        // swaps the values with each other
        E temp = node.item;
        node.item = successor.item;
        successor.item = temp;
        return unlink(successor);
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
