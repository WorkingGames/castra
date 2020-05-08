package com.github.workinggames.castra.core.ai;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.Array;
import com.github.workinggames.castra.core.actor.Army;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.model.ArmySize;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.stage.World;

@Slf4j
public class FooAi implements Ai
{
    private final World world;
    private int lastTickTime;
    private final Player aiPlayer;
    private final Array<SettlementInfo> settlementInfos;
    private final Map<Settlement, SettlementInfo> settlementInfosMap;
    private final RandomXS128 random = new RandomXS128();

    private int lastProcessedArmyId;

    public FooAi(World world, Player aiPlayer)
    {
        this.world = world;
        this.aiPlayer = aiPlayer;
        lastTickTime = 0;
        settlementInfos = new Array<>();
        settlementInfosMap = new HashMap<>();
        initializeSettlementInfos();
    }

    private void initializeSettlementInfos()
    {
        // TODO get game configuration for initial army size, as soon as it is configurable
        int initialArmySize = 100;

        for (Settlement settlement : world.getSettlements())
        {
            SettlementInfo settlementInfo = new SettlementInfo(settlement, initialArmySize);
            settlementInfos.add(settlementInfo);
            settlementInfosMap.put(settlement, settlementInfo);
        }
    }

    public void update()
    {
        float time = world.getTimepiece().getTime();
        // only do something if a complete 1 second tick has passed
        if (time - lastTickTime >= 1)
        {
            lastTickTime = (int) time;
            // update opponent ticks and estimations based on armies deployed
            addSpawnedSoldierToSettlementInfo();

            // first version will not consider effects of multiple armies with the same target created in the same tick
            updateArmies();

            // battles here

            // sort destinations by cost
            settlementInfos.sort();

            // decide if it's time to attack or defend based on costs
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

    public void addSpawnedSoldierToSettlementInfo()
    {
        for (SettlementInfo settlement : settlementInfos)
        {
            if (!settlement.getSettlement().getOwner().isNeutral())
            {
                settlement.updateCosts(aiPlayer, world.getGameConfiguration().isOpponentSettlementDetailsVisible());
            }
        }
    }

    private void updateArmies()
    {
        Array<Army> armies = world.getArmies();
        for (Army army : armies)
        {
            if (!army.getOwner().equals(aiPlayer) && lastProcessedArmyId < army.getArmyId())
            {
                SettlementInfo sourceSettlementInfo = settlementInfosMap.get(army.getSource());
                SettlementInfo targetSettlementInfo = settlementInfosMap.get(army.getTarget());

                int soldiers;
                log.info("New opponent Army discovered");
                if (world.getGameConfiguration().isOpponentArmyDetailsVisible())
                {
                    soldiers = army.getSoldiers();
                }
                else
                {
                    soldiers = randomSoldiersBasedOnArmySizeAndTargetSoldiers(army.getArmySize(),
                        sourceSettlementInfo,
                        targetSettlementInfo);
                }
                // reduce soldiers stationed in source settlement by soldier count/estimate
                sourceSettlementInfo.setSoldiersPresent(sourceSettlementInfo.getSoldiersPresent() - soldiers);

                targetSettlementInfo.setOpponentSoldiersInbound(targetSettlementInfo.getOpponentSoldiersInbound() +
                    soldiers);
                lastProcessedArmyId = army.getArmyId();
            }
        }
    }

    private int randomSoldiersBasedOnArmySizeAndTargetSoldiers(
        ArmySize armySize, SettlementInfo source, SettlementInfo target)
    {
        int min = armySize.getMinimumSoldiers();
        int max = source.getSoldiersPresent();
        switch (armySize)
        {
            case SMALL:
                int maxSoldiersBeforeMediumArmySize = ArmySize.MEDIUM.getMinimumSoldiers() - 1;
                if (max > maxSoldiersBeforeMediumArmySize)
                {
                    max = maxSoldiersBeforeMediumArmySize;
                }
                min = estimateMinSize(min, max, target);
            case MEDIUM:
                int maxSoldiersBeforeLargeArmySize = ArmySize.LARGE.getMinimumSoldiers() - 1;
                if (max > maxSoldiersBeforeLargeArmySize)
                {
                    max = maxSoldiersBeforeLargeArmySize;
                }
                min = estimateMinSize(min, max, target);

            case LARGE:
                min = estimateMinSize(min, max, target);
        }
        return getRandomValueInclusive(min, max);
    }

    private int estimateMinSize(int min, int max, SettlementInfo target)
    {
        // let's assume the opponent wants to take over the settlement and not just weaken it
        if (target.getSoldiersPresent() < max && target.getPlayerSoldiersInbound() == 0)
        {
            return target.getSoldiersPresent();
        }
        return min;
    }

    private int getRandomValueInclusive(int minimum, int maximum)
    {
        return minimum + random.nextInt(maximum - minimum + 1);
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