package com.github.workinggames.castra.core.pathfinding;

import lombok.Getter;

import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

@Getter
public class Line
{
    private final Vector2 start;
    private final Vector2 end;
    private final float distance;
    private final Array<Vector2> intersectCheckpoints;

    public Line(Vector2 start, Vector2 end)
    {
        this.start = start;
        this.end = end;
        distance = start.dst(end);
        intersectCheckpoints = getIntersectCheckPoints();
    }

    private Array<Vector2> getIntersectCheckPoints()
    {
        Array<Vector2> result = new Array<>();
        for (int i = 1; i < distance; i++)
        {
            Vector2 slopeVector = new Vector2(end).sub(start).setLength(i);
            Vector2 point = new Vector2(start).add(slopeVector);
            result.add(point);
        }
        return result;
    }

    public boolean intersectsEllipse(Ellipse ellipse)
    {
        boolean intersects = false;
        for (Vector2 point : intersectCheckpoints)
        {
            intersects = ellipse.contains(point);
            if (intersects)
            {
                break;
            }
        }
        return intersects;
    }

    public Vector2 valueAt(float distance)
    {
        Vector2 slopeVector = new Vector2(end).sub(start).setLength(distance);
        return new Vector2(start).add(slopeVector);
    }
}