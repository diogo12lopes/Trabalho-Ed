/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

/**
 *
 * @author Diogo Lopes 8180121
 */

import java.io.Serializable;

/**
 * NetworkADT defines the interface to a network.
 *
 * @param <T> wildtyypw
 */
public interface NetworkADT<T> extends GraphADT<T>, Serializable
{
    
    public void addEdge(T vertex1, T vertex2, double weight);
}
