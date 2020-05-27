package com.github.workinggames.castra.core.task;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.github.workinggames.castra.core.actor.Battle;
import com.github.workinggames.castra.core.statistics.StatisticsEventCreator;

@RequiredArgsConstructor
public class BattleProcessor implements Disposable
{
    private final Array<Battle> battles;
    private final UUID gameId;
    private final StatisticsEventCreator statisticsEventCreator;

    private Timer.Task battleTask;

    public void startBattles(float battleProcessingInterval)
    {
        battleTask = Timer.schedule(new BattleProcessTask(battles, gameId, statisticsEventCreator),
            0,
            battleProcessingInterval);
    }

    @Override
    public void dispose()
    {
        battleTask.cancel();
    }
}