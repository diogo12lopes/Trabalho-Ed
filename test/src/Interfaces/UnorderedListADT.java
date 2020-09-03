/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Exceptions.EmptyCollectionException;


/**
 *
 * @author Diogo Lopes 8180121
 * @param <T> WildType
 */
public interface UnorderedListADT<T> extends ListADT<T> {
    
    /**
     * Adds the specified element to the front of the list
     *
     * @param element the element to be added to this list
     */
    public void addToFront(T element);
    
    /**
     * Adds the specified element to rear of the list
     *
     * @param element the element to be added to the rear of the list
     */
    public void addToRear(T element);
    
    /**
     * Adds the specifed element to the position after an identified element of the list
     *
     * @param elementFinder the position element to find the locotaion as where to put the new element
     * @param NewElment Element to add
     * @throws Exceptions.EmptyCollectionException  Exception
     * 
     */
    public void addAfter(T elementFinder,T NewElment)throws EmptyCollectionException;
}
