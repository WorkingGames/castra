package de.incub8.castra.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

@Getter
@ToString
@EqualsAndHashCode
public class Army
{
    public static final int WIDTH = 50;
    public static final int HEIGHT = 28;

    private final Player owner;
    private final Array<Vector2> path;
    private final ArmySize size;
    private final Animation animation;

    private int soldiers;
    private int pathPosition;

    public Army(int soldiers, Player owner, Array<Vector2> path)
    {
        this.soldiers = soldiers;
        this.owner = owner;
        this.path = path;
        size = ArmySize.bySoldierCount(soldiers);
        animation = null;

        pathPosition = 0;
    }
}