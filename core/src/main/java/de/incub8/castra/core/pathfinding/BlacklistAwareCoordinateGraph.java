package de.incub8.castra.core.pathfinding;

import lombok.Getter;
import lombok.Setter;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.utils.Array;
import de.incub8.castra.core.model.Coordinates;

class BlacklistAwareCoordinateGraph implements IndexedGraph<GridPoint2>
{
    private final Coordinates coordinates;

    @Getter
    private final int nodeCount;

    @Setter
    private Array<Shape2D> blacklist;

    public BlacklistAwareCoordinateGraph(Coordinates coordinates)
    {
        this.coordinates = coordinates;
        nodeCount = coordinates.getCount();
    }

    @Override
    public Array<Connection<GridPoint2>> getConnections(GridPoint2 fromNode)
    {
        Array<GridPoint2> surroundingGridPoints = getSurroundingGridPoints(fromNode);
        Array<Connection<GridPoint2>> result = createConnections(fromNode, surroundingGridPoints);
        return result;
    }

    private Array<GridPoint2> getSurroundingGridPoints(GridPoint2 gridPoint2)
    {
        int x = gridPoint2.x;
        int y = gridPoint2.y;

        Array<GridPoint2> result = new Array<>();
        addGridPointIfValid(coordinates.get(x - 1, y - 1), result);
        addGridPointIfValid(coordinates.get(x, y - 1), result);
        addGridPointIfValid(coordinates.get(x + 1, y - 1), result);
        addGridPointIfValid(coordinates.get(x - 1, y), result);
        addGridPointIfValid(coordinates.get(x + 1, y), result);
        addGridPointIfValid(coordinates.get(x - 1, y + 1), result);
        addGridPointIfValid(coordinates.get(x, y + 1), result);
        addGridPointIfValid(coordinates.get(x + 1, y + 1), result);
        return result;
    }

    private void addGridPointIfValid(GridPoint2 gridPoint2, Array<GridPoint2> result)
    {
        if (isValid(gridPoint2))
        {
            result.add(gridPoint2);
        }
    }

    private boolean isValid(GridPoint2 gridPoint2)
    {
        return gridPoint2 != null && !isBlacklisted(gridPoint2);
    }

    private boolean isBlacklisted(GridPoint2 gridPoint2)
    {
        boolean result = false;
        if (blacklist != null)
        {
            for (Shape2D shape2D : blacklist)
            {
                result = shape2D.contains(gridPoint2.x, gridPoint2.y);
                if (result)
                {
                    break;
                }
            }
        }
        return result;
    }

    private Array<Connection<GridPoint2>> createConnections(
        GridPoint2 fromNode, Array<GridPoint2> surroundingGridPoints)
    {
        Array<Connection<GridPoint2>> result = new Array<>();
        for (GridPoint2 toNode : surroundingGridPoints)
        {
            result.add(new GridPoint2Connection(fromNode, toNode));
        }
        return result;
    }

    @Override
    public int getIndex(GridPoint2 node)
    {
        return (node.x - 1) * coordinates.getHeight() + node.y - 1;
    }
}