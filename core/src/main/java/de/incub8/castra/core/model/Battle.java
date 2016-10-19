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

    public Battle(Army army)
    {
        this.army = army;
        this.settlement = army.getTarget();
        animation = null;
    }
}