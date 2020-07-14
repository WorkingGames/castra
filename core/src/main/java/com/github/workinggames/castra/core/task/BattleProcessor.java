package com.github.workinggames.castra.core.task;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.github.workinggames.castra.core.actor.Battle;
import com.github.workinggames.castra.core.audio.AudioManager;
import com.github.workinggames.castra.core.statistics.StatisticsEventCreator;

@RequiredArgsConstructor
public class BattleProcessor implements Disposable
{
    private final Array<Battle> battles;
    private final String gameId;
    private final StatisticsEventCreator statisticsEventCreator;
    private final AudioManager audioManager;

    private Timer.Task battleTask;

    public void startBattles(float battleProcessingInterval)
    {
        battleTask = Timer.schedule(new BattleProcessTask(battles, gameId, statisticsEventCreator, audioManager),
            0,
            battleProcessingInterval);
    }

    public void stopBattles()
    {
        cancel(battleTask);
    }

    @Override
    public void dispose()
    {
        cancel(battleTask);
    }

    private void cancel(Timer.Task task)
    {
        if (task != null)
        {
            task.cancel();
        }
    }
}