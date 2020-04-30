package com.github.workinggames.castra.core.pathfinding;

import lombok.Getter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class LinePath
{
    private final Array<Line> lines;

    @Getter
    private float distance;

    public LinePath(Array<Line> lines)
    {
        this.lines = lines;
        for (Line line : lines)
        {
            distance += line.getDistance();
        }
    }

    /**
     * @param time ranges between 0 <= time <= 1
     */
    public Vector2 valueAt(float time)
    {
        Vector2 result = null;
        float relativeDistance = distance * time;
        float walkedDistance = 0;
        for (Line line : lines)
        {
            if (walkedDistance + line.getDistance() < relativeDistance)
            {
                walkedDistance += line.getDistance();
            }
            else
            {
                float openLineDistance = relativeDistance - walkedDistance;
                result = line.valueAt(openLineDistance);
                break;
            }
        }
        return result;
    }
}