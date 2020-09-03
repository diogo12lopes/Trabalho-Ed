/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jogo;

import OrderedList.DoubleLinkedOrderedList;
import directednetworkwithmatrix.DirectedNetworkWithMatrix;

import java.io.Serializable;

/**
 *
 * @author Diogo Lopes 8180121
 */
public class Map implements Serializable
{
    private String Name;
    private long InitialLifePoints;
    private Room MapStartingLocation;
    private DoubleLinkedOrderedList<Player> MapLeaderboard;
    private DirectedNetworkWithMatrix<Room> map;

    /**
     * Cria um novo mapa apenas com o nome e pontos de vida iniciais do mapa. Inicializa ainda as estruturas de dados a serem usadas para posterior criação do mapa.
     * @param Name
     * @param InitialLifePoints
     */
    public Map(String Name, long InitialLifePoints) {
        this.Name = Name;
        this.InitialLifePoints = InitialLifePoints;
        this.MapLeaderboard = new DoubleLinkedOrderedList();
        this.map = new DirectedNetworkWithMatrix();
    }

    /**
     * Devolve a localização inicial deste mapa
     * @return localização inicial deste mapa
     */
    public Room getMapStartingLocation() {
        return MapStartingLocation;
    }

    /**
     * Set da divisão que representa a posição de partida neste mapa
     * @param MapStartingLocation divisão que representa a posição de partida neste mapa
     */
    public void setMapStartingLocation(Room MapStartingLocation) {
        this.MapStartingLocation = MapStartingLocation;
    }

    /**
     * Devolve o nome deste mapa
     * @return nome deste mapa
     */
    public String getName() {
        return Name;
    }

    /**
     * Set do nome para o mapa
     * @param Name nome para o mapa
     */
    public void setName(String Name) {
        this.Name = Name;
    }

    /**
     * Devolve os pontos de vida iniciais deste mapa
     * @return pontos de vida iniciais deste mapa
     */
    public long getInitialLifePoints() {
        return InitialLifePoints;
    }

    /**
     * Devolve a estrutura de dados (grafo) que representa o mapa
     * @return grafo que representa o mapa
     */
    public DirectedNetworkWithMatrix<Room> getGraph() {
        return map;
    }

    /**
     * Devolve o leaderboard deste mapa
     * @return leaderboard deste mapa
     */
    public DoubleLinkedOrderedList getMapLeaderboard() {
        return MapLeaderboard;
    }

    /**
     * Adiciona ao leaderboard informação de um jogador (com a sua classificação)
     * @param Info informação do jogador
     */
    public void AddToTheLeaderboard(Player Info) {
        this.MapLeaderboard.add(Info);
    }

    /**
     * Devolve o máximo de dano que uma divisão deste mapa pode provocar no jogador
     * @return máximo de dano que uma divisão deste mapa pode provocar no jogador
     */
    public long getMaxDivisionDamage()
    {
        long biggestGhost = 0;
        for(Object obj : map.getVertices())
        {
            Room currRoom = (Room) obj;
            if(currRoom.getTotalDamage() > biggestGhost)
                biggestGhost = currRoom.getTotalDamage();
        }

        return biggestGhost;
    }
}
