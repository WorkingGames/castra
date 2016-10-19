package de.incub8.castra.core.pathfinding;

import lombok.Data;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.math.GridPoint2;

@Data
class GridPoint2Connection implements Connection<GridPoint2>
{
    private final GridPoint2 fromNode;
    private final GridPoint2 toNode;
    private final float cost;

    public GridPoint2Connection(GridPoint2 fromNode, GridPoint2 toNode)
    {
        this.fromNode = fromNode;
        this.toNode = toNode;
        cost = fromNode.dst2(toNode);
    }
}