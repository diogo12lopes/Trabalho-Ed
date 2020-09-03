/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OrderedList;

import Interfaces.OrderedListADT;

/**
 *
 * @author Diogo Lopes 8180121
 * @param <T> wildtype
 */
public class DoubleLinkedOrderedList<T> extends DoubleLinkedList<T> implements OrderedListADT<T> {

   /* public DoubleLinkedOrderedList(DoubleLinkedOrderedList<T> mapLeaderboard)
    {
        mapLeaderboard.clone()
    }*/

    @Override
    public void add(T element) {

        Comparable<T> temp = (Comparable<T>) element;

        DoubleNode NewNode = new DoubleNode(temp);

        DoubleNode Finder = new DoubleNode();

        Finder = rear;

        while ((Finder != null) && temp.compareTo((T) Finder.getElement()) > 0) {
            Finder = Finder.getNext();
        }

        if (count == 0) {
            rear = NewNode;
            front = NewNode;
        }

        if (count == 1) {
            if (Finder == rear) {
                rear.setPrevious(NewNode);
                NewNode.setNext(rear);
                rear = NewNode;
            }
            if (Finder == null) {
                rear.setNext(NewNode);
                NewNode.setPrevious(rear);
                front = NewNode;
            }
        }
        if (count == 2) {
            if (Finder == rear) {
                rear.setPrevious(NewNode);
                NewNode.setNext(rear);
                rear = NewNode;
            }
            if (Finder == front) {
                rear.setNext(NewNode);
                NewNode.setPrevious(rear);
                front.setPrevious(NewNode);
                NewNode.setNext(front);
            }
            if (Finder == null) {
                front.setNext(NewNode);
                NewNode.setPrevious(front);
                front = NewNode;
            }
        }

        if (count > 2) {
            if (Finder == rear) {
                NewNode.setNext(rear);
                rear.setPrevious(NewNode);
                rear = NewNode;
            }
            if (Finder == front) {
                NewNode.setPrevious(front.getPrevious());
                NewNode.setNext(front);
                front.getPrevious().setNext(NewNode);
                front.setPrevious(NewNode);
            }
            if (Finder == null) {
                front.setNext(NewNode);
                NewNode.setPrevious(front);
                front = NewNode;
            } else {
                Finder.getPrevious().setNext(NewNode);
                NewNode.setPrevious(Finder.getPrevious());
                NewNode.setNext(Finder);
                Finder.setPrevious(NewNode);
            }
        }
        ModCount++;
        count++;
    }

}
