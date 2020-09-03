/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.io.Serializable;

/**
 *
 * @author Diogo Lopes 8180121
 * @param <T> wildstypwe
 */
public interface OrderedListADT<T> extends ListADT<T>, Serializable
{

    /**
     * Adds the specified element to this list at the proper location
     *
     * @param element the element to be added to this list
     */
    public void add(T element);
}
