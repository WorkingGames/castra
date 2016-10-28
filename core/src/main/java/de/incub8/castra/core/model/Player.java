package de.incub8.castra.core.model;

import lombok.Getter;
import lombok.Setter;

public class Player
{
    private final PlayerType type;

    @Getter
    private final PlayerColor color;

    @Getter
    private final int textureIndex;

    @Getter
    private final String name;

    @Getter
    @Setter
    private int sendTroopPercentage;

    public Player(PlayerColor color, int textureIndex, String name, PlayerType type)
    {
        this.color = color;
        this.textureIndex = textureIndex;
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