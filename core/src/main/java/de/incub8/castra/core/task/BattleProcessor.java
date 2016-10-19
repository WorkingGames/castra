package de.incub8.castra.core.task;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import de.incub8.castra.core.model.Battle;

@RequiredArgsConstructor
public class BattleProcessor
{
    private static final float BATTLE_PROCESSING_INTERVAL = 0.1f;

    private final Array<Battle> battles;

    public void startBattles()
    {
        Timer.schedule(new BattleProcessTask(battles), 0, BATTLE_PROCESSING_INTERVAL);
    }
}