package uk.ac.warwick.cs126.structures;

public class MyArrayList<E> {

    private Object[] array;
    private int size;
    private int capacity;

    public MyArrayList() {
        // Initialise variables
        this.capacity = 1000;
        this.array = new Object[capacity];
        this.size = 0;
    }

    public boolean add(E element) {
        // Adds element to the array, returns true on success and false otherwise.
        // Doubles the array size when reached capacity
        try {
            if (this.size >= this.capacity) {
                this.capacity *= 2;
                Object[] temp = new Object[capacity];
                System.arraycopy(this.array, 0, temp, 0, this.size);
                this.array = temp;
            }

            array[this.size++] = element;

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean contains(E element) {
        // Returns true when element is in the array, false otherwise.
        for (int i = 0; i < this.size(); i++) {
            if (element.equals(this.get(i))) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        // Creates a new array and sets it to that
        this.capacity = 1000;
        this.array = new Object[capacity];
        this.size = 0;
    }

    public boolean isEmpty() {
        // Returns true if empty, false if not.
        return this.size() == 0;
    }

    public int size() {
        // Returns the size.
        return this.size;
    }

    // This line allows us to cast our object to type (E) without any warnings.
    // For further details, please see:
    // https://docs.oracle.com/javase/8/docs/api/java/lang/SuppressWarnings.html
    @SuppressWarnings("unchecked")
    public E get(int index) {
        // Returns the element from the given index in the array.
        return (E) this.array[index];
    }

    public int indexOf(E element) {
        // Returns the index if element exists in the array, -1 if does not exist.
        for (int i = 0; i < this.size(); i++) {
            if (element.equals(this.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public boolean remove(E element) {
        // Returns true if element removed from array, false if not.
        // Shifts all elements down if removed
        int index = this.indexOf(element);
        if (index >= 0) {
            for (int i = index + 1; i < this.size; i++) {
                this.set(i - 1, this.get(i));
            }
            this.array[this.size - 1] = null;
            this.size--;
            return true;
        }
        return false;
    }

    public E set(int index, E element) {
        // Returns the element from the given index
        // And replaces that element at the given index with given element
        if (index >= this.size) {
            throw new ArrayIndexOutOfBoundsException("index > size: " + index + " >= " + this.size);
        }
        E replaced = this.get(index);
        this.array[index] = element;
        return replaced;
    }

    public E[] toArray() {
        return this.array;
    }

    public String toString() {
        // Returns a String representation of the elements inside the array.
        if (this.isEmpty()) {
            return "Empty";
        }

        StringBuilder ret = new StringBuilder("");
        for (int i = 0; i < this.size; i++) {
            ret.append("Index: " + i + "    Element: " + this.get(i) + "\n");
        }

        ret.deleteCharAt(ret.length() - 1);

        return ret.toString();
    }
}
