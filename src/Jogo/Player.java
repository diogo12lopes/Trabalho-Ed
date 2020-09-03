/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jogo;

import java.io.Serializable;

/**
 *
 * @author Diogo Lopes 8180121
 */
public class Player implements Comparable, Serializable
{
    private String Username;
    private boolean teletransporte;
    private long LifePoints;
    private long Escudo;

    /**
     * Cria um novo jogador com um dado nome de utilizador e pontos de vida e sem habilidades.
     * @param Username nome de utilizador
     * @param LifePoints pontos de vida
     */
    public Player(String Username, long LifePoints)
    {
        this.Username = Username;
        this.teletransporte = false;
        this.LifePoints = LifePoints;
        this.Escudo = 0;
    }

    /**
     * Verifica se o jogador tem a habilidade de teletransporte
     * @return true se tem a habilidade. Caso contrário, false.
     */
    public boolean hasTeletransporte()
    {
        return teletransporte;
    }

    /**
     * Atribui um valor boleano indicando se o jogador tem a habilidade de teletransporte ou não.
     * @param teletransporte valor boleano indicando se o jogador tem a habilidade de teletransporte ou não.
     */
    public void setTeletransporte(boolean teletransporte)
    {
        this.teletransporte = teletransporte;
    }

    public long getLifePoints()
    {
        return LifePoints;
    }

    /**
     * Atribui um valor numérico indicando os pontos de vida do jogador.
     * @param lifePoints valor numérico indicando os pontos de vida do jogador.
     */
    public void setLifePoints(long lifePoints)
    {
        LifePoints = lifePoints;
    }

    public long getEscudo()
    {
        return Escudo;
    }

    /**
     * Atribui um valor numérico indicando o valor do escudo do jogador.
     * @param escudo valor numérico indicando o valor do escudo do jogador.
     */
    public void setEscudo(long escudo)
    {
        Escudo = escudo;
    }

    /**
     * Compara este jogador com o jogador recebido como parâmetro
     * @param playerObject jogador com o qual se vai comparar este jogador
     * @return 0 se os jogadores têm os mesmo pontos de vida. -1 se o jogador recebido como parâmetro tiver mais pontos de vida. 1 se este jogador tiver mais pontos de vida.
     */
    @Override
    public int compareTo(Object playerObject)
    {
        Player player2 = (Player) playerObject;
        return -(Long.valueOf(this.getLifePoints()).compareTo(player2.getLifePoints()));
    }

    /**
     * Devolve uma representação textual deste jogador, com indicação de nome de utilizador e pontos de vida.
     * @return representação textual deste jogador, com indicação de nome de utilizador e pontos de vida.
     */
    @Override
    public String toString() {
        return "Player:"+this.Username+"\n"+"Score:"+this.LifePoints+"\n";
    }
}
