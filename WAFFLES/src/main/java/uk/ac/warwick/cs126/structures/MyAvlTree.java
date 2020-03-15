package uk.ac.warwick.cs126.structures;

import java.lang.Math;
import java.text.DateFormat;
import java.util.Date;

public class MyAvlTree<E extends Comparable<E>, G extends Comparable<G>, L extends MyArrayList<G>, F extends MyArrayList<E>, H extends MyArrayList<G>,  T> extends MyBinaryTree<E, G, L, F, H, T> {


    public MyAvlTree() {
        super();
    }

    public MyNode<E, G, L, F, H, T> add(E value, L value1, F value2, H value3, T data) {
        MyNode<E, G, L, F, H, T> node = super.add(value, value1, value2, value3, data);
        restructure(node);
        return node;
    }

    public T remove(E value, L value1, F value2, H value3) {
        MyNode<E, G, L, F, H, T> parent = super.getFirstNode(value, value1, value2, value3);
        if (parent != null) {
            parent = parent.getParent();
            T old_data = super.remove(value, value1, value2, value3);
            rebalance(parent);
            return old_data;
        }
        System.out.println("no element found with that value - removal failed");            
        return null;
    }

    protected void restructure(MyNode<E, G, L, F, H, T> parent) {
        if (parent != null)  {
            int parent_side = parent.getSide();
            int left_height = getHeight(parent.getLeft());
            int right_height = getHeight(parent.getRight());
            int height_diff = Math.abs(left_height - right_height);
            if (height_diff > 1) {
                MyNode<E, G, L, F, H, T> highest = (left_height - right_height < 0) ? parent.getRight() : parent.getLeft();
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
                    MyNode<E, G, L, F, H, T> snd_highest;
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
                    MyNode<E, G, L, F, H, T> left = snd_highest.getLeft();
                    MyNode<E, G, L, F, H, T> right = snd_highest.getRight();
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
            restructure(parent.getParent());
        }
    }

    protected void rebalance(MyNode<E, G, L, F, H, T> parent) {
        if (parent != null) {
            int i = 0;
            MyNode<E, G, L, F, H, T> z_ptr = parent;
            int height_left, height_right, height_diff;
            while (z_ptr != null) {
                height_left = super.getHeight(z_ptr.getLeft());
                height_right = super.getHeight(z_ptr.getRight());
                height_diff = Math.abs(height_left - height_right);
                if (height_diff > 1) {
                    System.out.println("  -> start rebalancing");
                    MyNode<E, G, L, F, H, T> y_ptr = height_left < height_right ?
                                            z_ptr.getRight() : z_ptr.getLeft();
                    height_left = super.getHeight(y_ptr.getLeft());
                    height_right = super.getHeight(y_ptr.getRight());                            
                    MyNode<E, G, L, F, H, T> x_ptr = height_left < height_right ?
                                            y_ptr.getRight() : y_ptr.getLeft();
                    MyNode<E, G, L, F, H, T> a, b, c;
                    if (y_ptr.getSide() == 1) {
                        System.out.println("    -> left");
                        c = z_ptr;
                        if (x_ptr.getSide() == 1) {
                            System.out.println("        -> left");
                            a = x_ptr;
                            b = y_ptr;
                            c.setLeft(y_ptr.getRight());
                        } else {
                            System.out.println("        -> right");
                            a = y_ptr;
                            b = x_ptr;
                            a.setRight(x_ptr.getLeft());
                            c.setLeft(x_ptr.getRight());
                        }
                    } else {
                        System.out.println("    -> right");
                        a = z_ptr;
                        if (x_ptr.getSide() == 1) {
                            System.out.println("        -> left");
                            b = x_ptr;
                            c = y_ptr;
                            a.setRight(x_ptr.getLeft());
                            c.setLeft(x_ptr.getRight());

                        } else {
                            System.out.println("        -> right");
                            b = y_ptr;
                            c = x_ptr;
                            a.setRight(y_ptr.getLeft());
                        }
                    }
                    if (z_ptr.getSide() == 1) {
                        System.out.println("Setting parent's left to b");
                        z_ptr.getParent().setLeft(b);
                    } else if (z_ptr.getSide() == 2) {
                        System.out.println("Setting parent's right to b");
                        z_ptr.getParent().setRight(b);
                    } else {
                        System.out.println("Setting root to b");
                        b.setSide(0);
                        super.setRoot(b);
                    }
                    b.setLeft(a);
                    b.setRight(c);
                    z_ptr = b;

                }
                z_ptr = z_ptr.getParent();
                i++;
            }

        }
    }

}