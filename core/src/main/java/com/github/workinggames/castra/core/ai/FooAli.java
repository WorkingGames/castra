package com.github.workinggames.castra.core.ai;

import lombok.extern.slf4j.Slf4j;

import com.badlogic.gdx.utils.Array;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.stage.World;

@Slf4j
public class FooAli implements Ali
{
    private final World world;
    private final AiUtils aiUtils;
    private float lastTickTime;
    private final Player aiPlayer;
    private final Array<SettlementBar> destinations;

    public FooAli(World world, Player aiPlayer)
    {
        this.world = world;
        this.aiPlayer = aiPlayer;
        aiUtils = new AiUtils(world);
        lastTickTime = 0;
        destinations = new Array<>();
        initializeDestinations();
    }

    private void initializeDestinations()
    {
        for (Settlement neutral : aiUtils.getNeutralSettlements())
        {
            float cost = neutral.getSize().getSpawnIntervalInSeconds() * neutral.getSoldiers();
            destinations.add(new SettlementBar(neutral, cost));
        }
        for (Settlement opponent : aiUtils.getOpponentSettlements(aiPlayer))
        {
            // TODO get game configuration for initial army size, as soon as it is configurable
            destinations.add(new SettlementBar(opponent, opponent.getSize().getSpawnIntervalInSeconds() * 100));
        }
    }

    public void update()
    {
        float time = world.getTimepiece().getTime();
        log.info("time: " + time);
        // only do something if a complete 1 second tick has passed
        if (time - lastTickTime >= 1)
        {
            log.info("Tick");
            // update opponent ticks and estimations based on armies deployed

            // sort destinations by cost
            destinations.sort();

            // sort origins by something

            // decide if it's time to attack or defend
            boolean attack = false;
            boolean defend = false;
            if (attack)
            {
                attack();
            }
            else if (defend)
            {
                defend();
            }
        }
    }

    public void attack()
    {
        //        Settlement destination = selectDestination();
        //        Settlement origin = selectOrigin(destination);
        //        if (origin != null && destination != null)
        //        {
        //            aiPlayer.setSendTroopPercentage(MathUtils.random(MINIMUM_TROOP_PERCENTAGE, MAXIMUM_TROOP_PERCENTAGE));
        //            world.createArmy(origin, destination);
        //        }
    }

    public void defend()
    {
    }

    private Settlement selectDestination()
    {
        return null;
    }

    private Settlement selectOrigin(Settlement destination)
    {
        return null;
    }
}