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

/**
 * GraphADT defines the interface to a graph data structure.
 *
 * @param <T> WildType
 */
public interface GraphADT<T>
{
 /**
 * Adds a vertex to this graph, associating object with vertex.
 *
 * @param vertex the vertex to be added to this graph
 */
 public void addVertex (T vertex);

 /**
 * Removes an edge between two vertices of this graph.
 *
 * @param vertex1 the first vertex
 * @param vertex2 the second vertex
 */
 public void removeEdge (T vertex1, T vertex2);

 /**
 * Returns true if this graph is empty, false otherwise.
 *
 * @return true if this graph is empty
 */
 public boolean isEmpty();

 /**
 * Returns the number of vertices in this graph.
 *
 * @return the integer number of vertices in this graph
 */

 public int size();
 /**
 * Returns a string representation of the adjacency matrix.
 *
 * @return a string representation of the adjacency matrix
 */

 @Override
 public String toString();
}
