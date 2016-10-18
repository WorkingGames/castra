package de.incub8.castra.core.task;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import de.incub8.castra.core.model.Settlement;
import de.incub8.castra.core.model.SettlementSize;

@RequiredArgsConstructor
public class SoldierSpawner
{
    private final Array<Settlement> settlements;

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

        Timer.schedule(new SoldierSpawnTask(small), 0, SettlementSize.SMALL.getSpawnIntervalInSeconds());
        Timer.schedule(new SoldierSpawnTask(medium), 0, SettlementSize.MEDIUM.getSpawnIntervalInSeconds());
        Timer.schedule(new SoldierSpawnTask(large), 0, SettlementSize.LARGE.getSpawnIntervalInSeconds());
    }
}