package src.algo;

@SuppressWarnings("unused")
public class SinglyLinkedList<E> implements List<E> {
    private transient int size;
    private transient Node<E> head;

    public SinglyLinkedList() {
        this.size = 0;
        this.head = null;
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
        if (this.size == 0) {
            addFirst(e);
            return;
        }
        Node<E> node = node(this.size - 1); // last node
        node.next = new Node<>(e, null);
        this.size++;
    }

    @Override
    public void addFirst(E e) {
        this.head = new Node<>(e, this.head);
        this.size++;
    }

    @Override
    public void add(int index, E e) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException(index);
        }
        if (index == 0) {
            addFirst(e);
            return;
        }
        if (index == this.size) {
            addLast(e);
            return;
        }
        Node<E> node = node(index - 1);
        Node<E> next = node.next;
        node.next = new Node<>(e, next);
        this.size++;
    }

    @Override
    public E removeLast() {
        return null;
    }

    @Override
    public E removeFirst() {
        return null;
    }

    @Override
    public E remove(int index) {
        return null;
    }

    @Override
    public E get(int index) {
        return node(index).item;
    }

    @Override
    public void set(int index, E e) {
        node(index).item = e;
    }

    private Node<E> linkAfter(Node<E> node, Node<E> newNode) {
        if (node == null) {
            return newNode;
        }
        Node<E> next = node.next;
        node.next = newNode;
        newNode.next = next;
        return node;
    }

    private Node<E> node(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException(index);
        }
        Node<E> node = this.head;
        int i = 0;
        while (node != null) {
            if (i == index) {
                break;
            }
            node = node.next;
            i++;
        }
        return node;
    }

    private static class Node<E> {
        transient E item;
        transient Node<E> next;

        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }
}
