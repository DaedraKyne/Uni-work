package uk.ac.warwick.cs126.structures;

public class MyAvlTree<E extends Comparable<E>, T> extends MyBinaryTree<E, T> {


    public MyAvlTree() {
        super();
    }

    public void add(E value, T data) {
        super.add(value, data);
        restructure(super.getRoot());
    }

    private void restructure(MyNode<E, T> parent) {
        if (parent != null) {
            restructure(parent.getLeft());
            restructure(parent.getRight());
        }
    }
}