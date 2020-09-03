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
    private long Escudo;//TODO: ver nomes

    public Player(String Username, long LifePoints) {
        this.Username = Username;
        this.teletransporte = false;
        this.LifePoints = LifePoints;
        this.Escudo = 0;
    }

    public boolean hasTeletransporte()
    {
        return teletransporte;
    }

    public void setTeletransporte(boolean teletransporte)
    {
        this.teletransporte = teletransporte;
    }

    public long getLifePoints()
    {
        return LifePoints;
    }

    public void setLifePoints(long lifePoints)
    {
        LifePoints = lifePoints;
    }

    public long getEscudo()
    {
        return Escudo;
    }

    public void setEscudo(long escudo)
    {
        Escudo = escudo;
    }

    @Override
    public int compareTo(Object Comparator) {

        Player tmp = (Player) Comparator;

        return -(Long.valueOf(this.getLifePoints()).compareTo(tmp.getLifePoints()));
    }

    @Override
    public String toString() {
        return "Player:"+this.Username+"\n"+"Score:"+this.LifePoints+"\n";
    }
}
