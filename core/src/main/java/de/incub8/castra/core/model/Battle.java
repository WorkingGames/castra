package de.incub8.castra.core.model;

import lombok.Getter;

import com.badlogic.gdx.graphics.g2d.Animation;
import de.incub8.castra.core.actor.Army;
import de.incub8.castra.core.actor.Settlement;

@Getter
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