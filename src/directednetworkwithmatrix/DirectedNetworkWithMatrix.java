/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package directednetworkwithmatrix;

import Exceptions.EmptyCollectionException;
import Heap.ArrayHeap;
import Interfaces.NetworkADT;
import LinkedQueue.LinkedQueue;
import LinkedStack.LinkedStack;
import UnorederedList.ArrayUnorderedList;
import java.util.Iterator;

/**
 *
 * @author Diogo Lopes 8180121
 * @param <T> wildtype
 */
public class DirectedNetworkWithMatrix<T> implements NetworkADT<T> {

    protected final int DEFAULT_CAPACITY = 10;
    protected int numVertices; // number of vertices in the graph
    protected boolean[][] adjMatrix; // adjacency matrix
    protected double[][] weightMatrix;// weights matrix
    protected T[] vertices; // values of vertices

    /**
     * Creates an empty graph.
     */
    public DirectedNetworkWithMatrix() {
        numVertices = 0;
        this.adjMatrix = new boolean[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        this.weightMatrix = new double[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        this.vertices = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    /**
     * Inserts an edge between two vertices of the graph.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     */
    @Override
    public void addEdge(T vertex1, T vertex2, double weight) {
        addEdge(getIndex(vertex1), getIndex(vertex2), weight);
    }

    @Override
    public void addEdge(T vertex1, T vertex2) {
        addEdge(getIndex(vertex1), getIndex(vertex2));
    }

    /**
     * Inserts an edge between two vertices of the graph.
     *
     * @param index1 the first index
     * @param index2 the second index
     */
    public void addEdge(int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = true;
            weightMatrix[index1][index2] = 0;
        }
    }

    /**
     * Inserts an edge between two vertices of the graph.
     *
     * @param index1 Index From
     * @param index2 Index TO
     * @param weight wight of the path
     */
    public void addEdge(int index1, int index2, double weight) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = true;
            weightMatrix[index1][index2] = weight;
        }
    }

    /**
     * Adds a vertex to the graph, expanding the capacity of the graph if
     * necessary. It also associates an object with the vertex.
     *
     * @param vertex the vertex to add to the graph
     */
    @Override
    public void addVertex(T vertex) {

        if (numVertices == vertices.length) {
            expandCapacity();
        }

        for (int i = 0; i <= numVertices; i++) {
            weightMatrix[numVertices][i] = Double.POSITIVE_INFINITY;
            weightMatrix[i][numVertices] = Double.POSITIVE_INFINITY;
        }

        vertices[numVertices] = vertex;

        numVertices++;
    }

    @Override
    public void removeVertex(T vertex) {

        if (indexIsValid(getIndex(vertex))) {

            int valor = getIndex(vertex);

            for (int i = valor; i < numVertices; i++) {
                vertices[i + 1] = vertices[i];
            }

            vertices[numVertices - 1] = null;

            for (int i = valor; i < numVertices; i++) {
                adjMatrix[i] = adjMatrix[i + 1];

                for (int k = valor; k < numVertices; i++) {
                    adjMatrix[i][k] = adjMatrix[i][k + 1];
                }
            }
            for (int i = valor; i < numVertices; i++) {
                weightMatrix[i] = weightMatrix[i + 1];

                for (int k = valor; k < numVertices; i++) {
                    weightMatrix[i][k] = weightMatrix[i][k + 1];
                }
            }
            //Debugging
            for (int k = 0; k < numVertices; k++) {
                for (int i = 0; i < numVertices; i++) {
                    System.out.println(adjMatrix[i][k]);
                }
                System.out.println("\n");
            }
            ////
            numVertices--;
        }
    }

    @Override
    public void removeEdge(T vertex1, T vertex2) {
        if (indexIsValid(getIndex(vertex1)) && indexIsValid(getIndex(vertex2))) {
            adjMatrix[getIndex(vertex1)][getIndex(vertex2)] = false;
            weightMatrix[getIndex(vertex1)][getIndex(vertex2)] = 0;
        }
    }

