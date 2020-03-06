package uk.ac.warwick.cs126.structures;

public class MyNode<E, T> {

    private E value;
    private T data;
    private MyNode<E, T> parent;
    private MyNode<E, T> left;
    private MyNode<E, T> right;

    public MyNode(E value, T data) {
        this.value = value;
        this.data = data;
        parent = null;
        left = null;
        right = null;
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
    public MyNode<E, T> setParent(MyNode<E, T> parent) {
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
        return node;
    }

    public MyNode<E, T> getRight() {
        return right;
    }
    public MyNode<E, T> setRight(MyNode<E, T> right) {
        MyNode<E, T> node = this.right;
        this.right = right;
        return node;
    }

    public boolean hasLeftChild() {
        return left != null;
    }

    public boolean hasRightChild() {
        return right != null;
    }

    
}