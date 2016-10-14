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
import de.incub8.castra.core.model.Army;
import de.incub8.castra.core.model.Coordinates;
import de.incub8.castra.core.model.Paths;
import de.incub8.castra.core.model.Settlement;
import de.incub8.castra.core.model.World;

public class PathEnhancer
{
    private final Coordinates coordinates;
    private final BlacklistAwareCoordinateGraph blacklistAwareCoordinateGraph;
    private final PathFinder<GridPoint2> pathFinder;
    private final Heuristic<GridPoint2> heuristic;

    public PathEnhancer(Coordinates coordinates)
    {
        this.coordinates = coordinates;
        blacklistAwareCoordinateGraph = new BlacklistAwareCoordinateGraph(coordinates);
        pathFinder = new IndexedAStarPathFinder<>(blacklistAwareCoordinateGraph);
        heuristic = new StraightLineHeuristic();
    }

    public void enhance(World world)
    {
        Paths paths = world.getPaths();

        Array<Settlement> settlements = world.getSettlements();
        for (int i = 0; i < settlements.size - 1; i++)
        {
            Settlement settlement1 = settlements.get(i);
            for (int j = i + 1; j < settlements.size; j++)
            {
                Settlement settlement2 = settlements.get(j);

                applyBlacklist(settlements, settlement1, settlement2);

                GraphPath<GridPoint2> graphPath = calculateGraphPathBetween(settlement1, settlement2);

                if (graphPath != null)
                {
                    Array<GridPoint2> path = toArray(graphPath);
                    paths.put(settlement1, settlement2, path);

                    Array<GridPoint2> reversedPath = reverse(path);
                    paths.put(settlement2, settlement1, reversedPath);
                }
                else
                {
                    throw new PathFindingException(
                        "Unable to find path between " +
                            settlement1 +
                            " and " +
                            settlement2);
                }
            }
        }
    }

    private void applyBlacklist(Array<Settlement> settlements, Settlement settlement1, Settlement settlement2)
    {
        Array<Shape2D> blacklist = new Array<>();
        for (Settlement settlement : settlements)
        {
            if (!settlement.equals(settlement1) && !settlement.equals(settlement2))
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

    private GraphPath<GridPoint2> calculateGraphPathBetween(Settlement settlement1, Settlement settlement2)
    {
        GraphPath<GridPoint2> result = new DefaultGraphPath<>();
        boolean pathFound = pathFinder.searchNodePath(
            settlement1.getPosition(), settlement2.getPosition(), heuristic, result);
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