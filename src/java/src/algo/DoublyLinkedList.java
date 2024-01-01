package src.algo;

import java.util.Random;

public class DoublyLinkedList<E> implements List<E> {
    private int size;
    private Node<E> first;
    private Node<E> last;

    public DoublyLinkedList() {
        this.size = 0;
        this.first = null;
        this.last = null;
    }

    public static void main(String... args) {
        List<Integer> list = new DoublyLinkedList<>();
        Random random = new Random(0L);
        for (int i = 0; i < 30; i++) {
            int index = random.nextInt(list.size() + 1);
            list.add(index, i);
            System.out.printf("added %2d into index %2d: ", i, index);
            System.out.println(list);
        }
        while (!list.isEmpty()) {
            int index = random.nextInt(list.size());
            int item = list.remove(index);
            System.out.printf("removed %2d from the index %2d: ", item, index);
            System.out.println(list);
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
    public void addLast(E e) {
        add(this.size, e);
    }

    @Override
    public void addFirst(E e) {
        add(0, e);
    }

    @Override
    public void add(int index, E e) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException(index);
        }
        Node<E> newNode = new Node<>(e, null, null);
        if (index == this.size) {
            linkLast(newNode);
            if (this.first == null) {
                this.first = this.last;
            }
        } else {
            Node<E> node = node(index);
            linkBefore(node, newNode);
        }
        this.size++;
    }

    @Override
    public E removeLast() {
        return remove(this.size - 1);
    }

    @Override
    public E removeFirst() {
        return remove(0);
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException(index);
        }
        Node<E> node = node(index);
        Node<E> obsolete = unlink(node);
        E item = obsolete.item;
        obsolete.item = null; // faster GC
        obsolete.previous = null;
        obsolete.next = null;
        this.size--;
        return item;
    }

    @Override
    public E get(int index) {
        Node<E> node = node(index);
        return node.item;
    }

    @Override
    public void set(int index, E e) {
        Node<E> node = node(index);
        node.item = e;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (Node<E> n = this.first; n != null; n = n.next) {
            builder.append(n.item);
            if (n.next != null) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    private Node<E> node(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException(index);
        }
        int half = this.size / 2;
        Node<E> n;
        if (index > half) {
            n = this.last;
            for (int i = this.size - 1; i > index; i--) {
                n = n.previous;
            }
        } else {
            n = this.first;
            for (int i = 0; i < index; i++) {
                n = n.next;
            }
        }
        if (n == null) {
            throw new IndexOutOfBoundsException(index);
        }
        return n;
    }

    private void linkBefore(Node<E> node, Node<E> predecessor) {
        // asserts that the predecessor node is a unit node
        assert predecessor.previous == null && predecessor.next == null;
        Node<E> previous = node.previous;
        predecessor.previous = previous;
        predecessor.next = node;
        if (previous == null) {
            this.first = predecessor;
        } else {
            previous.next = predecessor;
        }
        node.previous = predecessor;
    }

    private void linkLast(Node<E> node) {
        assert node != null;
        Node<E> tail = this.last;
        if (tail != null) {
            tail.next = node;
            node.previous = tail;
        }
        this.last = node;
    }

    private Node<E> unlink(Node<E> node) {
        assert node != null;
        Node<E> previous = node.previous;
        Node<E> next = node.next;
        if (previous == null) { // node == first
            this.first = next;
        } else {
            previous.next = next;
        }
        if (next == null) { // node == last
            this.last = previous;
        } else {
            next.previous = previous;
        }
        return node;
    }

    private static class Node<E> {
        E item;
        Node<E> previous;
        Node<E> next;

        Node(E item, Node<E> previous, Node<E> next) {
            this.item = item;
            this.previous = previous;
            this.next = next;
        }
    }
}
