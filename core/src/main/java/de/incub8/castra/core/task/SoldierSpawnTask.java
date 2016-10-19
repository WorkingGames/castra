package de.incub8.castra.core.task;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import de.incub8.castra.core.model.PlayerType;
import de.incub8.castra.core.model.Settlement;

@RequiredArgsConstructor
public class SoldierSpawnTask extends Timer.Task
{
    private final Array<Settlement> settlements;

    @Override
    public void run()
    {
        for (Settlement settlement : settlements)
        {
            if (!settlement.getOwner().getType().equals(PlayerType.NEUTRAL))
            {
                settlement.addSoldier();
            }
        }
    }
}