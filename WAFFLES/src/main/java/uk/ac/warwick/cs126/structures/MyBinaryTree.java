package uk.ac.warwick.cs126.structures;

import java.util.Iterator;

public class MyBinaryTree<E extends Comparable<E>, G extends Comparable<G>, L extends MyArrayList<G>, F extends MyArrayList<E>, H extends MyArrayList<G>,  T> implements Iterable<T> {
    private MyNode<E, G, L, F, H, T> root;
    private int size;

    public MyBinaryTree() {
        this.root = null;
        size = 0;
    }

    public MyNode<E, G, L, F, H, T> add(E value, L value1, F value2, H value3, T data) {
        if (data != null) {
            size += 1;
            if (root == null) {
                return root = new MyNode<E, G, L, F, H, T>(value, value1, value2, value3, data);
            } else {
                return addToTree(root, value, value1, value2, value3, data);
            }    
        }
        return null;
    }

    private MyNode<E, G, L, F, H, T> addToTree(MyNode<E, G, L, F, H, T> parent, E value, L value1, F value2, H value3, T data) {
        if (parent != null) {
            MyNode<E, G, L, F, H, T> node;
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
                    node = new MyNode<E, G, L, F, H, T>(value, value1, value2, value3, data);
                    parent.setLeft(node);
                }
            } else {
                if (parent.hasRightChild()) {
                    node = addToTree(parent.getRight(), value, value1, value2, value3, data);
                } else {
                    node = new MyNode<E, G, L, F, H, T>(value, value1, value2, value3, data);
                    parent.setRight(node);
                }
            }
            return node;
        }
        return null;
    }

    public T remove(E value, L value1, F value2, H value3) {
        MyNode<E, G, L, F, H, T> node = getFirstNode(value, value1, value2, value3);
        if (node != null) {
            size -= 1;
            int side = node.getSide();
            MyNode<E, G, L, F, H, T> ptr;
            if (node.hasRightChild()) {
                ptr = node.getRight();
                ptr = getLeftest(ptr);
                MyNode<E, G, L, F, H, T> right = ptr.getRight();
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
        System.out.println("Nothing to remove?");
        return null;
    }

    public T removeSmallest() {
        MyNode<E, G, L, F, H, T> smallest = getLeftest(root);
        return remove(smallest.getValue(), smallest.getValue1(), smallest.getValue2(), smallest.getValue3());
    }
    public T removeLargest() {
        MyNode<E, G, L, F, H, T> largest = getRightest(root);
        return remove(largest.getValue(), largest.getValue1(), largest.getValue2(), largest.getValue3());
    }

    public T getData(E value, L value1, F value2, H value3) {
        MyNode<E, G, L, F, H, T> ptr = getFirstNode(value, value1, value2, value3);
        return (ptr != null) ? ptr.getData() : null;
    }
    public T setData(E value, L value1, F value2, H value3, T data) {
        MyNode<E, G, L, F, H, T> ptr = getFirstNode(value, value1, value2, value3);
        if (ptr != null) {
            return ptr.setData(data);
        }
        return null;
    }

    public int size() {
        return size;
    }

    protected MyNode<E, G, L, F, H, T> getFirstNode(E value, L value1, F value2, H value3) {
        MyNode<E, G, L, F, H, T> ptr = root;
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

    public boolean contains(E value, L value1, F value2, H value3) {
        return getFirstNode(value, value1, value2, value3) != null;
    }

    public int getIterations(MyNode<E, G, L, F, H, T> node, E value, L value1, F value2, H value3) {
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

    protected MyNode<E, G, L, F, H, T> getRoot() {
        return root;
    }

    protected MyNode<E, G, L, F, H, T> setRoot(MyNode<E, G, L, F, H, T> root) {
        MyNode<E, G, L, F, H, T> node = this.root;
        this.root = root;
        if (root != null) {
            this.root.removeParent();
        }
        return node;
    }

    protected int getHeight(MyNode<E, G, L, F, H, T> node) {
        if (node == null) {
            return 0;
        } else {
            /*int height = 0;
            MyArrayList<MyNode<E, G, L, F, H, T>> node_array= new MyArrayList<MyNode<E, G, L, F, H, T>>();
            MyArrayList<MyNode<E, G, L, F, H, T>> temp_node_array;
            node_array.add(node);
            while (node_array.size() != 0) {
                height += 1;
                temp_node_array = new MyArrayList<MyNode<E, G, L, F, H, T>>();
                for (int i = 0; i < node_array.size(); i++) {
                    MyNode<E, G, L, F, H, T> left = node_array.get(i).getLeft();
                    MyNode<E, G, L, F, H, T> right = node_array.get(i).getRight();
                    if (left != null) {
                        temp_node_array.add(left);
                    }
                    if (right != null) {
                        temp_node_array.add(right);
                    }
                }
                node_array = temp_node_array;
            }
            return height;*/
            return 1 + Math.max(getHeight(node.getLeft()), getHeight(node.getRight()));
        }
    }


    public MyNode<E, G, L, F, H, T> getLeftest(MyNode<E, G, L, F, H, T> node) {
        if (node != null) {
            while (node.hasLeftChild()) {
                node = node.getLeft();
            }
            return node;
        }
        return null;
    }
    public MyNode<E, G, L, F, H, T> getRightest(MyNode<E, G, L, F, H, T> node) {
        if (node != null) {
            while (node.hasRightChild()) {
                node = node.getRight();
            }
            return node;
        }
        return null;
    }



    @Override
    public Iterator<T> iterator() {
        return new MyBinaryTreeIterator();
    }
    public Iterator<T> flippedIterator() {
        return new MyFlippedBinaryTreeIterator();
    }

    class MyBinaryTreeIterator implements Iterator<T> {

        MyNode<E, G, L, F, H, T> ptr = null;
        MyNode<E, G, L, F, H, T> old_ptr = null;

        @Override
        public boolean hasNext() {
            if (ptr == null && getRoot() != null) {
                ptr = getRoot();
                return true;
            } else if (ptr == null && getRoot() == null) {
                System.out.println("root is null, no iteration allowed");
                return false;
            }
             /*else if (ptr.hasLeftChild() || ptr.hasRightChild()) {
                return true;
            } else if (ptr.getSide() == 1 && ptr.getParent().hasRightChild()) {
                return true;
            } else {
                MyNode<E, G, L, F, H, T> ptr2 = ptr.getParent();
                while (ptr2 != null) {
                    if (ptr2.getSide() == 1) {return true;}
                    ptr2 = ptr2.getParent();
                }
                return false;
            }*/
            else if (ptr.hasRightChild() || ptr.getSide() == 1) {
                return true;
            } else if (ptr.getSide() == 2) {
                MyNode<E, G, L, F, H, T> ptr2 = ptr.getParent();
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

        @Override
        public T next() {
            MyNode<E, G, L, F, H, T> leftest = getLeftest(ptr);
            if (ptr != leftest && old_ptr != ptr.getLeft()) {
                old_ptr = ptr;
                ptr = leftest;
                return ptr.getData();
            } else if (ptr.getSide() == 0 && old_ptr == null) {
                old_ptr = ptr;
                return ptr.getData();
            }
            else if (ptr.hasRightChild()) {
                ptr = ptr.getRight();
                leftest = getLeftest(ptr);
                if (ptr != leftest) {
                    old_ptr = ptr;
                    ptr = leftest;
                    return ptr.getData();
                }
                return ptr.getData();
            }
            else if (ptr.getSide() == 1) {
                old_ptr = ptr;
                ptr = ptr.getParent();
                return ptr.getData();
            } else if (ptr.getSide() == 2) {
                old_ptr = ptr;
                while (ptr != null) {
                    if (ptr.getSide() == 1) {
                        old_ptr = ptr;
                        ptr = ptr.getParent();
                        return ptr.getData();
                    }
                    ptr = ptr.getParent();
                }
                System.out.println("Somehow stuck at rightmost");
                return old_ptr.getData();
            } else {
                System.out.println("How th did I end up here??");
                return null;
            }
        }
    }

    class MyFlippedBinaryTreeIterator implements Iterator<T> {

        MyNode<E, G, L, F, H, T> ptr = null;
        MyNode<E, G, L, F, H, T> old_ptr = null;

        @Override
        public boolean hasNext() {
            if (ptr == null && getRoot() != null) {
                ptr = getRoot();
                return true;
            } else if (ptr == null && getRoot() == null) {
                System.out.println("root is null, no iteration allowed");
                return false;
            }
             /*else if (ptr.hasLeftChild() || ptr.hasRightChild()) {
                return true;
            } else if (ptr.getSide() == 1 && ptr.getParent().hasRightChild()) {
                return true;
            } else {
                MyNode<E, G, L, F, H, T> ptr2 = ptr.getParent();
                while (ptr2 != null) {
                    if (ptr2.getSide() == 1) {return true;}
                    ptr2 = ptr2.getParent();
                }
                return false;
            }*/
            else if (ptr.hasLeftChild() || ptr.getSide() == 2) {
                return true;
            } else if (ptr.getSide() == 1) {
                MyNode<E, G, L, F, H, T> ptr2 = ptr.getParent();
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

        @Override
        public T next() {
            MyNode<E, G, L, F, H, T> rightest = getRightest(ptr);
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
                System.out.println("Somehow stuck at rightmost");
                return old_ptr.getData();
            } else {
                System.out.println("How th did I end up here??");
                return null;
            }
        }
    }




}