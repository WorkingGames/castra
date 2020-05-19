package com.github.workinggames.castra.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.github.workinggames.castra.core.ai.AiType;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "sendTroopPercentage")
public class Player
{
    private PlayerType type;
    private AiType aiType;
    private PlayerColor color;
    private String name;
    private int sendTroopPercentage = 50;

    public Player(PlayerColor color, String name, PlayerType type)
    {
        this.color = color;
        this.name = name;
        this.type = type;
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