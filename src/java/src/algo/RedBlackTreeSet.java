package src.algo;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        List<Integer> list = IntStream
            .range(0, 30)
            .boxed()
            .collect(Collectors.toList());
        Collections.shuffle(list, random);
        for (int i : list) {
            if (set.remove(i)) {
                System.out.println(MessageFormat.format("removed {0}: {1}", i, set));
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
            if (colorOf(node) == Color.RED) {
                recolorAfterInsertion(node);
            } else {
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
            printStream.println(MessageFormat.format("|{0},{1}|<", node.item, node.color));
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

    private Node<E> unlink(Node<E> node) {
        if (node == null) {
            return null;
        }
        Node<E> left = leftOf(node);
        Node<E> right = rightOf(node);
        if (left != null && right != null) {
            Node<E> successor = successor(node);
            E temp = node.item;
            node.item = successor.item;
            successor.item = temp;
            node = successor;
        }
        Node<E> child = leftOf(node) != null ? leftOf(node) : rightOf(node);
        if (child != null) {
            setParentOf(child, parentOf(node));
            if (parentOf(node) == null) {
                this.root = child;
            } else if (node == leftOf(parentOf(node))) {
                setLeftOf(parentOf(node), child);
            } else {
                setRightOf(parentOf(node), child);
            }
            setParentOf(node, null);
            setLeftOf(node, null);
            setRightOf(node, null);
            if (colorOf(node) == Color.BLACK) {
                recolorAfterDeletion(child);
            }
        } else if (parentOf(node) == null) {
            this.root = null;
        } else {
            if (colorOf(node) == Color.BLACK) {
                recolorAfterDeletion(node);
            }
            if (parentOf(node) != null) {
                if (node == leftOf(parentOf(node))) {
                    setLeftOf(parentOf(node), null);
                } else {
                    setRightOf(parentOf(node), null);
                }
                setParentOf(node, null);
            }
        }
        return node;
    }

    private void recolorAfterInsertion(Node<E> node) {
        assert node != null;
        while (colorOf((parentOf(node))) == Color.RED) {
            assert parentOf(parentOf(node)) != null;
            if (parentOf(node) == leftOf(parentOf(parentOf(node)))) {
                Node<E> uncle = rightOf(parentOf(parentOf(node)));
                if (colorOf(uncle) == Color.RED) { // if the uncle node is red
                    // recolor
                    setColorOf(parentOf(node), Color.BLACK);
                    setColorOf(uncle, Color.BLACK);
                    setColorOf(parentOf(parentOf(node)), Color.RED);
                    // switch to the grandparent node to check double-red
                    node = parentOf(parentOf(node));
                } else { // if the uncle node is black
                    if (node == rightOf(parentOf(node))) {
                        node = parentOf(node);
                        rotateLeft(node);
                    }
                    // recolor
                    setColorOf(parentOf(node), Color.BLACK);
                    setColorOf(parentOf(parentOf(node)), Color.RED);
                    rotateRight(parentOf(parentOf(node)));
                    break;
                }
            } else { // symmetric operation
                Node<E> uncle = leftOf(parentOf(parentOf(node)));
                if (colorOf(uncle) == Color.RED) {
                    setColorOf(parentOf(node), Color.BLACK);
                    setColorOf(uncle, Color.BLACK);
                    setColorOf(parentOf(parentOf(node)), Color.RED);
                    node = parentOf(parentOf(node));
                } else {
                    if (node == leftOf(parentOf(node))) {
                        node = parentOf(node);
                        rotateRight(node);
                    }
                    setColorOf(parentOf(node), Color.BLACK);
                    setColorOf(parentOf(parentOf(node)), Color.RED);
                    rotateLeft(parentOf(parentOf(node)));
                    break;
                }
            }
        }
        setColorOf(this.root, Color.BLACK); // check integrity
    }

    private void recolorAfterDeletion(Node<E> node) {
        assert node != null;
        while (parentOf(node) != null && colorOf(node) == Color.BLACK) {
            if (node == leftOf(parentOf(node))) {
                Node<E> sibling = rightOf(parentOf(node));
                if (colorOf(sibling) == Color.RED) {
                    setColorOf(sibling, Color.BLACK);
                    setColorOf(parentOf(node), Color.BLACK);
                    rotateLeft(parentOf(node));
                    sibling = rightOf(parentOf(node));
                }
                if (colorOf(leftOf(sibling)) == Color.BLACK && colorOf(rightOf(sibling)) == Color.BLACK) {
                    setColorOf(sibling, Color.RED);
                    node = parentOf(node);
                } else {
                    if (colorOf(rightOf(sibling)) == Color.BLACK) {
                        setColorOf(leftOf(sibling), Color.BLACK);
                        setColorOf(sibling, Color.RED);
                        rotateRight(sibling);
                        sibling = rightOf(parentOf(node));
                    }
                    setColorOf(sibling, colorOf(parentOf(node)));
                    setColorOf(parentOf(node), Color.BLACK);
                    setColorOf(rightOf(sibling), Color.BLACK);
                    rotateLeft(parentOf(node));
                    node = this.root;
                }
            } else { // symmetric operation
                Node<E> sibling = leftOf(parentOf(node));
                if (colorOf(sibling) == Color.RED) {
                    setColorOf(sibling, Color.BLACK);
                    setColorOf(parentOf(node), Color.BLACK);
                    rotateRight(parentOf(node));
                    sibling = leftOf(parentOf(node));
                }
                if (colorOf(rightOf(sibling)) == Color.BLACK && colorOf(leftOf(sibling)) == Color.BLACK) {
                    setColorOf(sibling, Color.RED);
                    node = parentOf(node);
                } else {
                    if (colorOf(leftOf(sibling)) == Color.BLACK) {
                        setColorOf(rightOf(sibling), Color.BLACK);
                        setColorOf(sibling, Color.RED);
                        rotateLeft(sibling);
                        sibling = leftOf(parentOf(node));
                    }
                    setColorOf(sibling, colorOf(parentOf(node)));
                    setColorOf(parentOf(node), Color.BLACK);
                    setColorOf(leftOf(sibling), Color.BLACK);
                    rotateRight(parentOf(node));
                    node = this.root;
                }
            }
        }
        setColorOf(node, Color.BLACK); // check integrity
    }

    private void rotateLeft(Node<E> node) {
        Node<E> parent = parentOf(node);
        Node<E> child = rightOf(node);
        Node<E> grandchild = leftOf(child);
        setRightOf(node, grandchild);
        setParentOf(grandchild, node);
        setLeftOf(child, node);
        setParentOf(node, child);
        setParentOf(child, parent);
        if (parent == null) {
            this.root = child;
        } else {
            if (node == leftOf(parent)) {
                setLeftOf(parent, child);
            } else {
                setRightOf(parent, child);
            }
        }
    }

    private void rotateRight(Node<E> node) {
        Node<E> parent = parentOf(node);
        Node<E> child = leftOf(node);
        Node<E> grandchild = rightOf(child);
        setLeftOf(node, grandchild);
        setParentOf(grandchild, node);
        setRightOf(child, node);
        setParentOf(node, child);
        setParentOf(child, parent);
        if (parent == null) {
            this.root = child;
        } else {
            if (node == leftOf(parent)) {
                setLeftOf(parent, child);
            } else {
                setRightOf(parent, child);
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

        @Override
        public String toString() {
            return String.format(
                "Node{item=%s, color=%s}",
                this.item,
                this.color);
        }
    }
}
