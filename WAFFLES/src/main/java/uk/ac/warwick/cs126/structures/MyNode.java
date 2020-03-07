package uk.ac.warwick.cs126.structures;

public class MyNode<E, T> {

    private E value;
    private T data;
    private MyNode<E, T> parent;
    private MyNode<E, T> left;
    private MyNode<E, T> right;
    private int side;

    public MyNode(E value, T data) {
        this.value = value;
        this.data = data;
        parent = null;
        left = null;
        right = null;
        side = 0;
    }

    public E getValue() {
        return this.value;
    }
    public E setValue(E value) {
        E old_value = this.value;
        this.value = value;
        return old_value;
    }

    public T getData() {
        return data;
    }
    public T setData(T data) {
        T old_data = this.data;
        this.data = data;
        return old_data;
    }

    public MyNode<E, T> getParent() {
        return parent;
    }
    private MyNode<E, T> setParent(MyNode<E, T> parent) {
        MyNode<E, T> node = this.parent;
        this.parent = parent;
        return node;
    }

    public MyNode<E, T> getLeft() {
        return left;
    }
    public MyNode<E, T> setLeft(MyNode<E, T> left) {
        MyNode<E, T> node = this.left;
        this.left = left;
        if (left != null) {
            left.setParent(this);
            left.setSide(1);
        }
        return node;
    }

    public MyNode<E, T> getRight() {
        return right;
    }
    public MyNode<E, T> setRight(MyNode<E, T> right) {
        MyNode<E, T> node = this.right;
        this.right = right;
        if (right != null) {
            right.setParent(this);
            right.setSide(2);
        }
        return node;
    }

    public boolean hasLeftChild() {
        return left != null;
    }

    public boolean hasRightChild() {
        return right != null;
    }

    public int getSide() {
        return side;
    }
    public int setSide(int side) {
        int old_side = this.side;
        this.side = side;
        return old_side;
    }

    public MyNode<E, T> removeParent() {
        MyNode<E, T> parent = this.parent;
        if (parent != null) {
            int side = getSide();
            if (side == 1) {
                parent.setLeft(null);
            } else {
                parent.setRight(null);
            }
        }
        this.side = 0;
        this.parent = null;
        return parent;
    }
    
}