/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UnorederedList;

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
public class List<T> implements ListADT<T>, Iterable<T>, Serializable
{

    //Rear é a posição livre a adicionar novo elemento...
    protected int ModCount;
    protected int rear;
    protected final int DEFAULT_CAPACITY = 20;
    protected T[] Lista;

    public class ArrayIterator<T> implements Iterator<T> {

        private int ExpectedModCount;
        private int count;

        public ArrayIterator() {
            ExpectedModCount = ModCount;
            count = 0;
        }

        @Override
        public boolean hasNext() {
            try {
                if (ExpectedModCount == ModCount) {
                    throw new ConcurrentModificationException("Deu merdaaaa!");
                }
            } catch (ConcurrentModificationException x) {
            }
            return Lista[count] != null;
        }

        @Override
        public T next() {
            try {
                if (ExpectedModCount == ModCount) {
                    throw new ConcurrentModificationException("Deu merdaaaa!");
                }
            } catch (ConcurrentModificationException x) {
            }
            return (T) Lista[count++];
        }
    }

    public List() {
        rear = 0;
        Lista = (T[]) new Object[DEFAULT_CAPACITY];
    }

    public List(int size) {
        rear = 0;
        Lista = (T[]) new Object[size];
    }

    @Override
    public T removeFirst() throws EmptyCollectionException {

        if (isEmpty()) {
            throw new EmptyCollectionException("The list is empty...");
        }

        T Objeto = Lista[0];

        for (int i = 0; i < rear; i++) {
            Lista[i] = Lista[i + 1];
        }

        Lista[rear] = null;
        rear = rear - 1;
        ModCount++;
        return Objeto;
    }

    @Override
    public T removeLast() throws EmptyCollectionException {

        if (isEmpty()) {
            throw new EmptyCollectionException("The list is empty...");
        }
        T Objeto = Lista[rear - 1];
        Lista[rear - 1] = null;
        rear = rear - 1;
        ModCount++;
        return Objeto;
    }

    @Override
    public T remove(T element) throws EmptyCollectionException,
            ElementNotFoundException {

        if (isEmpty()) {
            throw new EmptyCollectionException("The list is empty...");
        }

        T Objeto = null;

        if (!contains(element)) {
            throw new ElementNotFoundException("The element isn't on the list...");
        }

        for (int i = 0; i < rear; i++) {
            if (element.equals(Lista[i])) {
                Objeto = Lista[i];
                for (int k = i; Lista[k] != null; k++) {
                    Lista[k] = Lista[k + 1];
                }
                Lista[rear - 1] = null;
                rear = rear - 1;
                break;
            }
        }
        ModCount++;
        return Objeto;
    }

    @Override
    public T first() {
        return Lista[0];
    }

    @Override
    public T last() {
        return Lista[rear - 1];
    }

    @Override
    public boolean contains(T target) {
        for (int i = 0; Lista[i] != null; i++) {
            if (target.equals(Lista[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return rear == 0;
    }

    @Override
    public int size() {
        return rear;
    }

    @Override
    public Iterator iterator() {
        return new ArrayIterator();
    }

    @Override
    public String toString() {
        String temp = "";
        for (int i = 0; i < rear; i++) {
            temp += Lista[i].toString();
        }
        return temp;
    }

    protected void ExpandCapacity() {
        T[] tmp = (T[]) new Object[Lista.length + 60];

        System.arraycopy(Lista, 0, tmp, 0, Lista.length);

        Lista = tmp;
    }
}
