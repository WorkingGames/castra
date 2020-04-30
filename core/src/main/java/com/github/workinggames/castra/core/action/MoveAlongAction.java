package com.github.workinggames.castra.core.action;

import lombok.Setter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.github.workinggames.castra.core.pathfinding.LinePath;

public class MoveAlongAction extends TemporalAction
{
    private static final int PIXEL_PER_SECOND = 300;

    /**
     * Moves the actor along the absolute Points from the path
     */
    public static MoveAlongAction obtain(LinePath path)
    {
        Pool<MoveAlongAction> pool = Pools.get(MoveAlongAction.class);
        MoveAlongAction action = pool.obtain();
        action.setPool(pool);
        action.setPath(path);

        action.setDuration(path.getDistance() / PIXEL_PER_SECOND);

        return action;
    }

    @Setter
    private LinePath path;

    @Override
    protected void update(float percent)
    {
        Vector2 pathPosition = path.valueAt(percent);
        actor.setPosition(pathPosition.x, pathPosition.y);
    }

    @Override
    public void reset()
    {
        super.reset();
        path = null;
    }
}