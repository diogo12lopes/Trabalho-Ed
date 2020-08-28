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
public class PlayerInformation implements Comparable {

    private String Username;
    private long Score;

    public PlayerInformation(String Username, long Score) {
        this.Username = Username;
        this.Score = Score;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public long getScore() {
        return Score;
    }

    public void setScore(long Score) {
        this.Score = Score;
    }

    @Override
    public int compareTo(Object Comparator) {

        PlayerInformation tmp = (PlayerInformation) Comparator;

        return -(Long.valueOf(this.getScore()).compareTo(tmp.getScore()));
    }

    @Override
    public String toString() {
        return "Player:"+this.Username+"\n"+"Score:"+this.Score+"\n";
    }
}
