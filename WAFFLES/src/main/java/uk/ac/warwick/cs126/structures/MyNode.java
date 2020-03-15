package uk.ac.warwick.cs126.structures;

public class MyNode<E, G, L extends MyArrayList<G>, F extends MyArrayList<E>, H extends MyArrayList<G>, T> {

    private E value;
    private L value1;
    private F value2;
    private H value3;
    private T data;
    private MyNode<E, G, L, F, H, T> parent;
    private MyNode<E, G, L, F, H, T> left;
    private MyNode<E, G, L, F, H, T> right;
    private int side;

    public MyNode(E value, L value1, F value2, H value3, T data) {
        this.value = value;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
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

    public L getValue1() {
        return this.value1;
    }
    public L setValue1(L value1) {
        L old_value1 = this.value1;
        this.value1 = value1;
        return old_value1;
    }

    public F getValue2() {
        return this.value2;
    }
    public F setValue2(F value2) {
        F old_value2 = this.value2;
        this.value2 = value2;
        return old_value2;
    }

    public H getValue3() {
        return this.value3;
    }
    public H setValue3(H value3) {
        H old_value3 = this.value3;
        this.value3 = value3;
        return old_value3;
    }

    public T getData() {
        return data;
    }
    public T setData(T data) {
        T old_data = this.data;
        this.data = data;
        return old_data;
    }

    public MyNode<E, G, L, F, H, T> getParent() {
        return parent;
    }
    private MyNode<E, G, L, F, H, T> setParent(MyNode<E, G, L, F, H, T> parent) {
        MyNode<E, G, L, F, H, T> node = this.parent;
        this.parent = parent;
        return node;
    }

    public MyNode<E, G, L, F, H, T> getLeft() {
        return left;
    }
    public MyNode<E, G, L, F, H, T> setLeft(MyNode<E, G, L, F, H, T> left) {
        MyNode<E, G, L, F, H, T> node = this.left;
        this.left = left;
        if (left != null) {
            left.setParent(this);
            left.setSide(1);
        }
        return node;
    }

    public MyNode<E, G, L, F, H, T> getRight() {
        return right;
    }
    public MyNode<E, G, L, F, H, T> setRight(MyNode<E, G, L, F, H, T> right) {
        MyNode<E, G, L, F, H, T> node = this.right;
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

    public MyNode<E, G, L, F, H, T> removeParent() {
        MyNode<E, G, L, F, H, T> parent = this.parent;
        if (parent != null) {
            int side = getSide();
            if (side == 1) {
                parent.setLeft(null);
            } else if (side == 2) {
                parent.setRight(null);
            }
        }
        this.side = 0;
        this.parent = null;
        return parent;
    }
    
}