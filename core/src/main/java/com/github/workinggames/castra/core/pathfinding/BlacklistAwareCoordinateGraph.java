package com.github.workinggames.castra.core.pathfinding;

import lombok.Getter;
import lombok.Setter;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

class BlacklistAwareCoordinateGraph implements IndexedGraph<Vector2>
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
    public Array<Connection<Vector2>> getConnections(Vector2 fromNode)
    {
        Array<Vector2> surroundingCoordinates = getSurroundingCoordinates(fromNode);
        return createConnections(fromNode, surroundingCoordinates);
    }

    private Array<Vector2> getSurroundingCoordinates(Vector2 coordinate)
    {
        float x = coordinate.x;
        float y = coordinate.y;

        Array<Vector2> result = new Array<>();
        addCoordinateIfValid(coordinates.get(x - 1, y - 1), result);
        addCoordinateIfValid(coordinates.get(x, y - 1), result);
        addCoordinateIfValid(coordinates.get(x + 1, y - 1), result);
        addCoordinateIfValid(coordinates.get(x - 1, y), result);
        addCoordinateIfValid(coordinates.get(x + 1, y), result);
        addCoordinateIfValid(coordinates.get(x - 1, y + 1), result);
        addCoordinateIfValid(coordinates.get(x, y + 1), result);
        addCoordinateIfValid(coordinates.get(x + 1, y + 1), result);
        return result;
    }

    private void addCoordinateIfValid(Vector2 coordinate, Array<Vector2> result)
    {
        if (isValid(coordinate))
        {
            result.add(coordinate);
        }
    }

    private boolean isValid(Vector2 coordinate)
    {
        return coordinate != null && !isBlacklisted(coordinate);
    }

    private boolean isBlacklisted(Vector2 coordinate)
    {
        boolean result = false;
        if (blacklist != null)
        {
            for (Shape2D shape2D : blacklist)
            {
                result = shape2D.contains(coordinate.x, coordinate.y);
                if (result)
                {
                    break;
                }
            }
        }
        return result;
    }

    private Array<Connection<Vector2>> createConnections(
        Vector2 fromNode, Array<Vector2> surroundingCoordinates)
    {
        Array<Connection<Vector2>> result = new Array<>();
        for (Vector2 toNode : surroundingCoordinates)
        {
            result.add(new PathConnection(fromNode, toNode));
        }
        return result;
    }

    @Override
    public int getIndex(Vector2 node)
    {
        return (int) ((node.x - 1) * coordinates.getHeight() + node.y - 1);
    }
}