package de.incub8.castra.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import com.badlogic.gdx.graphics.g2d.Animation;

@Getter
@ToString
@EqualsAndHashCode
public class Battle
{
    private final Settlement settlement;
    private final Army army;
    private final Animation animation;

    public Battle(Settlement settlement, Army army)
    {
        this.settlement = settlement;
        this.army = army;
        animation = null;
    }
}