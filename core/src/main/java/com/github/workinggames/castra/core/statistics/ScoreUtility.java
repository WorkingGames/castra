package com.github.workinggames.castra.core.statistics;

import lombok.experimental.UtilityClass;

import com.badlogic.gdx.math.MathUtils;
import com.github.workinggames.castra.core.actor.Army;
import com.github.workinggames.castra.core.actor.Battle;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.model.SettlementSize;
import com.github.workinggames.castra.core.stage.World;

@UtilityClass
public class ScoreUtility
{
    private final int MAX_SECONDS_GETTING_POINTS = 300;
    private final int PLAYTIME_SCORE_FACTOR = 30;
    private final int SOLDIERS_SCORE_FACTOR = 25;

    public int getGameScore(World world, Player winner, Float playTime)
    {
        int soldierCount = ScoreUtility.getSoldierCount(world, winner);
        float soldierSpawnPerSecond = ScoreUtility.getMapSoldierSpawnPerSecond(world);
        int timeScore = Math.max(MathUtils.floor(MAX_SECONDS_GETTING_POINTS - playTime) * PLAYTIME_SCORE_FACTOR, 0);
        int soldierScore = MathUtils.floor(soldierCount / soldierSpawnPerSecond) * SOLDIERS_SCORE_FACTOR;
        return timeScore + soldierScore;
    }

    private int getSoldierCount(World world, Player player)
    {
        int soldiers = 0;
        for (Settlement settlement : world.getSettlements())
        {
            if (settlement.getOwner().equals(player))
            {
                soldiers = soldiers + settlement.getSoldiers();
            }
        }

        for (Army army : world.getArmies())
        {
            if (army.getOwner().equals(player))
            {
                soldiers = soldiers + army.getSoldiers();
            }
        }

        for (Battle battle : world.getBattles())
        {
            if (battle.getArmy().getOwner().equals(player))
            {
                soldiers = soldiers + battle.getArmy().getSoldiers();
            }
        }
        return soldiers;
    }

    private float getMapSoldierSpawnPerSecond(World world)
    {
        float soldiersPerSecond = 0;
        for (Settlement settlement : world.getSettlements())
        {
            switch (settlement.getSize())
            {
                case SMALL:
                    soldiersPerSecond = soldiersPerSecond + 1 / SettlementSize.SMALL.getSpawnIntervalInSeconds();
                    break;
                case MEDIUM:
                    soldiersPerSecond = soldiersPerSecond + 1 / SettlementSize.MEDIUM.getSpawnIntervalInSeconds();
                    break;
                case LARGE:
                    soldiersPerSecond = soldiersPerSecond + 1 / SettlementSize.LARGE.getSpawnIntervalInSeconds();
                    break;
            }
        }
        return soldiersPerSecond;
    }
}