    /**
     * Returns an iterator that performs a breadth first search traversal
     * starting at the given index.
     *
     * @return an iterator that performs a breadth first traversal
     */
    @Override
    public Iterator<T> iteratorBFS(T startVertex) throws EmptyCollectionException {
        Integer x;
        LinkedQueue<Integer> traversalQueue = new LinkedQueue<Integer>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<T>();
        if (!indexIsValid(getIndex(startVertex))) {
            return resultList.iterator();
        }
        boolean[] visited = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }

        traversalQueue.enqueue(new Integer(getIndex(startVertex)));
        visited[getIndex(startVertex)] = true;

        while (!traversalQueue.isEmpty()) {
            x = traversalQueue.dequeue();
            resultList.addToFront(vertices[x.intValue()]);
            /**
             * Find all vertices adjacent to x that have not been visited and
             * queue them up
             */
            for (int i = 0; i < numVertices; i++) {
                if (adjMatrix[x.intValue()][i] && !visited[i]) {
                    traversalQueue.enqueue(new Integer(i));
                    visited[i] = true;
                }
            }
        }
        return resultList.iterator();
    }

    /**
     * Returns an iterator that performs a depth first search traversal starting
     * at the given index.
     *
     * @return an iterator that performs a depth first traversal
     */
    @Override
    public Iterator<T> iteratorDFS(T startVertex) throws EmptyCollectionException {
        Integer x;
        boolean found;
        LinkedStack<Integer> traversalStack = new LinkedStack<Integer>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<T>();
        boolean[] visited = new boolean[numVertices];
        if (!indexIsValid(getIndex(startVertex))) {
            return resultList.iterator();
        }
        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }

        traversalStack.push(new Integer(getIndex(startVertex)));
        resultList.addToRear(vertices[getIndex(startVertex)]);
        visited[getIndex(startVertex)] = true;

        while (!traversalStack.isEmpty()) {
            x = traversalStack.peek();
            found = false;
            /**
             * Find a vertex adjacent to x that has not been visited and push it
             * on the stack
             */
            for (int i = 0; (i < numVertices) && !found; i++) {
                if (adjMatrix[x.intValue()][i] && !visited[i]) {
                    traversalStack.push(new Integer(i));
                    resultList.addToRear(vertices[i]);
                    visited[i] = true;
                    found = true;
                }
            }
            if (!found && !traversalStack.isEmpty()) {
                traversalStack.pop();
            }
        }
        return resultList.iterator();
    }

    @Override
    public Iterator iteratorShortestPath(T startVertex, T targetVertex) {

        int startIndex = getIndex(startVertex);
        int targetIndex = getIndex(targetVertex);

        LinkedQueue<Integer> traversalQueue = new LinkedQueue<>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();

        if (indexIsValid(startIndex) == false || indexIsValid(targetIndex) == false) {
            return resultList.iterator();
        }

        int x = startIndex;
        int[] size = new int[this.numVertices];
        int[] previous = new int[this.numVertices];
        boolean[] visited = new boolean[this.numVertices];

        for (int i = 0; i < this.numVertices; i++) {
            visited[i] = false;
        }

        traversalQueue.enqueue(startIndex);
        visited[startIndex] = true;
        size[startIndex] = 0;
        previous[startIndex] = -1;

        while (!traversalQueue.isEmpty() && (x != targetIndex)) {
            try {
                x = traversalQueue.dequeue();
            } catch (EmptyCollectionException ex) {
            }
            for (int i = 0; i < this.numVertices; i++) {
                if (this.adjMatrix[x][i] && !visited[i]) {
                    size[i] = size[x] + 1;
                    previous[i] = x;
                    traversalQueue.enqueue(i);
                    visited[i] = true;
                }
            }
        }

        if (x != targetIndex) {
            return resultList.iterator();
        }

        LinkedStack<T> invertedResult = new LinkedStack<>();
        x = targetIndex;
        invertedResult.push(this.vertices[x]);

        do {
            x = previous[x];
            invertedResult.push(this.vertices[x]);
        } while (x != startIndex);

        while (!invertedResult.isEmpty()) {
            try {
                resultList.addToRear((invertedResult.pop()));
            } catch (EmptyCollectionException ex) {
            }
        }
        return resultList.iterator();
    }

    @Override
    public boolean isEmpty() {
        if (numVertices == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isConnected() throws EmptyCollectionException {
        Iterator it = iteratorBFS(vertices[0]);

        int Counter = 0;

        while (it.hasNext()) {
            Counter++;
        }
        return Counter == numVertices;
    }

    @Override
    public int size() {
        return numVertices;
    }

    public int getIndex(T vertex) {

        Comparable<T> Comparator = (Comparable<T>) vertex;

        int i = 0;

        while (Comparator.compareTo(vertices[i]) != 0 && i < vertices.length) {
            i++;
        }

        if (i == vertices.length) {
            return -1;
        }

        return i;
    }

    public boolean indexIsValid(int Index) {
        if (Index == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void expandCapacity() {

        boolean[][] tmp = new boolean[adjMatrix.length + DEFAULT_CAPACITY][adjMatrix.length + DEFAULT_CAPACITY];

        double[][] tmp2 = new double[adjMatrix.length + DEFAULT_CAPACITY][adjMatrix.length + DEFAULT_CAPACITY];

        T[] temp = (T[]) new Object[numVertices + DEFAULT_CAPACITY];

        for (int i = 0; i < adjMatrix.length; i++) {
            System.arraycopy(adjMatrix[i], 0, tmp[i], 0, numVertices);
        }

        for (int i = 0; i < weightMatrix.length; i++) {
            System.arraycopy(weightMatrix[i], 0, tmp2[i], 0, numVertices);
        }

        System.arraycopy(vertices, 0, temp, 0, numVertices);

        vertices = temp;
        adjMatrix = tmp;
        weightMatrix = tmp2;
    }

    public double[][] getWeightMatrix()
    {
        return weightMatrix;
    }


    @Override
    public String toString() {
       if (numVertices == 0) {
            return "Graph is empty";
        }

        String result = new String("");

        /**
         * Print the adjacency Matrix
         */
        result += "Adjacency Matrix\n";
        result += "----------------\n";
        result += "index\t";

        for (int i = 0; i < numVertices; i++) {
            result += "" + i;
            if (i < 10) {
                result += " ";
            }
        }
        result += "\n\n";

        for (int i = 0; i < numVertices; i++) {
            result += "" + i + "\t";

            for (int j = 0; j < numVertices; j++) {
                if (weightMatrix[i][j] < Double.POSITIVE_INFINITY) {
                    result += "1 ";
                } else {
                    result += "0 ";
                }
            }
            result += "\n";
        }

        /**
         * Print the vertex values
         */
        result += "\n\nVertex Values";
        result += "\n-------------\n";
        result += "index\tvalue\n\n";

        for (int i = 0; i < numVertices; i++) {
            result += "" + i + "\t";
            result += vertices[i].toString() + "\n";
        }

        /**
         * Print the weights of the edges
         */
        result += "\n\nWeights of Edges";
        result += "\n----------------\n";
        result += "index\tweight\n\n";

        for (int i = 0; i < numVertices; i++) {
            for (int j = numVertices - 1; j > i; j--) {
                if (weightMatrix[i][j] < Double.POSITIVE_INFINITY) {
                    result += i + " to " + j + "\t";
                    result += weightMatrix[i][j] + "\n";
                }
                if (weightMatrix[j][i] < Double.POSITIVE_INFINITY) {
                    result += j + " to " + i + "\t";
                    result += weightMatrix[j][i] + "\n";
                }
            }
        }

        result += "\n";
        return result;
    }

    public int[] GetIndexOfAdjVertices(T Vertex) {

        int[] tempArray = new int[numVertices];

        for (int i = 0; i < numVertices; i++) {
            tempArray[i] = -1;
        }

        int k = 0;

        for (int i = 0; i < numVertices; i++) {
            if (adjMatrix[getIndex(Vertex)][i]) {
                tempArray[k] = i;
                k++;
            }
        }

        int j = 0;
        int[] resultArray = new int[k];
        for (int i = 0; i < numVertices; i++) {
            if (tempArray[i] != -1) {
                resultArray[j] = tempArray[i];
                j++;
            }
        }

        return resultArray;
    }

    public int[] GetIndexOfPreviousVertices(T Vertex) {

        int[] tempArray = new int[numVertices];

        for (int i = 0; i < numVertices; i++) {
            tempArray[i] = -1;
        }

        int k = 0;

        for (int i = 0; i < numVertices; i++) {
            if (adjMatrix[i][getIndex(Vertex)]) {
                tempArray[k] = i;
                k++;
            }
        }

        int j = 0;
        int[] resultArray = new int[k];
        for (int i = 0; i < numVertices; i++) {
            if (tempArray[i] != -1) {
                resultArray[j] = tempArray[i];
                j++;
            }
        }

        return resultArray;
    }

    public Object[] getVertices() {
        return vertices;
    }

    @Override
    public double shortestPathWeight(T vertex1, T vertex2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Iterator iteratorShortestPathWeight(T startVertex, T targetVertex) throws EmptyCollectionException {

        int startIndex = getIndex(startVertex);
        int targetIndex = getIndex(targetVertex);
        int index;
        double weight;
        int[] predecessor = new int[numVertices];
        ArrayHeap<Double> traversalMinHeap = new ArrayHeap<>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();
        LinkedStack<Integer> stack = new LinkedStack<>();

        double[] pathWeight = new double[numVertices];
        for (int i = 0; i < numVertices; i++) {
            pathWeight[i] = Double.POSITIVE_INFINITY;
        }

        boolean[] visited = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }

        if (!indexIsValid(startIndex) || !indexIsValid(targetIndex) || (startIndex == targetIndex) || isEmpty()) {
            return resultList.iterator();
        }

        pathWeight[startIndex] = 0;
        predecessor[startIndex] = -1;
        visited[startIndex] = true;

        //atualiza o pathWeight de cada vertice
        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                pathWeight[i] = pathWeight[startIndex] + weightMatrix[startIndex][i];

                predecessor[i] = startIndex;
                traversalMinHeap.addElement(pathWeight[i]);
            }
        }

        do {
            weight = traversalMinHeap.removeMin();

            traversalMinHeap.removeAllElements();
            if (weight == Double.POSITIVE_INFINITY) // sem caminho possivel
            {
                return resultList.iterator();
            } else {
                index = getIndexOfAdjVertexWithWeightOf(visited, pathWeight, weight);
                visited[index] = true;
            }

            //atualiza o pathWeight de cada vertice
            for (int i = 0; i < numVertices; i++) {
                if (!visited[i]) {
                    if ((weightMatrix[index][i] < Double.POSITIVE_INFINITY) && (pathWeight[index] + weightMatrix[index][i]) < pathWeight[i]) {
                        pathWeight[i] = pathWeight[index] + weightMatrix[index][i];

                        predecessor[i] = index;
                    }
                    traversalMinHeap.addElement(pathWeight[i]);
                }
            }
        } while (!traversalMinHeap.isEmpty() && !visited[targetIndex]);

        index = targetIndex;
        stack.push(index);
        do {
            index = predecessor[index];
            stack.push(index);
        } while (index != startIndex);

        while (!stack.isEmpty()) {
            resultList.addToFront(vertices[(stack.pop())]);
        }
        return resultList.iterator();
    }

    private int getIndexOfAdjVertexWithWeightOf(boolean[] visited, double[] pathWeight, double weight) {
        for (int i = 0; i < numVertices; i++) {
            if ((pathWeight[i] == weight) && !visited[i]) {
                return i;

            }
        }
        System.out.println("erro");
        return -1;
    }
}
