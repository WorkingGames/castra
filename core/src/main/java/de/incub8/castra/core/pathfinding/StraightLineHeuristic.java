package de.incub8.castra.core.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.math.GridPoint2;

class StraightLineHeuristic implements Heuristic<GridPoint2>
{
    @Override
    public float estimate(GridPoint2 node, GridPoint2 endNode)
    {
        return node.dst2(endNode);
    }
}