package de.chst;

public class Graph<T> {
    private Node<T> root;

    public Graph(Node<T> root) {
        this.root = root;
    }

    public Node<T> getRoot() {
        return root;
    }
}
