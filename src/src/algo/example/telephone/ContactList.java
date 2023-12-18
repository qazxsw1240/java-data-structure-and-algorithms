package src.algo.example.telephone;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ContactList implements Iterable<Contact> {
    private transient Node first;
    private transient Node last;

    public ContactList() {
        this.first = null;
        this.last = null;
    }

    public boolean hasName(String name) {
        assert name != null;
        return node(name) != null;
    }

    public void add(String name, String phoneNumber) {
        assert name != null && phoneNumber != null;
        Node node = nearestNode(name);
        if (node == null) {
            Contact data = new Contact(name, phoneNumber);
            linkLast(new Node(data, null, null));
            if (this.first == null) {
                this.first = this.last;
            }
        } else {
            if (node.item.getName().equals(name)) {
                // if the name is already contained
                throw new IllegalStateException();
            }
            Contact data = new Contact(name, phoneNumber);
            linkBefore(node, new Node(data, null, null));
        }
    }

    public Contact get(String name) {
        assert name != null;
        Node node = node(name);
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.item;
    }

    public void update(String name, String phoneNumber) {
        assert name != null && phoneNumber != null;
        Node node = node(name);
        if (node == null) {
            throw new NoSuchElementException();
        }
        node.item.setPhoneNumber(phoneNumber);
    }

    public Contact remove(String name) {
        assert name != null;
        Node node = node(name);
        if (node == null) {
            throw new NoSuchElementException();
        }
        Node result = unlink(node);
        if (node != result) {
            throw new IllegalStateException();
        }
        Contact data = node.item;
        node.item = null; // faster GC
        node.previous = null;
        node.next = null;
        return data;
    }

    @Override
    public Iterator<Contact> iterator() {
        return new ContractListIterator(this.first);
    }

    private Node node(String name) {
        Node node = this.first;
        while (node != null) {
            Contact data = node.item;
            if (name.equals(data.getName())) {
                break;
            }
            node = node.next;
        }
        return node;
    }

    private Node nearestNode(String name) {
        Node node = this.first;
        if (node == null) {
            return null;
        }
        while (node != null) {
            if (name.compareTo(node.item.getName()) < 0) {
                break;
            }
            node = node.next;
        }
        return node;
    }

    private void linkBefore(Node node, Node predecessor) {
        // asserts that the predecessor node is a unit node
        assert predecessor.previous == null && predecessor.next == null;
        Node previous = node.previous;
        predecessor.previous = previous;
        predecessor.next = node;
        if (previous == null) {
            this.first = predecessor;
        } else {
            previous.next = predecessor;
        }
        node.previous = predecessor;
    }

    private void linkLast(Node node) {
        assert node != null;
        Node last = this.last;
        if (last != null) {
            last.next = node;
            node.previous = last;
        }
        this.last = node;
    }

    private Node unlink(Node node) {
        assert node != null;
        Node previous = node.previous;
        Node next = node.next;
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

    private static class Node {
        transient Contact item;
        transient Node previous;
        transient Node next;

        Node(Contact item, Node previous, Node next) {
            this.item = item;
            this.previous = previous;
            this.next = next;
        }
    }

    private static class ContractListIterator implements Iterator<Contact> {
        transient Node node;

        ContractListIterator(Node node) {
            this.node = node;
        }

        @Override
        public boolean hasNext() {
            return this.node != null;
        }

        @Override
        public Contact next() {
            if (this.node == null) {
                throw new NoSuchElementException();
            }
            Node node = this.node;
            Contact data = node.item;
            this.node = node.next;
            return data;
        }
    }
}
