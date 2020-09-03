package Jogo;

import directednetworkwithmatrix.DirectedNetworkWithMatrix;

import java.io.Serializable;

public class UndoInfo implements Serializable
{
    private Map SelectedMap;
    private Player player;
    private Room currentRoom;
    private DirectedNetworkWithMatrix<Room> tmp;

    /**
     * Cria um objeto UndoInfo que contém a informação necessária para fazer um undo de uma jogada do jogo Casa assombrada
     * @param selectedMap mapa atualmente selecionado
     * @param player informação do jogador que está atualmente a realizar um jogo
     * @param currentRoom divisão atual do jogo
     * @param tmp grafo do mapa de jogo que representa todo o estado do jogo
     */
    public UndoInfo(Map selectedMap, Player player, Room currentRoom, DirectedNetworkWithMatrix<Room> tmp)
    {
        SelectedMap = selectedMap;
        this.player = player;
        this.currentRoom = currentRoom;
        this.tmp = tmp;
    }

    /**
     * Devolve o mapa atualmente selecionado
     * @return mapa atualmente selecionado
     */
    public Map getSelectedMap()
    {
        return SelectedMap;
    }

    /**
     * Devolve informação do jogador que está atualmente a realizar um jogo
     * @return informação do jogador que está atualmente a realizar um jogo
     */
    public Player getPlayer()
    {
        return player;
    }

    /**
     * Devolve a divisão atual do jogo
     * @return divisão atual do jogo
     */
    public Room getCurrentRoom()
    {
        return currentRoom;
    }

    /**
     * Devolve o grafo do mapa de jogo que representa todo o estado do jogo
     * @return grafo do mapa de jogo que representa todo o estado do jogo
     */
    public DirectedNetworkWithMatrix<Room> getTmp()
    {
        return tmp;
    }
}
