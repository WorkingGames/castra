package de.incub8.castra.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.badlogic.gdx.graphics.Color;

@Getter
@ToString
@EqualsAndHashCode
public class Player
{
    private final Color color;
    private final String name;
    private final PlayerType type;
    @Setter
    private int sendTroopPercentage;

    public Player(Color color, String name, PlayerType type)
    {
        this.color = color;
        this.name = name;
        this.type = type;
        sendTroopPercentage = 50;
    }
}