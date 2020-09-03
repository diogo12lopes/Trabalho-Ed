/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Heap;

import Exceptions.ElementNotFoundException;
import Exceptions.EmptyCollectionException;
import Interfaces.BinaryTreeADT;
import LinkedQueue.LinkedQueue;
import UnorederedList.ArrayUnorderedList;

/**
 *
 * @author Diogo Lopes 8180121
 *
 */
public class ArrayBinaryTree<T> implements BinaryTreeADT<T> {

    protected int count;
    protected T[] tree;
    private final int CAPACITY = 50;

  
    public ArrayBinaryTree() {
        count = 0;
        tree = (T[]) new Object[CAPACITY];
    }

    public ArrayBinaryTree(T element) {
        count = 1;
        tree = (T[]) new Object[CAPACITY];
        tree[0] = element;
    }

    @Override
    public T find(T targetElement) throws ElementNotFoundException {
        T temp = null;
        boolean found = false;

        for (int ct = 0; ct < count && !found; ct++) {
            if (targetElement.equals(tree[ct])) {
                found = true;
                temp = tree[ct];
            }
        }
        if (!found) {
            throw new ElementNotFoundException("Binary tree");
        }
        return temp;
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
    public boolean contains(T targetElement) {
        try {
            find(targetElement);
        } catch (ElementNotFoundException ex) {
            return false;
        }
        return true;
    }

    protected void inorder(int node, ArrayUnorderedList<T> templist) {
        if (node < tree.length) {
            if (tree[node] != null) {
                inorder(node * 2 + 1, templist);
                templist.addToRear(tree[node]);
                inorder((node + 1) * 2, templist);
            }
        }
    }

    protected void preorder(int node, ArrayUnorderedList<T> templist) {
        if (node < tree.length) {
            if (tree[node] != null) {
                templist.addToFront(tree[node]);
                preorder(node * 2 + 1, templist);
                preorder((node + 1) * 2, templist);
            }
        }
    }

    protected void postorder(int node, ArrayUnorderedList<T> templist) {
        if (node < tree.length) {
            if (tree[node] != null) {
                postorder(node * 2 + 1, templist);
                postorder((node + 1) * 2, templist);
                templist.addToRear(tree[node]);
            }
        }
    }

    protected void levelorder(int node, ArrayUnorderedList<T> templist, LinkedQueue tempQueue) throws EmptyCollectionException {
        if (node < tree.length) {
            if (tree[node] != null) {
                tempQueue.enqueue(tree[node]);
                while (!tempQueue.isEmpty()) {
                    T temp = (T) tempQueue.dequeue();
                    templist.addToFront(temp);
                    tempQueue.enqueue(tree[node + 1]);
                }
            }
        }
    }

    protected void expandCapacity() {
        T[] newArray = (T[]) new Object[size() + CAPACITY];
        System.arraycopy(tree, 0, newArray, 0, size());
        tree = newArray;
    }

    public void removeAllElements() {
        count = 0;

        for (int ct = 0; ct < tree.length; ct++) {
            tree[ct] = null;
        }
    }
}
