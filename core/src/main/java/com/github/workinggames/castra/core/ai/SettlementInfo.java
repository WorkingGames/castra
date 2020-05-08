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
            cost = settlement.getSize().getSpawnIntervalInSeconds() * settlement.getSoldiers();
            soldiersPresent = settlement.getSoldiers();
        }
        else
        {
            cost = settlement.getSize().getSpawnIntervalInSeconds() * initialArmySize;
            soldiersPresent = initialArmySize;
        }
    }

    @Override
    public int compareTo(SettlementInfo o)
    {
        // reversed as lowest cost should be first after sorting
        return Float.compare(o.getCost(), this.getCost());
    }

    public void updateCosts(Player aiPlayer, boolean settlementDetailsVisible)
    {
        if (settlement.getOwner().equals(aiPlayer) || settlementDetailsVisible)
        {
            setCost(settlement.getSize().getSpawnIntervalInSeconds() * settlement.getSoldiers());
        }
        else
        {
            setCost(getCost() + getSettlement().getSize().getSpawnIntervalInSeconds());
        }
    }
}