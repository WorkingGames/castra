package com.github.workinggames.castra.core.ai;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ArrayMap;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.model.SettlementSize;
import com.github.workinggames.castra.core.task.BattleProcessor;

@Getter
@RequiredArgsConstructor
public class SettlementFoo
{
    private final Settlement settlement;
    private final Player aiPlayer;
    private final Map<Integer, Float> settlementDistancesInTicks = new HashMap<>();

    private final ArrayMap<Integer, ArmyFoo> inboundArmies = new ArrayMap<>();
    private final ArrayMap<Integer, BattleFoo> battles = new ArrayMap<>();

    @Setter
    private int defenders;

    float getBreakEvenInSeconds(int requiredSoldiers)
    {
        float secondsUntilReimbursed = requiredSoldiers * settlement.getSize().getSpawnIntervalInSeconds();
        float ownerScore;
        if (settlement.getOwner().isNeutral())
        {
            ownerScore = 1;
        }
        else
        {
            // we kill the opponent soldiers, so the break even is cheaper
            ownerScore = 0.5f;
        }
        return ownerScore * secondsUntilReimbursed;
    }

    int getRequiredSoldiersToTakeOver(float targetDistanceInTicks)
    {
        float requiredSoldiers = defenders + 1;
        if (!settlement.getOwner().isNeutral())
        {
            requiredSoldiers = requiredSoldiers +
                getSoldierSpawnUntilReached(targetDistanceInTicks) +
                getBattleSoldierSpawn(requiredSoldiers, settlement.getSize());
        }
        for (ArmyFoo armyFoo : inboundArmies.values())
        {
            if (armyFoo.getOwner().equals(aiPlayer))
            {
                requiredSoldiers = requiredSoldiers - armyFoo.getSoldiers();
            }
            else
            {
                requiredSoldiers = requiredSoldiers + armyFoo.getSoldiers();
            }
        }
        // let's round up
        return MathUtils.ceil(requiredSoldiers);
    }

    int getRequiredSoldiersToDefend()
    {
        // laters
        return MathUtils.ceil(settlement.getSoldiers());
    }

    float getSoldierSpawnUntilReached(float targetDistanceInTicks)
    {
        return targetDistanceInTicks / settlement.getSize().getSpawnIntervalInSeconds();
    }

    float getBattleSoldierSpawn(float soldiers, SettlementSize settlementSize)
    {
        float minBattleTime = soldiers * BattleProcessor.BATTLE_PROCESSING_INTERVAL;

        return minBattleTime / settlementSize.getSpawnIntervalInSeconds();
    }

    public boolean isOwnedByPlayer()
    {
        return settlement.getOwner().equals(aiPlayer);
    }
}