package com.github.workinggames.castra.core.ai.voons;

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

@Getter
@RequiredArgsConstructor
public class SettlementInfo
{
    private final Settlement settlement;
    private final Player aiPlayer;
    private final Map<Integer, Float> settlementDistancesInTicks = new HashMap<>();

    private final ArrayMap<Integer, ArmyInfo> inboundArmies = new ArrayMap<>();
    private final ArrayMap<Integer, BattleInfo> battles = new ArrayMap<>();

    @Setter
    private int defenders;

    float getBreakEvenInSeconds(float requiredSoldiers)
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

    int getRequiredSoldiersToTakeOver(float targetDistanceInTicks, float battleProcessingInterval)
    {
        float requiredSoldiers = defenders + 1;
        if (!settlement.getOwner().isNeutral())
        {
            requiredSoldiers = requiredSoldiers +
                getSoldierSpawnUntilReached(targetDistanceInTicks) +
                getBattleSoldierSpawn(requiredSoldiers, settlement.getSize(), battleProcessingInterval);
        }
        for (ArmyInfo armyInfo : inboundArmies.values())
        {
            if (armyInfo.getOwner().equals(aiPlayer))
            {
                requiredSoldiers = requiredSoldiers - armyInfo.getSoldiers();
            }
            else
            {
                requiredSoldiers = requiredSoldiers + armyInfo.getSoldiers();
            }
        }
        // let's round up
        return MathUtils.ceil(requiredSoldiers);
    }

    int getAvailableSoldiers()
    {
        int available = settlement.getSoldiers();
        for (ArmyInfo armyInfo : inboundArmies.values())
        {
            if (armyInfo.getOwner().equals(aiPlayer))
            {
                available = available + armyInfo.getSoldiers();
            }
            else
            {
                available = available - armyInfo.getSoldiers();
            }
        }
        return MathUtils.ceil(settlement.getSoldiers());
    }

    float getSoldierSpawnUntilReached(float targetDistanceInTicks)
    {
        return targetDistanceInTicks / settlement.getSize().getSpawnIntervalInSeconds();
    }

    float getBattleSoldierSpawn(float soldiers, SettlementSize settlementSize, float battleProcessingInterval)
    {
        float minBattleTime = soldiers * battleProcessingInterval;

        return minBattleTime / settlementSize.getSpawnIntervalInSeconds();
    }

    public boolean isOwnedByPlayer()
    {
        return settlement.getOwner().equals(aiPlayer);
    }
}