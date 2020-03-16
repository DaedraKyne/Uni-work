/*
* File: MyNode.java
* Created: 05 March 2020, 12:30
* Author: Sebastien Modley, First Year CompSci
*/

package uk.ac.warwick.cs126.structures;

import java.lang.Math;


/*
 * Class representing an AVL tree, stores its nodes and size and has a few
 * simple AVL tree functions, as well as those iterated by inheriting from MyBinaryTree.
 */
public class MyAvlTree<E extends Comparable<E>, G extends Comparable<G>, T> extends MyBinaryTree<E, G, T> {


  /*
   * Constructor class for MyAvlTree.
   * Simply calls inherited constructor method.
   */
    public MyAvlTree() {
        super();
    }


  /*
   * Calls the inherited add method, then
   * restructures the tree from that node
   *
   * @param     value                  Main value of the new node
   * @param     value1/value2/value3   optional values of the new node
   * @param     data                   data stored by new node
   * @return    node                   reference to added node
   */
    public MyNode<E, G, T> add(E value, MyArrayList<G> value1, MyArrayList<E> value2, MyArrayList<G> value3, T data) {
        MyNode<E, G, T> node = super.add(value, value1, value2, value3, data);
        restructure(node);
        return node;
    }


  /*
   * Calls the inherited remove method, then
   * rebalances the tree from that node
   *
   * @param     value                  Main value of the node
   * @param     value1/value2/value3   optional values of the node
   * @return    data                   data stored by node removed
   */
    public T remove(E value, MyArrayList<G> value1, MyArrayList<E> value2, MyArrayList<G> value3) {
        MyNode<E, G, T> parent = super.getFirstNode(value, value1, value2, value3);
        if (parent != null) {
            parent = parent.getParent();
            T old_data = super.remove(value, value1, value2, value3);
            rebalance(parent);
            return old_data;
        }
        return null;
    }


  /*
   * Restructures the tree from the given node,
   * if the tree at the given node is unbalanced.
   * The function keeeps calling itself until the root
   * is reached.
   * Restructuring is used to ensure that the tree
   * is blanced after adding a node (aka. the height of any node
   * at depth n differs at most by 1 to that of all other nodes
   * at that depth)
   * Restructuring does not affect sorting or nodes in the tree.
   *
   * @param     value                  Main value of the node
   * @param     value1/value2/value3   optional values of the node
   * @return    data                   data stored by node removed
   */
    protected void restructure(MyNode<E, G, T> parent) {
        if (parent != null)  {
            int parent_side = parent.getSide();
            int left_height = getHeight(parent.getLeft());
            int right_height = getHeight(parent.getRight());
            int height_diff = Math.abs(left_height - right_height);
            if (height_diff > 1) {
                MyNode<E, G, T> highest = (left_height - right_height < 0) ? parent.getRight() : parent.getLeft();
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
                    MyNode<E, G, T> snd_highest;
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
                    MyNode<E, G, T> left = snd_highest.getLeft();
                    MyNode<E, G, T> right = snd_highest.getRight();
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


  /*
   * Rebalancing the tree from the given node,
   * if the tree at the given node is unbalanced.
   * The function keeeps calling itself until the root
   * is reached.
   * Rebalancing is used to ensure that the tree is blanced 
   * after removing a node (aka. the height of any node
   * at depth n differs at most by 1 to that of all other nodes
   * at that depth).
   * Rebalancing does not affect sorting or nodes in the tree.
   *
   * @param     value                  Main value of the node
   * @param     value1/value2/value3   optional values of the node
   * @return    data                   data stored by node removed
   */
    protected void rebalance(MyNode<E, G, T> parent) {
        if (parent != null) {
            MyNode<E, G, T> z_ptr = parent;
            int height_left, height_right, height_diff;
            while (z_ptr != null) {
                height_left = super.getHeight(z_ptr.getLeft());
                height_right = super.getHeight(z_ptr.getRight());
                height_diff = Math.abs(height_left - height_right);
                if (height_diff > 1) {
                    MyNode<E, G, T> y_ptr = height_left < height_right ?
                                            z_ptr.getRight() : z_ptr.getLeft();
                    height_left = super.getHeight(y_ptr.getLeft());
                    height_right = super.getHeight(y_ptr.getRight());                            
                    MyNode<E, G, T> x_ptr = height_left < height_right ?
                                            y_ptr.getRight() : y_ptr.getLeft();
                    MyNode<E, G, T> a, b, c;
                    if (y_ptr.getSide() == 1) {
                        c = z_ptr;
                        if (x_ptr.getSide() == 1) {
                            a = x_ptr;
                            b = y_ptr;
                            c.setLeft(y_ptr.getRight());
                        } else {
                            a = y_ptr;
                            b = x_ptr;
                            a.setRight(x_ptr.getLeft());
                            c.setLeft(x_ptr.getRight());
                        }
                    } else {
                        a = z_ptr;
                        if (x_ptr.getSide() == 1) {
                            b = x_ptr;
                            c = y_ptr;
                            a.setRight(x_ptr.getLeft());
                            c.setLeft(x_ptr.getRight());

                        } else {
                            b = y_ptr;
                            c = x_ptr;
                            a.setRight(y_ptr.getLeft());
                        }
                    }
                    if (z_ptr.getSide() == 1) {
                        z_ptr.getParent().setLeft(b);
                    } else if (z_ptr.getSide() == 2) {
                        z_ptr.getParent().setRight(b);
                    } else {
                        b.setSide(0);
                        super.setRoot(b);
                    }
                    b.setLeft(a);
                    b.setRight(c);
                    z_ptr = b;

                }
                z_ptr = z_ptr.getParent();
            }

        }
    }

}