package src.algo;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.Random;
import java.util.TreeMap;

public class RedBlackTreeSet<E> implements Set<E> {
    private final Comparator<? super E> comparator;

    private Node<E> root;
    private int size;

    @SuppressWarnings("unchecked")
    public RedBlackTreeSet() {
        this((Comparator<? super E>) Comparator.naturalOrder());
    }

    public RedBlackTreeSet(Comparator<? super E> comparator) {
        this.comparator = comparator;
        this.root = null;
        this.size = 0;
        new TreeMap<String, Object>();
    }

    public static void main(String[] args) {
        Random random = new Random(0L);
        RedBlackTreeSet<Integer> set = new RedBlackTreeSet<>();
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

    private static <E> void setColorOf(Node<E> node, Color color) {
        if (node != null) {
            node.color = color;
        }
    }

    private static <E> Node<E> parentOf(Node<E> node) {
        return node == null ? null : node.parent;
    }

    private static <E> void setParentOf(Node<E> node, Node<E> parent) {
        if (node != null) {
            node.parent = parent;
        }
    }

    private static <E> Node<E> leftOf(Node<E> node) {
        return node == null ? null : node.left;
    }

    private static <E> void setLeftOf(Node<E> node, Node<E> left) {
        if (node != null) {
            node.left = left;
        }
    }

    private static <E> Node<E> rightOf(Node<E> node) {
        return node == null ? null : node.right;
    }

    private static <E> void setRightOf(Node<E> node, Node<E> right) {
        if (node != null) {
            node.right = right;
        }
    }

    private static <E> Node<E> successor(Node<E> node) {
        if (node == null) {
            return null;
        }
        if (rightOf(node) != null) {
            Node<E> next = rightOf(node);
            while (leftOf(next) != null) {
                next = leftOf(next);
            }
            return next;
        }
        Node<E> parent = parentOf(node);
        Node<E> current = node;
        while (parent != null && rightOf(parent) == current) {
            current = parent;
            parent = parentOf(current);
        }
        return parent;
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
            if (parentOf(node) == null) {
                this.root = node;
            } else {
                if (colorOf(parentOf(node)) == Color.RED) {
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
        for (Node<E> node = leftmost(this.root); node != null; ) {
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
        while (rightOf(node) != null) {
            node = rightOf(node);
            level++;
        }
        // starts with the rightmost node
        while (node != null) {
            printStream.print("    ".repeat(level));
            printStream.println(MessageFormat.format("|{0}|<", node.item));
            if (leftOf(node) != null) {
                node = leftOf(node);
                level++;
                // move to rightmost node
                while (rightOf(node) != null) {
                    node = rightOf(node);
                    level++;
                }
            } else {
                Node<E> parent = parentOf(node);
                Node<E> current = node;
                while (leftOf(parent) == current) {
                    current = parent;
                    parent = parentOf(current);
                    level--;
                }
                node = parent;
                level--;
            }
        }
    }

    private int compare(E x, E y) {
        return this.comparator.compare(x, y);
    }

    private Node<E> leftmost(Node<E> node) {
        if (node == null) {
            return null;
        }
        while (leftOf(node) != null) {
            node = leftOf(node);
        }
        return node;
    }

    private Node<E> node(Node<E> root, E item) {
        while (root != null) {
            int comparison = compare(item, root.item);
            if (comparison == 0) {
                break;
            }
            root = comparison < 0 ? leftOf(root) : rightOf(root);
        }
        return root;
    }

    private Node<E> add(Node<E> root, E item) {
        if (root == null) {
            return new Node<>(item, null, null, null, Color.BLACK);
        }
        Node<E> node = root;
        while (true) {
            int comparison = compare(item, node.item);
            if (comparison == 0) {
                return null;
            }
            if (comparison < 0) {
                Node<E> left = leftOf(node);
                if (left == null) {
                    setLeftOf(node, new Node<>(item, node, null, null, Color.RED));
                    node = leftOf(node);
                    break;
                }
                node = leftOf(node);
            } else {
                Node<E> right = rightOf(node);
                if (right == null) {
                    setRightOf(node, new Node<>(item, node, null, null, Color.RED));
                    node = rightOf(node);
                    break;
                }
                node = rightOf(node);
            }
        }
        return node;
    }

    private Node<E> unlinkLeafOrSingleChildNode(Node<E> node) {
        assert node != null;
        Node<E> parent = parentOf(node);
        Node<E> child = leftOf(node) != null ? leftOf(node) : rightOf(node);
        if (child != null) {
            setParentOf(child, parent);
        } else {
            if (colorOf(node) == Color.BLACK) {
                recolorAfterDeletion(node);
            }
        }
        parent = parentOf(node); // check reassigned parent
        if (parent == null) {
            this.root = child;
        } else {
            if (leftOf(parent) == node) {
                setLeftOf(parent, child);
            } else {
                setRightOf(parent, child);
            }
        }
        if (colorOf(this.root) == Color.RED) { // check integrity
            setColorOf(this.root, Color.BLACK);
        }
        // faster GC
        setParentOf(node, null);
        setLeftOf(node, null);
        setRightOf(node, null);
        return node;
    }

    private Node<E> unlink(Node<E> node) {
        if (node == null) {
            return null;
        }
        Node<E> left = leftOf(node);
        Node<E> right = rightOf(node);
        if (left == null || right == null) {
            return unlinkLeafOrSingleChildNode(node);
        }
        Node<E> successor = successor(node);
        node.item = successor.item;
        return unlinkLeafOrSingleChildNode(successor);
    }

    private void recolorAfterInsertion(Node<E> node) {
        assert node != null;
        while (colorOf((parentOf(node))) == Color.RED) {
            Node<E> parent = parentOf(node);
            Node<E> grandparent = parentOf(parent);
            if (parent == leftOf(grandparent)) {
                Node<E> uncle = rightOf(grandparent);
                if (colorOf(uncle) == Color.RED) { // if the uncle node is red
                    // recolor
                    setColorOf(parent, Color.BLACK);
                    setColorOf(uncle, Color.BLACK);
                    setColorOf(grandparent, Color.RED);
                    // switch to the grandparent node to check double-red
                    node = grandparent;
                } else { // if the uncle node is black
                    if (node == rightOf(parent)) {
                        rotateLeft(parent);
                        // reassign
                        node = parent;
                        parent = parentOf(node);
                    }
                    // recolor
                    setColorOf(parent, Color.BLACK);
                    setColorOf(parent, Color.BLACK);
                    setColorOf(grandparent, Color.RED);
                    rotateRight(grandparent);
                    break;
                }
            } else { // symmetric operation
                Node<E> uncle = leftOf(grandparent);
                if (colorOf(uncle) == Color.RED) {
                    setColorOf(parent, Color.BLACK);
                    setColorOf(uncle, Color.BLACK);
                    setColorOf(grandparent, Color.RED);
                    node = grandparent;
                } else {
                    if (node == leftOf(parent)) {
                        rotateRight(parent);
                        node = parent;
                        parent = parentOf(node);
                    }
                    setColorOf(parent, Color.BLACK);
                    setColorOf(grandparent, Color.RED);
                    rotateLeft(grandparent);
                    break;
                }
            }
        }
        setColorOf(this.root, Color.BLACK); // check integrity
    }

    private void recolorAfterDeletion(Node<E> node) {
        while (parentOf(node) != null && colorOf(node) == Color.BLACK) {
            Node<E> parent = parentOf(node);
            if (node == leftOf(parent)) {
                Node<E> sibling = rightOf(parent);
                if (colorOf(sibling) == Color.RED) {
                    setColorOf(sibling, Color.BLACK);
                    setColorOf(parent, Color.BLACK);
                    rotateLeft(parent);
                    sibling = rightOf(parent);
                }
                if (colorOf(leftOf(sibling)) == Color.BLACK && colorOf(rightOf(sibling)) == Color.BLACK) {
                    setColorOf(sibling, Color.RED);
                    node = parent;
                } else {
                    if (colorOf(rightOf(sibling)) == Color.BLACK) {
                        setColorOf(leftOf(sibling), Color.BLACK);
                        setColorOf(sibling, Color.RED);
                        rotateRight(sibling);
                        sibling = rightOf(parent);
                    }
                    setColorOf(sibling, colorOf(parent));
                    setColorOf(parent, Color.BLACK);
                    setColorOf(rightOf(sibling), Color.BLACK);
                    rotateLeft(parent);
                    break;
                }
            } else { // symmetric operation
                Node<E> sibling = leftOf(parent);
                if (colorOf(sibling) == Color.RED) {
                    setColorOf(sibling, Color.BLACK);
                    setColorOf(parent, Color.BLACK);
                    rotateRight(parent);
                    sibling = leftOf(parent);
                }
                if (colorOf(rightOf(sibling)) == Color.BLACK && colorOf(leftOf(sibling)) == Color.BLACK) {
                    setColorOf(sibling, Color.RED);
                    node = parent;
                } else {
                    if (colorOf(leftOf(sibling)) == Color.BLACK) {
                        setColorOf(rightOf(sibling), Color.BLACK);
                        setColorOf(sibling, Color.RED);
                        rotateLeft(sibling);
                        sibling = leftOf(parent);
                    }
                    setColorOf(sibling, colorOf(parent));
                    setColorOf(parent, Color.BLACK);
                    setColorOf(leftOf(sibling), Color.BLACK);
                    rotateLeft(parent);
                    break;
                }
            }
        }
        setColorOf(this.root, Color.BLACK); // check integrity
    }

    private void rotateLeft(Node<E> node) {
        Node<E> parent = parentOf(node);
        Node<E> right = rightOf(node);
        setRightOf(node, leftOf(right));
        setParentOf(rightOf(node), node);
        setParentOf(node, right);
        setLeftOf(right, node);
        setParentOf(right, parent);
        if (parent == null) {
            this.root = right;
        } else {
            if (node == leftOf(parent)) {
                setLeftOf(parent, right);
            } else {
                setRightOf(parent, right);
            }
        }
    }

    private void rotateRight(Node<E> node) {
        Node<E> parent = parentOf(node);
        Node<E> left = leftOf(node);
        setLeftOf(node, rightOf(left));
        setParentOf(leftOf(node), node);
        setParentOf(node, left);
        setRightOf(left, node);
        setParentOf(left, parent);
        if (parent == null) {
            this.root = left;
        } else {
            if (node == leftOf(parent)) {
                setLeftOf(parent, left);
            } else {
                setRightOf(parent, left);
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
