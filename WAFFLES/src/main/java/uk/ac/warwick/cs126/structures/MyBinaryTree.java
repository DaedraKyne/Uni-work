package uk.ac.warwick.cs126.structures;

public class MyBinaryTree<E extends Comparable<E>, T> {
    private MyNode<E, T> root;

    public MyBinaryTree() {
        this.root = null;
    }

    public void add(E value, T data) {
        if (root == null) {
            root = new MyNode<E, T>(value, data);
        } else {
            addToTree(root, value, data);
        }
    }

    private void addToTree(MyNode<E, T> parent, E value, T data) {
        if (parent != null) {
            int compare = value.compareTo(parent.getValue());
            if (compare <= 0) {
                if (parent.hasLeftChild()) {
                    addToTree(parent.getLeft(), value, data);
                } else {
                    parent.setLeft(new MyNode<E, T>(value, data));
                }
            } else {
                if (parent.hasRightChild()) {
                    addToTree(parent.getRight(), value, data);
                }
                else {
                    parent.setRight(new MyNode<E, T>(value, data));
                }
            }
        }
    }

    public T getData(E value) {
        MyNode<E, T> ptr = root;
        int compare;
        while (ptr != null) {
            compare = value.compareTo(ptr.getValue());
            if (compare == 0) {
                return ptr.getData();
            } else if (compare < 0) {
                ptr = ptr.getLeft();
            } else {
                ptr = ptr.getRight();
            }
        }
        return null;
    }

    protected MyNode<E, T> getRoot() {
        return root;
    }

    protected int getHeight(MyNode<E, T> node) {
        if (node == null) {return 0;}
        else {
            return getHeight(node.getLeft()) + getHeight(node.getRight());
        }
    }

}