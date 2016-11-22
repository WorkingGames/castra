package de.incub8.castra.core.pathfinding;

import lombok.Data;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.math.Vector2;

@Data
class PathConnection implements Connection<Vector2>
{
    private final Vector2 fromNode;
    private final Vector2 toNode;
    private final float cost;

    public PathConnection(Vector2 fromNode, Vector2 toNode)
    {
        this.fromNode = fromNode;
        this.toNode = toNode;
        cost = fromNode.dst2(toNode);
    }
}