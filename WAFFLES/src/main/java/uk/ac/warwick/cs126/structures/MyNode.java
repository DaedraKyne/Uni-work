/*
* File: MyNode.java
* Created: 04 March 2020, 16:30
* Author: Sebastien Modley, First Year CompSci
*/

package uk.ac.warwick.cs126.structures;

/*
 * Class representing a node for a tree structure.
 */
public class MyNode<E, G, T> {

    private E value; // Main value
    private MyArrayList<G> value1; // Optional value1
    private MyArrayList<E> value2; // Optional value2
    private MyArrayList<G> value3; // Optional value3
    private T data; // data stored
    private MyNode<E, G, T> parent; // parent node
    private MyNode<E, G, T> left; // left-child node
    private MyNode<E, G, T> right; // right-child node
    private int side; // 0 - root, 1 - node is left-child, 2 - node is right child


    

  /*
   * Constructor class for MyNode.
   * Sets the initial values for all the class variables.
   *
   * @param     value                  Main value of the node
   * @param     value1/value2/value3   optional values of the node
   * @param     data                   data stored by node
   */
    public MyNode(E value, MyArrayList<G> value1, MyArrayList<E> value2, MyArrayList<G> value3, T data) {
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


  /*
   * Returns main value of node.
   *
   * @return    value
   */
    public E getValue() {
        return this.value;
    }

  /*
   * Sets main value of node.
   *
   * @param     value
   * @return    old_value     past value of node or null
   */
    public E setValue(E value) {
        E old_value = this.value;
        this.value = value;
        return old_value;
    }


  /*
   * Returns optional value 1 of node.
   *
   * @return    value1
   */

    public MyArrayList<G> getValue1() {
        return this.value1;
    }

  /*
   * Sets optional value 1 of node.
   *
   * @param     value
   * @return    old_value1     past value1 of node or null
   */
    public MyArrayList<G> setValue1(MyArrayList<G> value1) {
        MyArrayList<G> old_value1 = this.value1;
        this.value1 = value1;
        return old_value1;
    }


  /*
   * Returns optional value 2 of node.
   *
   * @return    value2
   */
    public MyArrayList<E> getValue2() {
        return this.value2;
    }

  /*
   * Sets optional value 2 of node.
   *
   * @param     value
   * @return    old_value2     past value2 of node or null
   */
  public MyArrayList<E> setValue2(MyArrayList<E> value2) {
        MyArrayList<E> old_value2 = this.value2;
        this.value2 = value2;
        return old_value2;
    }


  /*
   * Returns optional value 3 of node.
   *
   * @return    value3
   */
    public MyArrayList<G> getValue3() {
        return this.value3;
    }

  /*
   * Sets optional value 3 of node.
   *
   * @param     value
   * @return    old_value3     past value1 of node or null
   */
    public MyArrayList<G> setValue3(MyArrayList<G> value3) {
        MyArrayList<G> old_value3 = this.value3;
        this.value3 = value3;
        return old_value3;
    }


  /*
   * Returns data stored by node.
   *
   * @return    data
   */
    public T getData() {
        return data;
    }

  /*
   * Sets data stored by node.
   *
   * @param     data
   * @return    old_data     past data stored by node or null
   */
    public T setData(T data) {
        T old_data = this.data;
        this.data = data;
        return old_data;
    }


  /*
   * Returns parent of node.
   *
   * @return    data
   */
    public MyNode<E, G, T> getParent() {
        return parent;
    }

  /*
   * Sets parent of node.
   *
   * @param     parent
   * @return    node     past parent of node
   */
    private MyNode<E, G, T> setParent(MyNode<E, G, T> parent) {
        MyNode<E, G, T> node = this.parent;
        this.parent = parent;
        return node;
    }


  /*
   * Returns left child of node.
   *
   * @return    left
   */
    public MyNode<E, G, T> getLeft() {
        return left;
    }

  /*
   * Sets left child of node.
   *
   * @param     left
   * @return    node     past left child of node
   */
    public MyNode<E, G, T> setLeft(MyNode<E, G, T> left) {
        MyNode<E, G, T> node = this.left;
        this.left = left;
        if (left != null) {
            left.setParent(this);
            left.setSide(1);
        }
        return node;
    }


  /*
   * Returns right child of node.
   *
   * @return    right
   */

    public MyNode<E, G, T> getRight() {
        return right;
    }

  /*
   * Sets right child of node.
   *
   * @param     right
   * @return    node     past right child of node
   */
    public MyNode<E, G, T> setRight(MyNode<E, G, T> right) {
        MyNode<E, G, T> node = this.right;
        this.right = right;
        if (right != null) {
            right.setParent(this);
            right.setSide(2);
        }
        return node;
    }


  /*
   * Returns whether node has left child.
   *
   * @return    boolean
   */
    public boolean hasLeftChild() {
        return left != null;
    }

  /*
   * Returns whether node has right child.
   *
   * @return    boolean
   */
    public boolean hasRightChild() {
        return right != null;
    }

  /*
   * Returns side of node.
   *
   * @return    side: 0 - root, 1 - left, 2 - right
   */
    public int getSide() {
        return side;
    }

  /*
   * Sets side of node.
   *
   * @param     side: 0 - root, 1 - left, 2 - right
   * @return    old_side: past side of node
   */
    public int setSide(int side) {
        int old_side = this.side;
        this.side = side;
        return old_side;
    }

  /*
   * Removes node's parent and sets side to 0.
   *
   * @return    side: 0 - root, 1 - left, 2 - right
   */
    public MyNode<E, G, T> removeParent() {
        if (parent != null) {
            if (side == 1) {
                parent.setLeft(null);
            } else if (side == 2) {
                parent.setRight(null);
            }
        }
        side = 0;
        parent = null;
        return parent;
    }
    
}