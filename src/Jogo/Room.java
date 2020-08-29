/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jogo;

import Exceptions.ElementNotFoundException;
import Exceptions.EmptyCollectionException;
import UnorederedList.ArrayUnorderedList;

import java.util.Iterator;

/**
 *
 * @author Diogo Lopes 8180121
 */
public class Room implements Comparable {

    private String name;
    private ArrayUnorderedList<Fantasma> ghosts;

    public Room(String name, long Ghost) {
        this.name = name;
    }

    public Room() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotalDamage() {
        long totalDamage = 0;
        Iterator ghostsIterator = ghosts.iterator();
        while (ghostsIterator.hasNext())
            totalDamage += (((Fantasma)ghostsIterator.next()).getDamage());

        return totalDamage;
    }

    public ArrayUnorderedList<Fantasma> getGhosts()
    {
        return ghosts;
    }

    public void setGhosts(ArrayUnorderedList<Fantasma> ghosts)
    {
        this.ghosts = ghosts;
    }

    public void addGhost(Fantasma ghost)
    {
        this.ghosts.addToRear(ghost);
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(Object o) {

        Room Comparator = (Room) o;

        return this.getName().compareTo(Comparator.getName());
    }

    public void removeGhost(Fantasma fantasma) throws ElementNotFoundException, EmptyCollectionException
    {
        this.ghosts.remove(fantasma);
    }
}
