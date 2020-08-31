/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OrderedList;

import Exceptions.ConcurrentModificationException;
import Exceptions.ElementNotFoundException;
import Exceptions.EmptyCollectionException;
import Interfaces.ListADT;

import java.io.Serializable;
import java.util.Iterator;


/**
 *
 * @author Diogo Lopes 8180121
 * @param <T> wildtype
 */
public class DoubleLinkedList<T> implements ListADT<T>, Iterable<T>, Serializable
{

    protected DoubleNode rear;
    protected DoubleNode front;
    protected int count;
    protected int ModCount;

    private class DoubleLinkedIterator<T> implements Iterator<T> {

        private int ExpectedModCount;
        private int count;
        private DoubleNode Finder;

        public DoubleLinkedIterator() {
            ExpectedModCount = ModCount;
            count = 0;
            Finder = rear;
        }

        @Override
        public boolean hasNext() {
            try {
                if (ExpectedModCount == ModCount) {
                    throw new ConcurrentModificationException("Deu merdaaaa!");
                }
            } catch (ConcurrentModificationException x) {
            }
            return Finder != null;
        }

        @Override
        public T next() {
            DoubleNode temp;
            try {
                if (ExpectedModCount == ModCount) {
                    throw new ConcurrentModificationException("Deu merdaaaa!");
                }
            } catch (ConcurrentModificationException x) {
            }
            temp = Finder;
            Finder = Finder.getNext();
            return (T) temp.getElement();
        }
    }

    public DoubleLinkedList() {
        rear = null;
        front = null;
        count = 0;
    }

    @Override
    public T removeFirst() throws EmptyCollectionException {
        if (count == 0) {
            throw new EmptyCollectionException("The list is empty...");
        }

        DoubleNode temp = new DoubleNode();
        temp = rear;
        rear = rear.getNext();
        ModCount--;
        count--;
        return (T) temp.getElement();
    }

    @Override
    public T removeLast() throws EmptyCollectionException {

        if (count == 0) {
            throw new EmptyCollectionException("The list is empty...");
        }

        DoubleNode temp = new DoubleNode();

        temp = front;

        if (count == 1) {
            front = front.getPrevious();
            rear = front;
            ModCount--;
            count--;
            return (T) temp.getElement();
        }

        front = front.getPrevious();

        front.setNext(null);
        ModCount--;
        count--;

        return (T) temp.getElement();
    }

    @Override
    public T remove(T element) throws EmptyCollectionException, ElementNotFoundException {

        if (count == 0) {
            throw new EmptyCollectionException("The list is empty...");
        }

        Comparable<T> temp = (Comparable<T>) element;

        DoubleNode NewNode = new DoubleNode(temp);

        DoubleNode Finder = new DoubleNode();

        Finder = rear;

        while ((Finder != null) && temp.compareTo((T) Finder.getElement()) > 0) {
            Finder = Finder.getNext();
        }

        if (Finder == null) {
            throw new ElementNotFoundException("The elment was not found...");
        }

        if (count == 1) {
            rear = null;
            front = null;
            ModCount--;
            count--;
            return (T) Finder.getElement();
        }

        if (Finder == rear) {
            rear = rear.getNext();
            rear.setPrevious(null);
            ModCount--;
            count--;
            return (T) Finder.getElement();
        }
        if (Finder == front) {
            front = front.getPrevious();
            front.setNext(null);
            ModCount--;
            count--;
            return (T) Finder.getElement();
        } else {
            Finder.getPrevious().setNext(Finder.getNext());
            Finder.getNext().setPrevious(Finder.getPrevious());
            Finder.setNext(null);
            Finder.setPrevious(null);
        }
        ModCount--;
        count--;
        return (T) Finder.getElement();
    }

    @Override
    public T first() throws EmptyCollectionException {
        if (count == 0) {
            throw new EmptyCollectionException("The list is empty...");
        }
        return (T) rear.getElement();
    }

    @Override
    public T last() throws EmptyCollectionException {
        if (count == 0) {
            throw new EmptyCollectionException("The list is empty...");
        }
        return (T) front.getElement();
    }

    @Override
    public boolean contains(T target) {
        if (count == 0) {
            return false;
        }

        Comparable<T> temp = (Comparable<T>) target;

        DoubleNode Finder = new DoubleNode();

        Finder = rear;

        while ((Finder != null) && temp.compareTo((T) Finder.getElement()) > 0) {
            Finder = Finder.getNext();
        }

        return Finder != null;
    }

    @Override
    public boolean isEmpty() {
        if (count == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public Iterator<T> iterator() {
        return new DoubleLinkedIterator();
    }

    @Override
    public String toString() {
        Iterator tmp = iterator();
        String result = "";

        while (tmp.hasNext()) {
            result += tmp.next().toString();
        }
        return result;
    }

}
