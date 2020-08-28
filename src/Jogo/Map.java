/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jogo;

import OrderedList.DoubleLinkedOrderedList;
import directednetworkwithmatrix.DirectedNetworkWithMatrix;

/**
 *
 * @author Diogo Lopes 8180121
 */
public class Map {

    private String Name;
    private long InitialLifePoints;
    private Room MapStartingLocation;
    private DoubleLinkedOrderedList<PlayerInformation> MapLeaderboard;
    private DirectedNetworkWithMatrix<Room> map;

    public Map(String Name, long InitialLifePoints) {
        this.Name = Name;
        this.InitialLifePoints = InitialLifePoints;
        this.MapLeaderboard = new DoubleLinkedOrderedList();
        this.map = new DirectedNetworkWithMatrix();
    }

    public Room getMapStartingLocation() {
        return MapStartingLocation;
    }

    public void setMapStartingLocation(Room MapStartingLocation) {
        this.MapStartingLocation = MapStartingLocation;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public long getInitialLifePoints() {
        return InitialLifePoints;
    }

    public DirectedNetworkWithMatrix<Room> getMap() {
        return map;
    }

    public DoubleLinkedOrderedList getMapLeaderboard() {
        return MapLeaderboard;
    }

    public void setInitialLifePoints(long InitialLifePoints) {
        this.InitialLifePoints = InitialLifePoints;
    }

    public void AddToTheLeaderboard(PlayerInformation Info) {
        this.MapLeaderboard.add(Info);
    }
}
