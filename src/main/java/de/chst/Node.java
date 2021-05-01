package de.chst;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
    private final T value;
    private final List<Node<T>> children;

    public Node(T value) {
        this.value = value;
        children = new ArrayList<>();
    }

    public Node(T value, List<Node<T>> children) {
        this.value = value;
        this.children = children;
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }

    public void addChild(Node<T> child) {
        children.add(child);
    }

    public boolean removeChild(Node<T> child) {
        return children.remove(child);
    }

    public final List<Node<T>> getChildren() {
        return children;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Node)) {
            return false;
        }
        Node<?> o = (Node<?>) other;

        /* Check for null value */
        if (o.getValue() == null){
            if(getValue() != null)
                return false;
        } else {
            if (!o.getValue().equals(getValue())) {
                return false;
            }
        }

        if (o.getChildren().size() != getChildren().size()) {
            return false;
        }

        for (int i = 0; i < getChildren().size(); i++) {
            if (!o.getChildren().get(i).equals(getChildren().get(i))) {
                return false;
            }
        }

        return true;
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("(Node ");
        if (value == null) {
            result.append("null");
        } else {
            result.append(value);
        }
        result.append(", ");

        for (var child : children) {
            result.append(child.toString()).append(",");
        }
        result.append(")");

        return result.toString();
    }

    public String toGraphviz() {
        StringBuilder result = new StringBuilder();
        String label = "null";
        if(value != null) {
            label = value.toString();
        }
        result.append(hashCode()).append(" [label=\"").append(label).append("\"];\n");

        for(var c : children) {
            result.append(c.toGraphviz());
            result.append(hashCode()).append(" -> ").append(c.hashCode()).append("\n");
        }

        return result.toString();
    }
}
