package com.github.workinggames.castra.core.stage;

import java.util.Comparator;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class ZAwareActorComparator implements Comparator<Actor>
{
    @Override
    public int compare(Actor actor1, Actor actor2)
    {
        int actor1Z = actor1.getZIndex();
        int actor2Z = actor2.getZIndex();
        if (actor1Z == actor2Z)
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
                return Float.compare(actor2X, actor1X);
            }
        }
        else
        {
            return Integer.compare(actor1Z, actor2Z);
        }
    }
}