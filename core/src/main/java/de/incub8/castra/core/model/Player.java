package de.incub8.castra.core.model;

import lombok.Getter;
import lombok.Setter;

import com.badlogic.gdx.graphics.Color;

public class Player
{
    private final PlayerType type;

    @Getter
    private final Color color;

    @Getter
    private final String name;

    @Getter
    @Setter
    private int sendTroopPercentage;

    public Player(Color color, String name, PlayerType type)
    {
        this.color = color;
        this.name = name;
        this.type = type;
        sendTroopPercentage = 50;
    }

    public boolean isHuman()
    {
        return type.equals(PlayerType.HUMAN);
    }

    public boolean isNeutral()
    {
        return type.equals(PlayerType.NEUTRAL);
    }

    public boolean isAi()
    {
        return type.equals(PlayerType.AI);
    }
}