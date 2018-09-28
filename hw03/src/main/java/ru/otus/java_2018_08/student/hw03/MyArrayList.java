package ru.otus.java_2018_08.student.hw03;

import java.util.*;

public class MyArrayList<E> implements List<E> {
    static final private int INIT_CAPACITY = 10;

    private E[] array;
    private int capacity;
    private int size;

    public MyArrayList(int capacity) {
        this.capacity = capacity;

        ArrayList<Object> arr = new ArrayList<Object>();
        array = (E[]) new Object[this.capacity];
    }

    public MyArrayList() {
        this(INIT_CAPACITY);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    public Iterator<E> iterator() {
        return new ArrIterator<>();
    }

    public Object[] toArray() {
        return Arrays.copyOf(array, size);
    }

    public <T> T[] toArray(T[] ts) {
        if (ts.length < size) {
            return (T[]) Arrays.copyOf(array, size);
        }

        System.arraycopy(array, 0,  ts, 0, size);

        return ts;
    }

    private void checkSize() {
        if (size >= capacity) {
            capacity *= 2;
            E[] newArray = (E[]) new Object[capacity];
            System.arraycopy(array, 0, newArray, 0, size);

            array = newArray;
        }
    }

    public boolean containsAll(Collection<?> collection) {
        for (Object elem : collection) {
            if (indexOf(elem) < 0) {
                return false;
            }
        }

        return true;
    }

    public boolean addAll(Collection<? extends E> collection) {
        for (Object elem : collection) {
            add((E) elem);
        }

        return true;
    }

    public boolean addAll(int i, Collection<? extends E> collection) {
        int j = i;

        for (E obj : collection) {
            add(j++, obj);
        }

        return (j - i) > collection.size();
    }

    public boolean removeAll(Collection<?> collection) {
        boolean flag = false;

        for (Object obj : collection) {
            if (contains(obj)) {
                flag = true;
                remove(obj);
            }
        }

        return flag;
    }

    public boolean retainAll(Collection<?> collection) {
        boolean flag = false;

        for (E item : array) {
            if (!collection.contains(item)) {
                remove(item);
                flag = true;
            }
        }

        return flag;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
    }

    public boolean equals(Object o) {
        return this == o;
    }

    public int hashCode() {
        return this.getClass().hashCode();
    }

    public E get(int i) {
        if (i < 0 || i >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }

        return array[i];
    }

    public E set(int i, E e) {
        E tmp = get(i);
        array[i] = e;

        return tmp;
    }

    public boolean add(E e) {
        checkSize();

        array[size++] = e;
        return true;
    }

    public void add(int i, E e) {
        checkSize();

        System.arraycopy(array, i, array, i + 1, size++ - i);
        array[i] = e;
    }

    public E remove(int i) {
        E tmp = array[i++];

        System.arraycopy(array, i, array, i - 1, size-- - i);

        return tmp;
    }

    public boolean remove(Object o) {
        int index = indexOf(o);

        if (index < 0) {
            return false;
        }

        System.arraycopy(array, index + 1, array, index, size--);

        return true;
    }

    public boolean compare(Object o, Object element) {
        if (o == null) {
            return element == o;
        }

        return  o.equals(element);
    }

    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (compare(o, array[i])) return i;
        }

        return -1;
    }

    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (compare(o, array[i])) return i;
        }

        return -1;
    }

    public ListIterator<E> listIterator() {
        return new LIterator();
    }

    public ListIterator<E> listIterator(int i) {
        return new LIterator(i);
    }

    public List<E> subList(int i, int i1) {
        return Arrays.asList(Arrays.copyOfRange(array, i, i1));
    }

    private class ArrIterator<E> implements Iterator<E> {
        private int current = 0;

        @Override
        public boolean hasNext() {
            return size > current;
        }

        @Override
        public E next() {
            return (E) array[current++];
        }
    }

    private class LIterator implements ListIterator<E> {
        private int index;

        public LIterator() {
            index = -1;
        }

        public LIterator(int index) {
            this.index = index;
        }
        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            return array[++index];
        }

        @Override
        public boolean hasPrevious() {
            return index <= 0;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }

            return array[index--];
        }

        @Override
        public int nextIndex() {
            return index >= size ? size : index;
        }

        @Override
        public int previousIndex() {
            return index <= 0 ? -1 : index - 1;
        }

        @Override
        public void remove() {
            MyArrayList.this.remove(index);
        }

        @Override
        public void set(E e) {
            MyArrayList.this.set(index, e);
        }

        @Override
        public void add(E e) {
            MyArrayList.this.add(index, e);
        }
    }
}
