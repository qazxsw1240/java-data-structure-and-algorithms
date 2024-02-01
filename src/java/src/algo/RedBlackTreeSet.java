package src.algo;

import java.io.PrintStream;
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
        for (int i = 0; i < 100; i++) {
            // int value = random.nextInt(30);
            int value = i;
            set.add(value);
            System.out.printf("Added %d: %n", value);
            set.debug(System.out);
            System.out.println();
        }
        // while (!set.isEmpty()) {
        //     int value = random.nextInt(33);
        //     if (set.remove(value)) {
        //         System.out.print("removed " + value + ": ");
        //         System.out.println(set);
        //     }
        // }
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

    private void debug(PrintStream printStream, Node<E> root, int level) {
        if (root == null) {
            return;
        }
        debug(printStream, root.right, level + 1);
        printStream.print("     ".repeat(level));
        printStream.print("|");
        printStream.printf("%s, %s", root.item, root.color == Color.RED ? "R" : "B");
        printStream.println("|<");
        debug(printStream, root.left, level + 1);
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
                    if (uncle != null) uncle.color = Color.BLACK;
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
                    if (uncle != null) uncle.color = Color.BLACK;
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
