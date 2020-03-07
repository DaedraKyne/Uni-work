package uk.ac.warwick.cs126.structures;

import java.lang.Math;

public class MyAvlTree<E extends Comparable<E>, T> extends MyBinaryTree<E, T> {


    public MyAvlTree() {
        super();
    }

    public void add(E value, T data) {
        super.add(value, data);
        restructure(super.getRoot());
    }

    protected void restructure(MyNode<E, T> parent) {
        if (parent != null)  {
            restructure(parent.getLeft());
            restructure(parent.getRight());
            int parent_side = parent.getSide();
            int left_height = getHeight(parent.getLeft());
            int right_height = getHeight(parent.getRight());
            int height_diff = Math.abs(left_height - right_height);
            if (height_diff > 1) {
                MyNode<E, T> highest = (left_height - right_height < 0) ? parent.getRight() : parent.getLeft();
                left_height = getHeight(highest.getLeft());
                right_height = getHeight(highest.getRight());
                int side = highest.getSide();
                if ((left_height - right_height < 0 && side == 2)
                    || (right_height - left_height < 0 && side == 1)) {
                    if (parent_side == 1) {
                        parent.getParent().setLeft(highest);
                    } else if (parent_side == 2) {
                        parent.getParent().setRight(highest);
                    } else if (parent == super.getRoot()) {
                        highest.removeParent();
                        super.setRoot(highest);
                    }
                    if (side == 1) {
                        parent.setLeft(highest.getRight());
                        highest.setRight(parent);
                    } else {
                        parent.setRight(highest.getLeft());
                        highest.setLeft(parent);
                    }
                } else {
                    MyNode<E, T> snd_highest;
                    if (side == 1) {
                        snd_highest = highest.getRight();
                    } else {
                        snd_highest = highest.getLeft();
                    }
                    if (parent_side == 1) {
                        parent.getParent().setLeft(snd_highest);
                    } else if (parent_side == 2) {
                        parent.getParent().setRight(snd_highest);
                    } else if (parent == super.getRoot()) {
                        snd_highest.removeParent();
                        super.setRoot(snd_highest);
                    }
                    MyNode<E, T> left = snd_highest.getLeft();
                    MyNode<E, T> right = snd_highest.getRight();
                    if (side == 1) {
                        snd_highest.setLeft(highest);
                        snd_highest.setRight(parent);
                        parent.setLeft(right);
                        highest.setRight(left);
                    } else {
                        snd_highest.setLeft(parent);
                        snd_highest.setRight(highest);
                        parent.setRight(left);
                        highest.setLeft(right);
                    }

                }

            }
        }
    }
}