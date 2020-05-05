package com.github.workinggames.castra.core.statistics;

import lombok.extern.slf4j.Slf4j;

import com.github.workinggames.castra.core.actor.Army;

@Slf4j
public class StatisticsEventCreator
{
    public static void sendSoldiers(Army army)
    {
        log.info("Player {} send {} soldiers to {}'s {} settlement with {} soldiers in distance {}}",
            army.getOwner().getName(),
            army.getSoldiers(),
            army.getTarget().getOwner().getName(),
            army.getTarget().getSize().name(),
            army.getTarget().getSoldiers(),
            army.getPath().getDistance());
    }

    public static void battle(Army army)
    {
        log.info(
            "Battle at {} settlement from player {}, defending with {} soldiers against {} attackers from player {}",
            army.getTarget().getSize().name(),
            army.getTarget().getOwner().getName(),
            army.getTarget().getSoldiers(),
            army.getSoldiers(),
            army.getOwner().getName());
    }

    public static void joinedBattle(Army army)
    {
        log.info("Army joined Battle at {} settlement from player {}, adding {} soldiers for player {}",
            army.getTarget().getSize().name(),
            army.getTarget().getOwner().getName(),
            army.getSoldiers(),
            army.getOwner().getName());
    }

    public static void BattleEnded(Army army, boolean defended)
    {
        if (defended)
        {
            log.info("Battle for {} settlement from player {} ended. Army from player {} was defeated",
                army.getTarget().getSize().name(),
                army.getTarget().getOwner().getName(),
                army.getOwner().getName());
        }
        else
        {
            log.info("Battle for {} settlement from player {} ended. Army from player {} was victorious",
                army.getTarget().getSize().name(),
                army.getTarget().getOwner().getName(),
                army.getOwner().getName());
        }
    }
}