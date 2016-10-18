package de.incub8.castra.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;

@Getter
@ToString
@EqualsAndHashCode
public class Army
{
    public static final int WIDTH = 50;
    public static final int HEIGHT = 28;
    private static final float SPEED = 200;

    private final Player owner;
    private final Array<GridPoint2> path;
    private final ArmySize size;
    private final TextureDefinition textureDefinition;

    private int soldiers;
    private int pathPosition;

    public Army(int soldiers, Player owner, Array<GridPoint2> path)
    {
        this.soldiers = soldiers;
        this.owner = owner;
        this.path = path;
        size = ArmySize.bySoldierCount(soldiers);
        textureDefinition = TextureDefinition.ARMY;

        pathPosition = 0;
    }

    public GridPoint2 getPosition()
    {
        return path.get(pathPosition);
    }

    public void move(float deltaTime)
    {
        pathPosition = Math.min(pathPosition + (int) (SPEED * deltaTime), path.size - 1);
    }
}