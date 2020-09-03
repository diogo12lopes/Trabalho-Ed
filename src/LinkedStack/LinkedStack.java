/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LinkedStack;

import Exceptions.EmptyCollectionException;
import Interfaces.StackADT;


/**
 *
 * @author Diogo Lopes 8180121
 * @param <T> WildType
 */
public class LinkedStack<T> implements StackADT<T> {

    private int count;
    private LinearNode top;

    public LinkedStack() {
        this.top = new LinearNode();
        count = 0;
    }

    @Override
    public void push(T element) {
   
        LinearNode new_node = new LinearNode(element);

        if (this.top.getNext() == null) {
            top.setNext(new_node);
            new_node.setNext(null);

        } else {
            new_node.setNext(top.getNext());
            top.setNext(new_node);
        }
        count++;
    }

    @Override
    public T pop() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("The stack is empty...");
        } else {
            LinearNode tmp = top.getNext();
            top.setNext(tmp.getNext());
            tmp.setNext(null);
            count --;
            return (T) tmp.getElement();
        }
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public int size() {
       return count;
    }
    

    @Override
    public String toString(){
        if (top.getNext()== null){
            return "Your stack is empty...";
        }
        
        String Stack = "";
                
        LinearNode<T> tmp = top.getNext();
        
        while (tmp != null){
            Stack += "\n" + tmp.getElement().toString();
            tmp = tmp.getNext();
        }
        return Stack;
    } 
}
