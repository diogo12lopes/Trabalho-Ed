package Jogo;

import java.io.Serializable;

public class Fantasma implements Serializable
{
    private long damage;

    /**
     * Cria um fantasma com o valor de dano passado como parâmetro
     * @param damage
     */
    public Fantasma(long damage)
    {
        this.damage = damage;
    }

    /**
     * Devolve o valor do dano que o fantasma provoca
     * @return valor do dano que o fantasma provoca
     */
    public long getDamage()
    {
        return damage;
    }
}
