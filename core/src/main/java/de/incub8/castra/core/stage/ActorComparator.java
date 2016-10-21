package de.incub8.castra.core.stage;

import java.util.Comparator;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorComparator implements Comparator<Actor>
{
    @Override
    public int compare(Actor actor1, Actor actor2)
    {
        float actor1Y = actor1.getY() + actor1.getHeight() / 2;
        float actor2Y = actor2.getY() + actor2.getHeight() / 2;
        if (actor1Y < actor2Y)
        {
            return 1;
        }
        else if (actor1Y > actor2Y)
        {
            return -1;
        }
        else
        {
            float actor1X = actor1.getX() + actor1.getWidth() / 2;
            float actor2X = actor2.getX() + actor2.getWidth() / 2;
            if (actor1X < actor2X)
            {
                return 1;
            }
            else if (actor1X > actor2X)
            {
                return -1;
            }
            else
            {
                return 0;
            }
        }
    }
}