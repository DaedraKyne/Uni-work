/*
* File: MyBinaryTree.java
* Created: 04 March 2020, 18:48
* Author: Sebastien Modley, First Year CompSci
*/

package uk.ac.warwick.cs126.structures;

import java.util.Iterator;

/*
 * Class representing a binary tree, stores its nodes and size and has a few
 * simple binary tree functions.
 */
public class MyBinaryTree<E extends Comparable<E>, G extends Comparable<G>, T> implements Iterable<T> {
    private MyNode<E, G, T> root; // root node of tree
    private int size; // amount of nodes in tree


  /*
   * Constructor class for MyBinaryTree.
   * Initializes values.
   */
    public MyBinaryTree() {
        this.root = null;
        size = 0;
    }

    
  /*
   * Adds new node to tree's root if it doesn't have one,
   * or calls addToTree()
   *
   * @param     value                  Main value of the new node
   * @param     value1/value2/value3   optional values of the new node
   * @param     data                   data stored by new node
   * @return    node                   reference to added node
   */
    public MyNode<E, G, T> add(E value, MyArrayList<G> value1, MyArrayList<E> value2, MyArrayList<G> value3, T data) {
        size += 1;
        if (root == null) {
            return root = new MyNode<E, G, T>(value, value1, value2, value3, data);
        } else {
            return addToTree(root, value, value1, value2, value3, data);
        }    
    }



  /*
   * Adds new node to tree, comparing values with other nodes to 
   * deetermine position of node in tree.
   *
   * @param     value                  Main value of the new node
   * @param     value1/value2/value3   optional values of the new node
   * @param     data                   data stored by new node
   * @return    node                   reference to added node
   */
    private MyNode<E, G, T> addToTree(MyNode<E, G, T> parent, E value, MyArrayList<G> value1, MyArrayList<E> value2, MyArrayList<G> value3, T data) {
        if (parent != null) {
            MyNode<E, G, T> node;
            int compare = value.compareTo(parent.getValue());
            if (compare == 0 && value1 != null) {
                for (int i = 0; i < value1.size(); i++) {
                    compare = value1.get(i).compareTo(parent.getValue1().get(i));
                    if (compare != 0) {break;}
                }
            }
            if (compare == 0 && value2 != null) {
                for (int i = 0; i < value2.size(); i++) {
                    compare = value2.get(i).compareTo(parent.getValue2().get(i));
                    if (compare != 0) {break;}
                }
            }
            if (compare == 0 && value3 != null) {
                for (int i = 0; i < value3.size(); i++) {
                    compare = value3.get(i).compareTo(parent.getValue3().get(i));
                    if (compare != 0) {break;}
                }
            }
            if (compare <= 0) {
                if (parent.hasLeftChild()) {
                    node = addToTree(parent.getLeft(), value, value1, value2, value3, data);
                } else {
                    node = new MyNode<E, G, T>(value, value1, value2, value3, data);
                    parent.setLeft(node);
                }
            } else {
                if (parent.hasRightChild()) {
                    node = addToTree(parent.getRight(), value, value1, value2, value3, data);
                } else {
                    node = new MyNode<E, G, T>(value, value1, value2, value3, data);
                    parent.setRight(node);
                }
            }
            return node;
        }
        return null;
    }


  /*
   * Checks if node with given values exists, if so, removes it
   * from tree using binary tree removal technique to ensure that
   * nodes are still correctly placed.
   *
   * @param     value                  Main value of the node
   * @param     value1/value2/value3   optional values of the node
   * @return    data                   data stored by node removed
   */
    public T remove(E value, MyArrayList<G> value1, MyArrayList<E> value2, MyArrayList<G> value3) {
        MyNode<E, G, T> node = getFirstNode(value, value1, value2, value3);
        if (node != null) {
            size -= 1;
            int side = node.getSide();
            MyNode<E, G, T> ptr;
            if (node.hasRightChild()) {
                ptr = node.getRight();
                ptr = getLeftest(ptr);
                MyNode<E, G, T> right = ptr.getRight();
                if (ptr.getSide() == 1) {
                    ptr.getParent().setLeft(null);
                } else if (ptr.getSide() == 2) {
                    ptr.getParent().setRight(null);
                }
                ptr.setLeft(node.getLeft());
                ptr.setRight(node.getRight());
                if (side == 1) {
                    node.getParent().setLeft(ptr);
                } else if (side == 2) {
                    node.getParent().setRight(ptr);
                } else {
                    ptr.setSide(0);
                    setRoot(ptr);
                }
                if (right != null) {
                    size -= 1;
                    add(right.getValue(), right.getValue1(), right.getValue2(), right.getValue3(), right.getData());
                }
                return node.getData();
    
            } else {
                if (side == 1) {
                    node.getParent().setLeft(node.getLeft());
                } else if (side == 2) {
                    node.getParent().setRight(node.getLeft());
                } else {
                    setRoot(node.getLeft());
                }
                return node.getData();
            }


        }
        return null;
    }


