/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OrderedList;

import java.io.Serializable;

/**
 *
 * @author Diogo Lopes 8180121
 * @param <T> wildtype
 */
public class DoubleNode<T> implements Serializable
{

    private DoubleNode<T> next;
    private T element;
    private DoubleNode<T> previous;

    /**
     * Creates an empty node.
     */
    public DoubleNode() {
        next = null;
        element = null;
        previous = null;
    }

    /**
     * Creates a node storing the specified element.
     *
     * @param elem the element to be stored into the new node
     */
    public DoubleNode(T elem) {
        next = null;
        element = elem;
        previous = null;
    }

    /**
     * Sets the node that precedes this one.
     *
     * @param dnode the node to be set as the one to procede the current one
     */
    public void setNext(DoubleNode<T> dnode) {
        next = dnode;
    }

    /**
     * Returns the node that follows this one.
     *
     * @return the node that follows the current one
     */
    public DoubleNode<T> getNext() {
        return next;
    }

    /**
     * Sets the node that precedes this one.
     *
     * @param dnode the node to be set as the one to precede the current one
     */
    public void setPrevious(DoubleNode<T> dnode) {
        previous = dnode;
    }
    
    /**
     * Returns the node that follows this one.
     *
     * @return the node that follows the current one
     */
    public DoubleNode<T> getPrevious() {
        return previous;
    }
    /**
     * Returns the element stored in this node.
     *
     * @return the element stored in this node
     */
    public T getElement() {
        return element;
    }

    /**
     * Sets the element stored in this node.
     *
     * @param elem the element to be stored in this node
     */
    public void setElement(T elem) {
        element = elem;
    }
}
