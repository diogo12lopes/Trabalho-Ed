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

public interface GraphADT<T>
{   
 public void addVertex (T vertex);

 public void removeEdge (T vertex1, T vertex2);

 public boolean isEmpty();
 
 public int size();
 
 @Override
 public String toString();
}
