/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;
import Exceptions.ElementNotFoundException;
import Exceptions.EmptyCollectionException;
import java.util.Iterator;

/**
 *
 * @author Diogo Lopes 8180121
 * @param <T> wildtype
 */
public interface BinaryTreeADT<T> {

    /**
     * Returns true if this binary tree is empty and false otherwise.
     *
     * @return true if this binary tree is empty
     */
    boolean isEmpty();

    /**
     * Returns the number of elements in this binary tree.
     *
     * @return the integer number of elements in this tree
     */
    int size();

    /**
     * Returns true if the binary tree contains an element that matches the
     * specified element and false otherwise.
     *
     * @param targetElement the element being sought in the tree
     * @return true if the tree contains the target element
     */
    boolean contains(T targetElement);

    /**
     * Returns a reference to the specified element if it is found in this
     * binary tree. Throws an exception if the specified element is not found.
     *
     * @param targetElement  the element being sought in the tree
     * @return a reference to the specified element
     * @throws Exceptions.ElementNotFoundException Exception
     
     */
    T find(T targetElement)throws ElementNotFoundException;

    /**
     * Returns the string representation of the binary tree.
     *
     * @return a string representation of the binary tree
     */
    @Override
    String toString();
}
