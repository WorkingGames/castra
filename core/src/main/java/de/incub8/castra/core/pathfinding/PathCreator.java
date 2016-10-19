package de.incub8.castra.core.pathfinding;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.PathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.utils.Array;
import de.incub8.castra.core.Castra;
import de.incub8.castra.core.model.Army;
import de.incub8.castra.core.model.Paths;
import de.incub8.castra.core.model.Settlement;

public class PathCreator
{
    private final Coordinates coordinates;
    private final BlacklistAwareCoordinateGraph blacklistAwareCoordinateGraph;
    private final PathFinder<GridPoint2> pathFinder;
    private final Heuristic<GridPoint2> heuristic;

    public PathCreator()
    {
        coordinates = new Coordinates(Castra.VIEWPORT_WIDTH, Castra.VIEWPORT_HEIGHT);
        blacklistAwareCoordinateGraph = new BlacklistAwareCoordinateGraph(coordinates);
        pathFinder = new IndexedAStarPathFinder<>(blacklistAwareCoordinateGraph);
        heuristic = new StraightLineHeuristic();
    }

    public Paths create(Array<Settlement> settlements)
    {
        Paths result = new Paths();

        for (int i = 0; i < settlements.size - 1; i++)
        {
            Settlement origin = settlements.get(i);
            for (int j = i + 1; j < settlements.size; j++)
            {
                Settlement destination = settlements.get(j);

                applyBlacklist(settlements, origin, destination);

                GraphPath<GridPoint2> graphPath = calculateGraphPathBetween(origin, destination);

                if (graphPath != null)
                {
                    Array<GridPoint2> path = toArray(graphPath);
                    result.put(origin, destination, path);

                    Array<GridPoint2> reversedPath = reverse(path);
                    result.put(destination, origin, reversedPath);
                }
                else
                {
                    throw new PathFindingException(
                        "Unable to find path between " +
                            origin +
                            " and " +
                            destination);
                }
            }
        }
        return result;
    }

    private void applyBlacklist(Array<Settlement> settlements, Settlement origin, Settlement destination)
    {
        Array<Shape2D> blacklist = new Array<>();
        for (Settlement settlement : settlements)
        {
            if (!settlement.equals(origin) && !settlement.equals(destination))
            {
                GridPoint2 settlementPosition = settlement.getPosition();
                Ellipse settlementHitbox = settlement.getHitbox();
                Shape2D blacklistEntry = new Ellipse(
                    settlementPosition.x,
                    settlementPosition.y,
                    settlementHitbox.width + Army.WIDTH,
                    settlementHitbox.height + Army.HEIGHT);
                blacklist.add(blacklistEntry);
            }
        }
        blacklistAwareCoordinateGraph.setBlacklist(blacklist);
    }

    private GraphPath<GridPoint2> calculateGraphPathBetween(Settlement origin, Settlement destination)
    {
        GraphPath<GridPoint2> result = new DefaultGraphPath<>();
        boolean pathFound = pathFinder.searchNodePath(
            coordinates.attach(
                origin.getPosition()), coordinates.attach(destination.getPosition()), heuristic, result);
        if (!pathFound)
        {
            result = null;
        }
        return result;
    }

    private <T> Array<T> toArray(GraphPath<T> graphPath)
    {
        Array<T> result = new Array<>();
        for (T step : graphPath)
        {
            result.add(step);
        }
        return result;
    }

    private <T> Array<T> reverse(Array<T> array)
    {
        Array<T> result = new Array<>(array);
        result.reverse();
        return result;
    }
}