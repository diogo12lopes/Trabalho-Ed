/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UnorederedList;

import Interfaces.UnorderedListADT;

/**
 *
 * @author Diogo Lopes 8180121
 * @param <T> wildtype
 */
public class ArrayUnorderedList<T> extends List<T> implements UnorderedListADT<T> {

    public ArrayUnorderedList() {
        super();
    }

    public ArrayUnorderedList(int size) {
        super(size);
    }

    @Override
    public void addToFront(T element) {

        if (size() == Lista.length) {
            ExpandCapacity();
        }

        T[] temp = (T[]) new Object[Lista.length];

        for (int i = 0; i < rear; i++) {
            temp[i + 1] = Lista[i];
        }

        Lista = temp;
        Lista[0] = element;

        rear++;
        ModCount++;
    }

    @Override
    public void addToRear(T element) {

        if (size() == Lista.length) {
            ExpandCapacity();
        }

        Lista[rear] = element;

        rear++;
        ModCount++;
    }
}
