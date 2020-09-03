/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jogo;

import Exceptions.ElementNotFoundException;
import Exceptions.EmptyCollectionException;
import UnorederedList.ArrayUnorderedList;

import java.io.Serializable;
import java.util.Iterator;

/**
 *
 * @author Diogo Lopes 8180121
 */
public class Room implements Comparable, Serializable
{
    private String name;
    private ArrayUnorderedList<Fantasma> ghosts;
    private PowerUp powerUp = PowerUp.None;
    private boolean hasGhostBeenPlacedHereInThisPlay = false;

    public Room(String name, ArrayUnorderedList<Fantasma> ghosts) {
        this.name = name;
        this.ghosts = ghosts;
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

    public PowerUp getPowerUp()
    {
        return powerUp;
    }

    public void setPowerUp(PowerUp powerUp)
    {
        this.powerUp = powerUp;
    }


    public ArrayUnorderedList<Fantasma> getGhosts()
    {
        return ghosts;
    }

    public void addGhost(Fantasma ghost)
    {
        this.ghosts.addToRear(ghost);
    }

    public boolean hasGhostBeenPlacedHereInThisPlay()
    {
        return hasGhostBeenPlacedHereInThisPlay;
    }

    public void setHasGhostBeenPlacedHereInThisPlay(boolean hasGhostBeenPlacedHereInThisPlay)
    {
        this.hasGhostBeenPlacedHereInThisPlay = hasGhostBeenPlacedHereInThisPlay;
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

