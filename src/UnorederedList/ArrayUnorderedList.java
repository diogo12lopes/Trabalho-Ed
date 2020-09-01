/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UnorederedList;

import Exceptions.EmptyCollectionException;
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

    @Override
    public void addAfter(T elementFinder, T NewElement) throws EmptyCollectionException {

        if (Lista.length == 0) {
            throw new EmptyCollectionException("The list is empty...");
        }

        if (size() == Lista.length) {
            ExpandCapacity();
        }

        int scan = 0;

        while (Lista[scan] != elementFinder || scan == rear) {
            scan++;
        }

        if (scan == rear) {
            throw new Error("The list does not contain that element");
        }

        if (scan == rear - 1) {
            addToFront(NewElement);
        } else if (scan == 0) {

            T[] temp = (T[]) new Object[Lista.length];

            temp[0] = Lista[0];

            System.arraycopy(Lista, 1, temp, 2, rear - 1);

            Lista = temp;
            Lista[1] = NewElement;

            rear++;
            ModCount++;
        } else if (scan < rear - 2 && scan != 0 && scan != rear) {

            T[] temp = (T[]) new Object[Lista.length];

            System.arraycopy(Lista, 0, temp, 0, scan + 1);

            for (int i = scan + 1; i < rear; i++) {
                temp[i + 1] = Lista[i];
            }
            Lista = temp;

            Lista[scan + 1] = NewElement;

            rear++;
            ModCount++;
        }
    }
}
