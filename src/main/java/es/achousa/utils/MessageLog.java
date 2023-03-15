package es.achousa.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
/*
The MessageLog class extends the built-in ArrayList class in Java and adds the ability to limit the maximum number of elements it can hold.
If the maximum number of elements is exceeded, the oldest elements are removed from the beginning of the list.
*/
public class MessageLog<E> extends ArrayList<E> {
    private int maxSize;

    public MessageLog(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(E e) {
        boolean added = super.add(e);
        if (size() > maxSize) {
            removeRange(0, size() - maxSize);
        }
        return added;
    }

    @Override
    public boolean addAll(int index, java.util.Collection<? extends E> c) {
        boolean added = super.addAll(index, c);
        if (size() > maxSize) {
            removeRange(0, size() - maxSize);
        }
        return added;
    }

}
