/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jogo;

/**
 *
 * @author Diogo Lopes 8180121
 */
public class Room implements Comparable {

    private String name;
    private long ghost;

    public Room(String name, long Ghost) {
        this.name = name;
        this.ghost = Ghost;
    }

    public Room() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getGhost() {
        return ghost;
    }

    public void setGhost(long ghost) {
        this.ghost = ghost;
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
}