  /*
   * Removes node with smallest values from tree.
   *
   * @return    data                   data stored by node removed
   */
    public T removeSmallest() {
        if (root != null) {
            MyNode<E, G, T> smallest = getLeftest(root);
            return remove(smallest.getValue(), smallest.getValue1(), smallest.getValue2(), smallest.getValue3());    
        }
        return null;
    }

  /*
   * Removes node with largest values from tree.
   *
   * @return    data                   data stored by node removed
   */
    public T removeLargest() {
        if (root != null) {
            MyNode<E, G, T> largest = getRightest(root);
            return remove(largest.getValue(), largest.getValue1(), largest.getValue2(), largest.getValue3());
        }
        return null;
    }


  /*
   * Checks if node with given values exists, if so, returns
   * its stored data.
   *
   * @param     value                  Main value of the node
   * @param     value1/value2/value3   optional values of the node
   * @return    data                   data stored by node
   */
    public T getData(E value, MyArrayList<G> value1, MyArrayList<E> value2, MyArrayList<G> value3) {
        MyNode<E, G, T> ptr = getFirstNode(value, value1, value2, value3);
        return (ptr != null) ? ptr.getData() : null;
    }

  /*
   * Checks if node with given values exists, if so, sets
   * its stored data to the given value.
   *
   * @param     value                  Main value of the node
   * @param     value1/value2/value3   optional values of the node
   * @param     data                   data to be stored by node
   * @return    data                   past data stored by node
   */
    public T setData(E value, MyArrayList<G> value1, MyArrayList<E> value2, MyArrayList<G> value3, T data) {
        MyNode<E, G, T> ptr = getFirstNode(value, value1, value2, value3);
        if (ptr != null) {
            return ptr.setData(data);
        }
        return null;
    }


  /*
   * Returns amount of nodes in tree (its size).
   *
   * @return    size
   */
    public int size() {
        return size;
    }

    
  /*
   * Returns first occurence of node with given values, if 
   * it exists.
   *
   * @param     value                  Main value of the node
   * @param     value1/value2/value3   optional values of the node
   * @return    node                   reference to found node
   */
    protected MyNode<E, G, T> getFirstNode(E value, MyArrayList<G> value1, MyArrayList<E> value2, MyArrayList<G> value3) {
        MyNode<E, G, T> ptr = root;
        int compare;
        while (ptr != null) {
            compare = value.compareTo(ptr.getValue());
            if (compare == 0 && value1 != null) {
                for (int i = 0; i < value1.size(); i++) {
                    compare = value1.get(i).compareTo(ptr.getValue1().get(i));
                    if (compare != 0) {
                        break;
                    }
                }
            }
            if (compare == 0 && value2 != null) {
                for (int i = 0; i < value2.size(); i++) {
                    compare = value2.get(i).compareTo(ptr.getValue2().get(i));
                    if (compare != 0) {
                        break;
                    }
                }
            }
            if (compare == 0 && value3 != null) {
                for (int i = 0; i < value3.size(); i++) {
                    compare = value3.get(i).compareTo(ptr.getValue3().get(i));
                    if (compare != 0) {
                        break;
                    }
                }
            }
            if (compare == 0) {
                return ptr;
            }
            else if (compare < 0) {
                ptr = ptr.getLeft();
            }
            else {
                ptr = ptr.getRight();
            }
        }
        return null;
    }


