package de.chst;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
    private T value;
    private List<Node<T>> children;

    public Node(T value) {
        this.value = value;
        children = new ArrayList<>();
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public boolean removeChild(Node child) {
        return children.remove(child);
    }

    public final List<Node<T>> getChildren() {
        return children;
    }

    public T getValue() {
        return value;
    }
}
