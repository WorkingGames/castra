package com.github.workinggames.castra.core.pathfinding;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.github.workinggames.castra.core.actor.Settlement;

public class PathSmoother
{
    private final Array<Settlement> settlements;
    private final PathUtils pathUtils;

    public PathSmoother(Array<Settlement> settlements, PathUtils pathUtils)
    {
        this.settlements = settlements;
        this.pathUtils = pathUtils;
    }

    public Array<Line> smoothPath(Settlement origin, Settlement destination, GraphPath<Vector2> graphPath)
    {
        Array<Line> pathLines = new Array<>();
        Array<Vector2> pathPoints = smoothPathPoints(origin, destination, graphPath);
        for (int i = 0; i < pathPoints.size - 1; i++)
        {
            pathLines.add(new Line(pathPoints.get(i), pathPoints.get(i + 1)));
        }
        return pathLines;
    }

    private Array<Vector2> smoothPathPoints(Settlement origin, Settlement destination, GraphPath<Vector2> graphPath)
    {
        Array<Vector2> result = new Array<>();

        Vector2 start = graphPath.get(0);
        result.add(start);

        int endIndex = graphPath.getCount() - 1;
        Vector2 end = graphPath.get(endIndex);

        int stepStartIndex = 0;
        int stepEndIndex = endIndex;
        int maximumIntersectionFreeIndex = 0;
        int lastSuccessfulIndex = 0;

        while (!result.contains(end, true))
        {
            Line line = new Line(graphPath.get(stepStartIndex), graphPath.get(stepEndIndex));
            boolean intersects = pathUtils.intersectsWithOtherSettlements(origin, destination, line, settlements);
            if (!intersects)
            {
                if (graphPath.get(stepEndIndex) == end)
                {
                    // there were no settlements intersecting the complete path
                    result.add(end);
                }
                else
                {
                    if (stepEndIndex > maximumIntersectionFreeIndex)
                    {
                        // new maximum found, setting it and trying a higher stepEndIndex
                        maximumIntersectionFreeIndex = stepEndIndex;
                        lastSuccessfulIndex = stepEndIndex;
                        int step = (int) Math.floor((endIndex - lastSuccessfulIndex) * 0.5f);
                        stepEndIndex = stepEndIndex + step;
                    }
                }
            }
            else
            {
                // reduce the stepEnd to close in on the Largest step index without intersection
                int step = (int) Math.floor((stepEndIndex - lastSuccessfulIndex) * 0.5f);
                if (step == 0)
                {
                    // we found our maximum point
                    result.add(graphPath.get(lastSuccessfulIndex));
                    stepStartIndex = lastSuccessfulIndex;
                    stepEndIndex = endIndex;
                }
                else
                {
                    stepEndIndex = stepEndIndex - step;
                }
            }
        }
        return result;
    }
}