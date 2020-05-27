package com.github.workinggames.castra.core.pathfinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.PathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.model.Paths;
import com.github.workinggames.castra.core.screen.LoadingState;
import com.github.workinggames.castra.core.stage.World;
import com.github.workinggames.castra.core.task.AsyncInitializer;

public class PathInitializer implements AsyncInitializer
{
    private final Coordinates coordinates;
    private final BlacklistAwareCoordinateGraph blacklistAwareCoordinateGraph;
    private final PathFinder<Vector2> pathFinder;
    private final Heuristic<Vector2> heuristic;
    private final PathUtils pathUtils;
    private final World world;

    public PathInitializer(Viewport viewport, TextureAtlas textureAtlas, World world)
    {
        coordinates = new Coordinates(viewport);
        blacklistAwareCoordinateGraph = new BlacklistAwareCoordinateGraph(coordinates);
        pathFinder = new IndexedAStarPathFinder<>(blacklistAwareCoordinateGraph);
        heuristic = new StraightLineHeuristic();
        pathUtils = new PathUtils(textureAtlas);
        this.world = world;
    }

    public void initialize()
    {
        Array<Settlement> settlements = world.getSettlements();
        PathSmoother pathSmoother = new PathSmoother(settlements, pathUtils);

        Paths paths = world.getPaths();

        for (int i = 0; i < settlements.size - 1; i++)
        {
            Settlement origin = settlements.get(i);
            for (int j = i + 1; j < settlements.size; j++)
            {
                Settlement destination = settlements.get(j);

                applyBlacklist(settlements, origin, destination);

                GraphPath<Vector2> graphPath = calculateGraphPathBetween(origin, destination);

                if (graphPath != null)
                {
                    Array<Line> linePath = pathSmoother.smoothPath(origin, destination, graphPath);
                    paths.put(origin, destination, new LinePath(linePath));

                    Array<Line> reversedLinePath = pathUtils.reverse(linePath);
                    paths.put(destination, origin, new LinePath(reversedLinePath));
                }
                else
                {
                    Gdx.app.error("PathInitializer",
                        "Can't find path for seed " +
                            world.getGameConfiguration().getSeed() +
                            " from settlement " +
                            origin.getId() +
                            " to settlement " +
                            destination.getId());
                    throw new PathFindingException("Unable to find path between two settlements");
                }
            }
        }
        LoadingState.getInstance().setPathInitialized(true);
    }

    private void applyBlacklist(Array<Settlement> settlements, Settlement origin, Settlement destination)
    {
        Array<Shape2D> blacklist = new Array<>();
        for (Settlement settlement : settlements)
        {
            if (!settlement.equals(origin) && !settlement.equals(destination))
            {
                Ellipse blacklistEntry = pathUtils.getHitboxWithArmySpacing(settlement);
                blacklist.add(blacklistEntry);
            }
        }
        blacklistAwareCoordinateGraph.setBlacklist(blacklist);
    }

    private GraphPath<Vector2> calculateGraphPathBetween(Settlement origin, Settlement destination)
    {
        GraphPath<Vector2> result = new DefaultGraphPath<>();
        boolean pathFound = pathFinder.searchNodePath(coordinates.get(origin.getCenterX(), origin.getCenterY()),
            coordinates.get(destination.getCenterX(), destination.getCenterY()),
            heuristic,
            result);
        if (!pathFound)
        {
            result = null;
        }
        return result;
    }
}