package Jogo;

import directednetworkwithmatrix.DirectedNetworkWithMatrix;

import java.io.Serializable;

public class UndoInfo implements Serializable
{
    private Map SelectedMap;
    private Player player;
    private Room currentRoom;
    private DirectedNetworkWithMatrix<Room> tmp;

    public UndoInfo(Map selectedMap, Player player, Room currentRoom, DirectedNetworkWithMatrix<Room> tmp)
    {
        SelectedMap = selectedMap;
        this.player = player;
        this.currentRoom = currentRoom;
        this.tmp = tmp;
    }

    public Map getSelectedMap()
    {
        return SelectedMap;
    }


    public Player getPlayer()
    {
        return player;
    }

    public Room getCurrentRoom()
    {
        return currentRoom;
    }

    public DirectedNetworkWithMatrix<Room> getTmp()
    {
        return tmp;
    }
}
