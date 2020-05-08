package com.github.workinggames.castra.core.ai;

import lombok.Data;

import com.badlogic.gdx.utils.Array;
import com.github.workinggames.castra.core.actor.Battle;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.model.Player;

@Data
public class SettlementInfo implements Comparable<SettlementInfo>
{
    private Settlement settlement;
    private float cost;
    private int opponentSoldiersInbound = 0;
    private int playerSoldiersInbound = 0;
    private int soldiersPresent;
    private Array<Battle> battles;

    public SettlementInfo(Settlement settlement, int initialArmySize)
    {
        this.settlement = settlement;

        if (settlement.getOwner().isNeutral())
        {
            soldiersPresent = settlement.getSoldiers();
        }
        else
        {
            soldiersPresent = initialArmySize;
        }
        cost = settlement.getSize().getSpawnIntervalInSeconds() * soldiersPresent;
    }

    @Override
    public int compareTo(SettlementInfo o)
    {
        // lowest cost should be on top
        return Float.compare(this.getCost(), o.getCost());
    }

    public void update(int lastTickTime, Player aiPlayer, boolean settlementDetailsVisible)
    {
        float spawnIntervalInSeconds = settlement.getSize().getSpawnIntervalInSeconds();
        if (settlement.getOwner().equals(aiPlayer) || settlementDetailsVisible)
        {
            setCost(settlement.getSoldiers() + getOpponentSoldiersInbound() -
                getPlayerSoldiersInbound() * spawnIntervalInSeconds);
        }
        else
        {
            if (lastTickTime % (int) spawnIntervalInSeconds == 0)
            {
                soldiersPresent++;
            }
            setCost(soldiersPresent + getOpponentSoldiersInbound() -
                getPlayerSoldiersInbound() * spawnIntervalInSeconds);
        }
    }
}