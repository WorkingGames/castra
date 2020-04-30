package com.github.workinggames.castra.core.task;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.github.workinggames.castra.core.actor.Battle;

@RequiredArgsConstructor
public class BattleProcessor implements Disposable
{
    private static final float BATTLE_PROCESSING_INTERVAL = 0.1f;

    private final Array<Battle> battles;
    private Timer.Task battleTask;

    public void startBattles()
    {
        battleTask = Timer.schedule(new BattleProcessTask(battles), 0, BATTLE_PROCESSING_INTERVAL);
    }

    @Override
    public void dispose()
    {
        battleTask.cancel();
    }
}