/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LinkedList;

import java.util.Iterator;

/**
 *
 * @author Diogo Lopes 8180121
 * @param <T> wildtype
 */
public class LinkedList<T> implements Iterable<T> {

    private LinkedNode Head;
    private int Count;
    private int ModCount;

    public class LinkedIterator implements Iterator<T> {

        private int ExpectedModCount;
        private LinkedNode Finder;

        public LinkedIterator() {
            ExpectedModCount = ModCount;
            Finder = getHead();
        }

        @Override
        public boolean hasNext() {
            try {
                if (ExpectedModCount != ModCount) {
                    throw new Error("Deu merdaaaa!");
                }
            } catch (Error x) {
            }
            return Finder != null;
        }

        @Override
        public T next() {

            LinkedNode temp;

            try {
                if (ExpectedModCount != ModCount) {
                    throw new Error("Deu merdaaaa!");
                }
            } catch (Error x) {
            }

            temp = Finder;

            Finder = Finder.getNext();

            return (T) temp.getObjeto();
        }
    }

    public LinkedList() {
        this.Head = null;
    }

    public LinkedNode getHead() {
        return Head;
    }

    public void addNewNode(T Objeto) {
       
        LinkedNode new_node = new LinkedNode(Objeto);

        if (Head == null) {
            Head = new_node;
        } else {
          
            LinkedNode last = getHead();

            while (last.getNext() != null) {
                last = last.getNext();
            }
          
            last.setNext(new_node);
        }
        Count++;
        ModCount++;
    }

    public boolean removeNode(T Objeto) {

        boolean isRemoved = false;
     

        if (this.Head == null) {
            System.out.println("There's nothing to remove");
        }

        LinkedNode Finder = getHead();

        Comparable<T> Comparator = (Comparable<T>) Objeto;

        while (Finder != null) {
            if (getHead().getObjeto() == Comparator) {
                this.Head = Head.getNext();
                isRemoved = true;
                break;
            } else if (Finder.getNext().getObjeto() == Comparator && Finder.getNext().getNext() != null) {
                Finder.setNext(Finder.getNext().getNext());
                isRemoved = true;
                break;
            } else if (Finder.getNext().getObjeto() == Comparator && Finder.getNext().getNext() == null) {
                Finder.setNext(null);
                isRemoved = true;
                break;
            }
            Finder = Finder.getNext();
        }

        Count--;
        ModCount++;
        return isRemoved;
    }

    public void printList() {
        Iterator it;

        it = iterator();

        LinkedNode Last = getHead();

        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedIterator();
    }
}
