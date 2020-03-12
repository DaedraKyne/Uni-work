package uk.ac.warwick.cs126.structures;

import java.util.Iterator;

public class MyBinaryTree<E extends Comparable<E>, T> implements Iterable<T> {
    private MyNode<E, T> root;
    private int size;

    public MyBinaryTree() {
        this.root = null;
        size = 0;
    }

    public void add(E value, T data) {
        if (root == null) {
            root = new MyNode<E, T>(value, data);
        } else {
            addToTree(root, value, data);
        }
        size += 1;
    }

    private void addToTree(MyNode<E, T> parent, E value, T data) {
        if (parent != null) {
            int compare = value.compareTo(parent.getValue());
            if (compare <= 0) {
                if (parent.hasLeftChild()) {
                    addToTree(parent.getLeft(), value, data);
                } else {
                    parent.setLeft(new MyNode<E, T>(value, data));
                }
            } else {
                if (parent.hasRightChild()) {
                    addToTree(parent.getRight(), value, data);
                } else {
                    parent.setRight(new MyNode<E, T>(value, data));
                }
            }
        }
    }

    public T remove(E value) {
        MyNode<E, T> node = getFirstNode(value);
        if (node != null) {
            int side = node.getSide();
            MyNode<E, T> ptr;
            if (node.hasRightChild()) {
                ptr = node.getRight();
                ptr = getLeftest(ptr);
                MyNode<E, T> right = ptr.getRight();
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
                    root = ptr;
                }
                if (right != null) {
                    add(right.getValue(), right.getData());
                }
                return node.getData();
    
            } else {
                if (side == 1) {
                    node.getParent().setLeft(node.getLeft());
                } else if (side == 2) {
                    node.getParent().setRight(node.getLeft());
                } else {
                    root = null;
                }
                return node.getData();
            }


        }
        return null;
    }

    public T getData(E value) {
        MyNode<E, T> ptr = getFirstNode(value);
        return (ptr != null) ? ptr.getData() : null;
    }
    public T setData(E value, T data) {
        MyNode<E, T> ptr = getFirstNode(value);
        if (ptr != null) {
            return ptr.setData(data);
        }
        return null;
    }

    public int size() {
        return size;
    }

    protected MyNode<E, T> getFirstNode(E value) {
        MyNode<E, T> ptr = root;
        int compare;
        while (ptr != null) {
            compare = value.compareTo(ptr.getValue());
            if (compare == 0) {
                return ptr;
            } else if (compare < 0) {
                ptr = ptr.getLeft();
            } else {
                ptr = ptr.getRight();
            }
        }
        return null;
    }

    public int getIterations(MyNode<E, T> node, E value) {
        if (getFirstNode(value) == null) {
            return 0;
        }
        if (node == null) {
            return getIterations(getFirstNode(value), value);
        }
        int iterations = 1;
        if (node.hasLeftChild() && node.getLeft().getValue().equals(value)) {
            iterations += getIterations(node.getLeft(), value);
        }
        if (node.hasRightChild() && node.getRight().getValue().equals(value)) {
            iterations += getIterations(node.getRight(), value);
        }
        return iterations;
    }

    protected MyNode<E, T> getRoot() {
        return root;
    }

    protected MyNode<E, T> setRoot(MyNode<E, T> root) {
        MyNode<E, T> node = this.root;
        this.root = root;
        return node;
    }

    protected int getHeight(MyNode<E, T> node) {
        if (node == null) {
            return 0;
        } else {
            /*int height = 0;
            MyArrayList<MyNode<E, T>> node_array= new MyArrayList<MyNode<E, T>>();
            MyArrayList<MyNode<E, T>> temp_node_array;
            node_array.add(node);
            while (node_array.size() != 0) {
                height += 1;
                temp_node_array = new MyArrayList<MyNode<E, T>>();
                for (int i = 0; i < node_array.size(); i++) {
                    MyNode<E, T> left = node_array.get(i).getLeft();
                    MyNode<E, T> right = node_array.get(i).getRight();
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
            return 1 + getHeight(node.getLeft()) + getHeight(node.getRight());
        }
    }

    protected int getHeight(MyNode<E, T> node, boolean n) {
        if (node == null) {
            return 0;
        } else {
            int height = 0;
            MyArrayList<MyNode<E, T>> node_array= new MyArrayList<MyNode<E, T>>();
            MyArrayList<MyNode<E, T>> temp_node_array;
            node_array.add(node);
            System.out.print("a" + node);
            while (node_array.size() != 0) {
                System.out.print(height + " ");
                height += 1;
                temp_node_array = new MyArrayList<MyNode<E, T>>();
                for (int i = 0; i < node_array.size(); i++) {
                    MyNode<E, T> left = node_array.get(i).getLeft();
                    MyNode<E, T> right = node_array.get(i).getRight();
                    if (left != null) {
                        temp_node_array.add(left);
                    }
                    if (right != null) {
                        temp_node_array.add(right);
                    }
                }
                node_array = temp_node_array;
            }
            return height;
        }
    }

    public MyNode<E, T> getLeftest(MyNode<E, T> node) {
        if (node != null) {
            while (node.hasLeftChild()) {
                node = node.getLeft();
            }
            return node;
        }
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyBinaryTreeIterator();
    }

    class MyBinaryTreeIterator implements Iterator<T> {

        MyNode<E, T> ptr = null;
        MyNode<E, T> old_ptr = null;

        @Override
        public boolean hasNext() {
            if (ptr == null && getRoot() != null) {
                ptr = getRoot();
                return true;
            } /*else if (ptr.hasLeftChild() || ptr.hasRightChild()) {
                return true;
            } else if (ptr.getSide() == 1 && ptr.getParent().hasRightChild()) {
                return true;
            } else {
                MyNode<E, T> ptr2 = ptr.getParent();
                while (ptr2 != null) {
                    if (ptr2.getSide() == 1) {return true;}
                    ptr2 = ptr2.getParent();
                }
                return false;
            }*/
            else if (ptr.hasRightChild() || ptr.getSide() == 1) {
                return true;
            } else if (ptr.getSide() == 2) {
                MyNode<E, T> ptr2 = ptr.getParent();
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
            MyNode<E, T> leftest = getLeftest(ptr);
            if (ptr != leftest && old_ptr != ptr.getLeft()) {
                old_ptr = ptr;
                ptr = leftest;
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
                while (ptr != null) {
                    if (ptr.getSide() == 1) {
                        old_ptr = ptr;
                        ptr = ptr.getParent();
                        return ptr.getData();
                    }
                    ptr = ptr.getParent();
                }
                return null;           
            } else {
                return null;
            }
        }
    }



}