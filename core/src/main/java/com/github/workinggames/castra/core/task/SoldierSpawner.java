package com.github.workinggames.castra.core.task;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.model.SettlementSize;

@RequiredArgsConstructor
public class SoldierSpawner implements Disposable
{
    private final Array<Settlement> settlements;
    private Timer.Task smallTask;
    private Timer.Task mediumTask;
    private Timer.Task largeTask;

    public void startSpawn()
    {
        Array<Settlement> small = new Array<>();
        Array<Settlement> medium = new Array<>();
        Array<Settlement> large = new Array<>();

        for (Settlement settlement : settlements)
        {
            if (settlement.getSize().equals(SettlementSize.SMALL))
            {
                small.add(settlement);
            }
            else if (settlement.getSize().equals(SettlementSize.MEDIUM))
            {
                medium.add(settlement);
            }
            else
            {
                large.add(settlement);
            }
        }

        smallTask = Timer.schedule(new SoldierSpawnTask(small), 0, SettlementSize.SMALL.getSpawnIntervalInSeconds());
        mediumTask = Timer.schedule(new SoldierSpawnTask(medium), 0, SettlementSize.MEDIUM.getSpawnIntervalInSeconds());
        largeTask = Timer.schedule(new SoldierSpawnTask(large), 0, SettlementSize.LARGE.getSpawnIntervalInSeconds());
    }

    @Override
    public void dispose()
    {
        smallTask.cancel();
        mediumTask.cancel();
        largeTask.cancel();
    }
}