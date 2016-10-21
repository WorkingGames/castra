package de.incub8.castra.core.pathfinding;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.PathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.incub8.castra.core.actor.Settlement;
import de.incub8.castra.core.model.ArmySize;
import de.incub8.castra.core.model.Paths;
import de.incub8.castra.core.stage.World;

public class PathInitializer
{
    private final Coordinates coordinates;
    private final BlacklistAwareCoordinateGraph blacklistAwareCoordinateGraph;
    private final PathFinder<GridPoint2> pathFinder;
    private final Heuristic<GridPoint2> heuristic;
    private final int armyWidth;
    private final int armyHeight;

    public PathInitializer(Viewport viewport, TextureAtlas textureAtlas)
    {
        coordinates = new Coordinates(viewport);
        blacklistAwareCoordinateGraph = new BlacklistAwareCoordinateGraph(coordinates);
        pathFinder = new IndexedAStarPathFinder<>(blacklistAwareCoordinateGraph);
        heuristic = new StraightLineHeuristic();
        Texture armyTexture = textureAtlas.findRegion(ArmySize.LARGE.getTextureName()).getTexture();
        armyWidth = armyTexture.getWidth();
        armyHeight = armyTexture.getHeight();
    }

    public void initialize(World world)
    {
        Array<Settlement> settlements = world.getSettlements();
        Paths paths = world.getPaths();

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
                    paths.put(origin, destination, path);

                    Array<GridPoint2> reversedPath = reverse(path);
                    paths.put(destination, origin, reversedPath);
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
    }

    private void applyBlacklist(Array<Settlement> settlements, Settlement origin, Settlement destination)
    {
        Array<Shape2D> blacklist = new Array<>();
        for (Settlement settlement : settlements)
        {
            if (!settlement.equals(origin) && !settlement.equals(destination))
            {
                Ellipse blacklistEntry = new Ellipse(settlement.getHitbox());
                blacklistEntry.setSize(blacklistEntry.width + armyWidth, blacklistEntry.height + armyHeight);
                blacklist.add(blacklistEntry);
            }
        }
        blacklistAwareCoordinateGraph.setBlacklist(blacklist);
    }

    private GraphPath<GridPoint2> calculateGraphPathBetween(Settlement origin, Settlement destination)
    {
        GraphPath<GridPoint2> result = new DefaultGraphPath<>();
        boolean pathFound = pathFinder.searchNodePath(
            coordinates.get(origin.getX(), origin.getY()),
            coordinates.get(destination.getX(), destination.getY()),
            heuristic,
            result);
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