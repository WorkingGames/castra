package com.github.workinggames.castra.core.screen;

import lombok.RequiredArgsConstructor;

import com.github.workinggames.castra.core.actor.Army;
import com.github.workinggames.castra.core.actor.Battle;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.stage.World;

@RequiredArgsConstructor
public class VictoryCondition
{
    private final World world;

    public boolean playerLost()
    {
        boolean lost = true;
        for (Settlement settlement : world.getSettlements())
        {
            if (settlement.getOwner().isHuman())
            {
                lost = false;
                break;
            }
        }
        if (lost)
        {
            for (Army army : world.getArmies())
            {
                if (army.getOwner().isHuman())
                {
                    lost = false;
                    break;
                }
            }
        }
        if (lost)
        {
            for (Battle battle : world.getBattles())
            {
                if (battle.getArmy().getOwner().isHuman())
                {
                    lost = false;
                    break;
                }
            }
        }
        return lost;
    }

    public boolean playerWon()
    {
        boolean won = true;
        for (Settlement settlement : world.getSettlements())
        {
            if (settlement.getOwner().isAi())
            {
                won = false;
                break;
            }
        }
        if (won)
        {
            for (Army army : world.getArmies())
            {
                if (army.getOwner().isAi())
                {
                    won = false;
                    break;
                }
            }
        }
        if (won)
        {
            for (Battle battle : world.getBattles())
            {
                if (battle.getArmy().getOwner().isAi())
                {
                    won = false;
                    break;
                }
            }
        }
        return won;
    }
}