  /*
   * Returns true if node with given values exists in tree.
   * If not, returns false.
   *
   * @param     value                  Main value of the node
   * @param     value1/value2/value3   optional values of the node
   * @return    boolean
   */
    public boolean contains(E value, MyArrayList<G> value1, MyArrayList<E> value2, MyArrayList<G> value3) {
        return getFirstNode(value, value1, value2, value3) != null;
    }


  /*
   * Returns amount of instances of nodes with given values
   * in tree.
   *
   * @param     value                  Main value of the node
   * @param     value1/value2/value3   optional values of the node
   * @return    iterations
   */
    public int getIterations(MyNode<E, G, T> node, E value, MyArrayList<G> value1, MyArrayList<E> value2, MyArrayList<G> value3) {
        if (getFirstNode(value, value1, value2, value3) == null) {
            return 0;
        }
        if (node == null) {
            return getIterations(getFirstNode(value, value1, value2, value3), value, value1, value2, value3);
        }
        int iterations = 1;
        if (   node.hasLeftChild()
            && (value2 == null || node.hasLeftChild() && node.getLeft().getValue2().equals(value2))
            && node.getLeft().getValue().equals(value)) {
            iterations += getIterations(node.getLeft(), value, value1, value2, value3);
        }
        if (   node.hasRightChild()
            && (value2 == null || node.getRight().getValue2().equals(value2))
            && node.getRight().getValue().equals(value)) {
            iterations += getIterations(node.getRight(), value, value1, value2, value3);
        }
        return iterations;
    }


  /*
   * Returns root of the tree, if it exists.
   *
   * @return    root
   */
    protected MyNode<E, G, T> getRoot() {
        return root;
    }

  /*
   * Sets root of tree to given node.
   *
   * @param     root    new root of tree
   * @return    node    old root of tree
   */
    protected MyNode<E, G, T> setRoot(MyNode<E, G, T> root) {
        MyNode<E, G, T> node = this.root;
        this.root = root;
        if (root != null) {
            this.root.removeParent();
        }
        return node;
    }


  /*
   * Returns height of a given node.
   *
   * @return    height
   */
    protected int getHeight(MyNode<E, G, T> node) {
        if (node == null) {
            return 0;
        } else {
            return 1 + Math.max(getHeight(node.getLeft()), getHeight(node.getRight()));
        }
    }


  /*
   * Returns smallest node that is a child of given node.
   * If none exists, return given node.
   *
   * @return    node
   */
    public MyNode<E, G, T> getLeftest(MyNode<E, G, T> node) {
        if (node != null) {
            while (node.hasLeftChild()) {
                node = node.getLeft();
            }
            return node;
        }
        return null;
    }

  /*
   * Returns largest node that is a child of given node.
   * If none exists, return given node.
   *
   * @return    node
   */
    public MyNode<E, G, T> getRightest(MyNode<E, G, T> node) {
        if (node != null) {
            while (node.hasRightChild()) {
                node = node.getRight();
            }
            return node;
        }
        return null;
    }



  /*
   * Returns iterator of tree, from
   * the smallest to the largest node.
   *
   * @return    iterator
   */
    @Override
    public Iterator<T> iterator() {
        return new MyBinaryTreeIterator();
    }

  /*
   * Returns reverse-iterator of tree, from
   * the largest to the smallest node.
   *
   * @return    iterator
   */
    public Iterator<T> flippedIterator() {
        return new MyFlippedBinaryTreeIterator();
    }

    /*
    * Iterator class for MyBinaryTree
    */
    class MyBinaryTreeIterator implements Iterator<T> {

        MyNode<E, G, T> ptr = null; // node pointer of current node returned
        MyNode<E, G, T> old_ptr = null; // node pointer of past node used to access current node

