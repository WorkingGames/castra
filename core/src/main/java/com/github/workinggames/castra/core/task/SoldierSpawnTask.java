package com.github.workinggames.castra.core.task;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.github.workinggames.castra.core.actor.Settlement;

@RequiredArgsConstructor
public class SoldierSpawnTask extends Timer.Task
{
    private final Array<Settlement> settlements;

    @Override
    public void run()
    {
        for (Settlement settlement : settlements)
        {
            if (!settlement.getOwner().isNeutral())
            {
                settlement.addSoldier();
            }
        }
    }
}