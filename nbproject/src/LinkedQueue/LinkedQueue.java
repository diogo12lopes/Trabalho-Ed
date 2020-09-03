/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LinkedQueue;

import Exceptions.EmptyCollectionException;
import Interfaces.QueueADT;

/**
 *
 * @author Diogo Lopes 8180121
 * @param <T> WildType
 */
public class LinkedQueue<T> implements QueueADT<T> {

    private int count;
    private LinearNode rear;
    private LinearNode front;

    public LinkedQueue() {
        this.rear = null;
        this.front = null;
        count = 0;
    }

    @Override
    public void enqueue(T element) {

        LinearNode new_node = new LinearNode(element);

        if (isEmpty()) {
            front = new_node;
            rear = new_node;
        }
        else {
            rear.setNext(new_node);
            rear = new_node;
        }
        count++;
    }

    @Override
    public T dequeue() throws EmptyCollectionException {
        
        T element;
        element = (T) front.getElement();
        front = front.getNext();
        count--;
        return (T) element;
        
    }

    @Override
    public T first() {
        if (isEmpty()){
            return null;
        }
        return (T) front.getElement();
    }

    @Override
    public boolean isEmpty() {
        return (count == 0);
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "Your queue is empty...";
        }

        String Queue = "";

        LinearNode<T> tmp = front;

        while (tmp != null) {
            Queue += tmp.getElement().toString()+ "\n";
            tmp = tmp.getNext();
        }
        return Queue;
    }
}

