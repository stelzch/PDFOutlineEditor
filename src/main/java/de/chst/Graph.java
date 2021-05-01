package de.chst;

public class Graph<T> {
    private final Node<T> root;

    public Graph(Node<T> root) {
        this.root = root;
    }

    public Node<T> getRoot() {
        return root;
    }


    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Graph<?>)) {
            return false;
        }
        Graph<?> o = (Graph<?>) other;

        if (getRoot() == null && o.getRoot() != null)
            return false;

        return getRoot().equals(o.getRoot());
    }

    public String toGraphviz() {
        if (root == null) {
            return "digraph G {}";
        }
        return "digraph G {\n" + getRoot().toGraphviz() + "}";
    }

    @Override
    public String toString() {
        if (root == null) {
            return "mnull";
        } else {
            return root.toString();
        }
    }
}
