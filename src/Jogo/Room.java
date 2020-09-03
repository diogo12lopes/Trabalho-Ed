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

    /**
     * Cria uma nova instância de  uma divisão com o seu nome e lista de fantasmas presentes na divisão.
     * @param name nome da divisão
     * @param ghosts lista de fantasmas presentes na divisão
     */
    public Room(String name, ArrayUnorderedList<Fantasma> ghosts) {
        this.name = name;
        this.ghosts = ghosts;
    }

    /**
     * Devolve o nome da divisão
     * @return nome da divisão
     */
    public String getName() {
        return name;
    }

    /**
     * Atribui um nome a esta divisão igual ao recebido como parâmetro
     * @param name nome a atribuir à divisão
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Devolve o valor de dano que entrar nesta divisão causa ao jogador, baseado na soma do dano de cada um dos fantasmas presentes na divisão
     * @return valor de dano que entrar nesta divisão causa ao jogador
     */
    public long getTotalDamage() {
        long totalDamage = 0;
        Iterator ghostsIterator = ghosts.iterator();
        while (ghostsIterator.hasNext())
            totalDamage += (((Fantasma)ghostsIterator.next()).getDamage());

        return totalDamage;
    }

    /**
     * Devolve o powerup que entrar na divisão dá, caso exista
     * @return instancia de PowerUp, em que nenhum powerup é indicado po PowwerUp.None
     */
    public PowerUp getPowerUp()
    {
        return powerUp;
    }

    /**
     * Atribui um powerup a esta divisão igual ao recebido como parâmetro
     * @param powerUp powerUp a atribuir à divisão
     */
    public void setPowerUp(PowerUp powerUp)
    {
        this.powerUp = powerUp;
    }

    /**
     * Devolve a lista de fantasmas da divisão
     * @return lista de fantasmas da divisão
     */
    public ArrayUnorderedList<Fantasma> getGhosts()
    {
        return ghosts;
    }

    /**
     * Adiciona um novo fantasma à lista de fantasmas da divisão
     */
    public void addGhost(Fantasma ghost)
    {
        this.ghosts.addToRear(ghost);
    }

    /**
     * Remove o fantasma recebido como parâmetro da lista de fantasmas da divisão
     * @param fantasma fantasma a remover
     * @throws ElementNotFoundException
     * @throws EmptyCollectionException
     */
    public void removeGhost(Fantasma fantasma) throws ElementNotFoundException, EmptyCollectionException
    {
        this.ghosts.remove(fantasma);
    }

    /**
     * Devolve o valor da flag que indica se um fantasma já foi colocado nesta divisão na jogada atual
     * @return valor da flag que indica se um fantasma já foi colocado nesta divisão na jogada atual
     */
    public boolean hasGhostBeenPlacedHereInThisPlay()
    {
        return hasGhostBeenPlacedHereInThisPlay;
    }

    /**
     * Atribui um valor boleano à flag que indica se um fantasma já foi colocado nesta divisão na jogada atual
     * @param hasGhostBeenPlacedHereInThisPlay valor a atribuir à flag
     */
    public void setHasGhostBeenPlacedHereInThisPlay(boolean hasGhostBeenPlacedHereInThisPlay)
    {
        this.hasGhostBeenPlacedHereInThisPlay = hasGhostBeenPlacedHereInThisPlay;
    }

    /**
     * Devolve uma representação textual da divisão, com indicação do seu nome.
     * @return representação textual da divisão, com indicação do seu nome.
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * Compara esta divisão com a divisão recebida como parâmetro, baseado nos seus nomes
     * @param roomObject divisão com a qual se vai comparar este divisão
     * @return 0 se os jogadores têm o mesmo nome. -1 se a representação numerica do nome que representa a divisão recebida for superior à do nome desta divisão
     * 1 se a representação numerica do nome que representa esta divisão for superior à do nome da divisão recebida como parâmetro
     */
    @Override
    public int compareTo(Object roomObject) {

        Room room2 = (Room) roomObject;
        return this.getName().compareTo(room2.getName());
    }
}

