package de.incub8.castra.core.spawning;

import com.badlogic.gdx.utils.Array;
import de.incub8.castra.core.model.Settlement;

public class ArmySpawner
{
    private static final float TICK_TIME = 4;

    private float lastSpawnTime = 0;

    public void spawnArmies(float currentTime, Array<Settlement> settlements)
    {
        if (currentTime - lastSpawnTime > TICK_TIME)
        {
            for (Settlement settlement : settlements)
            {
                settlement.setSoldiers(settlement.getSoldiers() + settlement.getSize().getSpawnAmount());
            }
            lastSpawnTime = currentTime;
        }
    }
}