       /*
        * Returns whether iterator has
        * next element to return.
        *
        * @return    boolean
        */
        @Override
        public boolean hasNext() {
            if (ptr == null && getRoot() != null) { // hasn't started iteration
                ptr = getRoot();
                return true;
            } else if (ptr == null && getRoot() == null) { // tree has no nodes
                return false;
            }
            else if (ptr.hasRightChild() || ptr.getSide() == 1) { // larger node exists
                return true;
            } else if (ptr.getSide() == 2) { // go up the tree to search for larger node
                MyNode<E, G, T> ptr2 = ptr.getParent();
                while (ptr2 != null) {
                    if (ptr2.getSide() == 1) {
                        return true;
                    }
                    ptr2 = ptr2.getParent();
                }
                return false;
            } else {
                return false;
            }
        }

       /*
        * Returns data stored by next node in tree.
        *
        * @return    data
        */
        @Override
        public T next() {
            MyNode<E, G, T> leftest = getLeftest(ptr);
            if (ptr != leftest && old_ptr != ptr.getLeft()) { // find smallest node first, but make sure you haven't iterated through it yet
                old_ptr = ptr;
                ptr = leftest;
                return ptr.getData();
            } else if (ptr.getSide() == 0 && old_ptr == null) { // root has no left node, return root before iterating through right child
                old_ptr = ptr;
                return ptr.getData();
            }
            else if (ptr.hasRightChild()) { // right child is larger
                ptr = ptr.getRight();
                leftest = getLeftest(ptr);
                if (ptr != leftest) {
                    old_ptr = ptr;
                    ptr = leftest;
                    return ptr.getData();
                }
                return ptr.getData();
            }
            else if (ptr.getSide() == 1) { // parent is larger
                old_ptr = ptr;
                ptr = ptr.getParent();
                return ptr.getData();
            } else if (ptr.getSide() == 2) { // go up the tree to search for larger node
                old_ptr = ptr;
                while (ptr != null) {
                    if (ptr.getSide() == 1) {
                        old_ptr = ptr;
                        ptr = ptr.getParent();
                        return ptr.getData();
                    }
                    ptr = ptr.getParent();
                }
                return old_ptr.getData();
            } else {
                return null;
            }
        }
    }


    /*
    * Reverse-Iterator class for MyBinaryTree
    */
    class MyFlippedBinaryTreeIterator implements Iterator<T> {

        MyNode<E, G, T> ptr = null;
        MyNode<E, G, T> old_ptr = null;


       /*
        * Returns whether iterator has
        * next element to return.
        *
        * @return    boolean
        */
        @Override
        public boolean hasNext() {
            if (ptr == null && getRoot() != null) {
                ptr = getRoot();
                return true;
            } else if (ptr == null && getRoot() == null) {
                return false;
            }
            else if (ptr.hasLeftChild() || ptr.getSide() == 2) {
                return true;
            } else if (ptr.getSide() == 1) {
                MyNode<E, G, T> ptr2 = ptr.getParent();
                while (ptr2 != null) {
                    if (ptr2.getSide() == 2) {
                        return true;
                    }
                    ptr2 = ptr2.getParent();
                }
                return false;
            } else {
                return false;
            }
        }

       /*
        * Returns data stored by next node in tree.
        *
        * @return    data
        */
        @Override
        public T next() {
            MyNode<E, G, T> rightest = getRightest(ptr);
            if (ptr != rightest && old_ptr != ptr.getRight()) {
                old_ptr = ptr;
                ptr = rightest;
                return ptr.getData();
            } else if (ptr.getSide() == 0 && old_ptr == null) {
                old_ptr = ptr;
                return ptr.getData();
            }
            else if (ptr.hasLeftChild()) {
                ptr = ptr.getLeft();
                rightest = getRightest(ptr);
                if (ptr != rightest) {
                    old_ptr = ptr;
                    ptr = rightest;
                    return ptr.getData();
                }
                return ptr.getData();
            }
            else if (ptr.getSide() == 2) {
                old_ptr = ptr;
                ptr = ptr.getParent();
                return ptr.getData();
            } else if (ptr.getSide() == 1) {
                old_ptr = ptr;
                while (ptr != null) {
                    if (ptr.getSide() == 2) {
                        old_ptr = ptr;
                        ptr = ptr.getParent();
                        return ptr.getData();
                    }
                    ptr = ptr.getParent();
                }
                return old_ptr.getData();
            } else {
                return null;
            }
        }
    }




}