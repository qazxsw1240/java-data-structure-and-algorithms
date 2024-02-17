package src.algo;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.Random;

public class RedBlackTreeSet<E> implements Set<E> {
    private final Comparator<? super E> comparator;

    private Node<E> root;
    private int size;

    public RedBlackTreeSet(Comparator<? super E> comparator) {
        this.comparator = comparator;
        this.root = null;
        this.size = 0;
    }

    public static void main(String[] args) {
        Random random = new Random(0L);
        RedBlackTreeSet<Integer> set = new RedBlackTreeSet<>(Comparator.naturalOrder());
        for (int i = 0; i < 30; i++) {
            int value = random.nextInt(30);
            int previousSize = set.size();
            set.add(value);
            int nextSize = set.size();
            if (previousSize != nextSize) {
                System.out.printf("Added %d: %n", value);
                set.debug(System.out);
                System.out.println();
            }
        }
        while (!set.isEmpty()) {
            int value = random.nextInt(30);
            if (set.remove(value)) {
                System.out.println(MessageFormat.format("removed {0}: {1}", value, set));
                set.debug(System.out);
                System.out.println();
            }
        }
    }

    private static <E> Color colorOf(Node<E> node) {
        return node == null ? Color.BLACK : node.color;
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
            } else {
                if (node.parent.color == Color.RED) {
                    recolorAfterInsertion(node);
                }
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (Node<E> node = leftmost(this.root); node != null;) {
            builder.append(node.item);
            node = successor(node);
            if (node == null) {
                break;
            }
            builder.append(", ");
        }
        builder.append("]");
        return builder.toString();
    }

    public void debug(PrintStream printStream) {
        if (this.root == null) {
            return;
        }
        int level = 0;
        Node<E> node = this.root;
        // move to rightmost node
        while (node != null && node.right != null) {
            node = node.right;
            level++;
        }
        // starts with the rightmost node
        while (node != null) {
            printStream.print("    ".repeat(level));
            printStream.println(MessageFormat.format("|{0}|<", node.item));
            if (node.left != null) {
                node = node.left;
                level++;
                // move to rightmost node
                while (node != null && node.right != null) {
                    node = node.right;
                    level++;
                }
            } else {
                Node<E> parent = node.parent;
                Node<E> current = node;
                while (parent != null && parent.left == current) {
                    current = parent;
                    parent = current.parent;
                    level--;
                }
                node = parent;
                level--;
            }
        }
    }

    private static <E> Node<E> predecessor(Node<E> node) {
        if (node == null) {
            return null;
        }
        if (node.left != null) {
            Node<E> next = node.left;
            while (next.right != null) {
                next = next.right;
            }
            return next;
        }
        Node<E> parent = node.parent;
        Node<E> current = node;
        while (parent != null && parent.left == current) {
            current = parent;
            parent = current.parent;
        }
        return parent;
    }

    private static <E> Node<E> successor(Node<E> node) {
        if (node == null) {
            return null;
        }
        if (node.right != null) {
            Node<E> next = node.right;
            while (next.left != null) {
                next = next.left;
            }
            return next;
        }
        Node<E> parent = node.parent;
        Node<E> current = node;
        while (parent != null && parent.right == current) {
            current = parent;
            parent = parent.parent;
        }
        return parent;
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

    private Node<E> node(Node<E> root, E item) {
        while (root != null) {
            int comparison = this.comparator.compare(item, root.item);
            if (comparison == 0) {
                break;
            }
            root = comparison < 0 ? root.left : root.right;
        }
        return root;
    }

    private Node<E> add(Node<E> root, E item) {
        if (root == null) {
            return new Node<>(item, null, null, null, Color.BLACK);
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
                    node.left = new Node<>(item, node, null, null, Color.RED);
                    node = node.left;
                    break;
                }
                node = node.left;
            } else {
                Node<E> right = node.right;
                if (right == null) {
                    node.right = new Node<>(item, node, null, null, Color.RED);
                    node = node.right;
                    break;
                }
                node = node.right;
            }
        }
        return node;
    }

    private Node<E> unlink(Node<E> node) {
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

    private Node<E> shiftNode(Node<E> node, Node<E> predecessor) {
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

    private void recolorAfterInsertion(Node<E> node) {
        assert node != null;
        Node<E> parent;
        while (colorOf((parent = node.parent)) == Color.RED) {
            Node<E> grandparent = parent.parent;
            Node<E> uncle;
            if (parent == grandparent.left) {
                uncle = grandparent.right;
                if (colorOf(uncle) == Color.RED) { // if the uncle node is red
                    // recolor
                    parent.color = Color.BLACK;
                    if (uncle != null)
                        uncle.color = Color.BLACK;
                    grandparent.color = Color.RED;
                    // switch to the grandparent node to check double-red
                    node = grandparent;
                } else { // if the uncle node is black
                    if (node == parent.right) {
                        Node<E> temp = node;
                        node = parent;
                        parent = temp;
                        // after the rotation, the node and its parent will swap places
                        rotateLeft(node);
                    }
                    // recolor
                    parent.color = Color.BLACK;
                    grandparent.color = Color.RED;
                    rotateRight(grandparent);
                    break;
                }
            } else {
                uncle = grandparent.left;
                if (colorOf(uncle) == Color.RED) {
                    // recolor
                    parent.color = Color.BLACK;
                    if (uncle != null)
                        uncle.color = Color.BLACK;
                    grandparent.color = Color.RED;
                    // switch to the grandparent node to check double-red
                    node = grandparent;
                } else { // if the uncle node is black
                    if (node == parent.left) {
                        Node<E> temp = node;
                        node = parent;
                        parent = temp;
                        // after the rotation, the node and its parent will swap places
                        rotateRight(node);
                    }
                    // recolor
                    parent.color = Color.BLACK;
                    grandparent.color = Color.RED;
                    rotateLeft(grandparent);
                    break;
                }
            }
        }
        if (this.root.color != Color.BLACK) { // check integrity
            this.root.color = Color.BLACK;
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
    }

    private static enum Color {
        RED,
        BLACK;
    }

    private static class Node<E> {
        E item;
        Node<E> parent;
        Node<E> left;
        Node<E> right;
        Color color;

        Node(E item, Node<E> parent, Node<E> left, Node<E> right, Color color) {
            this.item = item;
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.color = color;
        }
    }
}
