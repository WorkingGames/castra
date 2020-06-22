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
    private final int WINNING_SCORE_FACTOR = 30;
    private final int LOOSING_SCORE_FACTOR = 10;
    private final int MAXIMUM_SCORE_BY_SOLDIERS = MAX_SECONDS_GETTING_POINTS * WINNING_SCORE_FACTOR / 2;

    public int getWinningGameScore(World world, Player winner, int playTime)
    {
        int soldierCount = ScoreUtility.getSoldierCount(world, winner);
        float soldierSpawnPerSecond = ScoreUtility.getMapSoldierSpawnPerSecond(world);
        int timeScore = getWinningTimeScore(playTime);
        int soldierScore = getSoldierScore(soldierCount, soldierSpawnPerSecond);
        return timeScore + soldierScore;
    }

    public int getSoldierScore(int soldierCount, float soldierSpawnPerSecond)
    {
        return Math.min(MathUtils.floor(soldierCount / soldierSpawnPerSecond) * WINNING_SCORE_FACTOR,
            MAXIMUM_SCORE_BY_SOLDIERS);
    }

    // score by time is 0-9000
    public int getWinningTimeScore(int playTime)
    {
        return Math.max(MathUtils.floor(MAX_SECONDS_GETTING_POINTS - playTime) * WINNING_SCORE_FACTOR, 0);
    }

    // score by time is 0-3000
    public int getLostTimeScore(int playTime)
    {
        return MathUtils.floor(Math.min(playTime, MAX_SECONDS_GETTING_POINTS) * LOOSING_SCORE_FACTOR);
    }

    public int getSoldierCount(World world, Player player)
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

    public float getMapSoldierSpawnPerSecond(World world)
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