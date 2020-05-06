package com.github.workinggames.castra.core.statistics;

import lombok.extern.slf4j.Slf4j;

import com.github.workinggames.castra.core.actor.Army;

@Slf4j
public class StatisticsEventCreator
{
    public static void sendSoldiers(Army army)
    {
        log.info("{} send {} soldiers to {}'s {} settlement with {} soldiers in distance {}}",
            army.getOwner().getName(),
            army.getSoldiers(),
            army.getTarget().getOwner().getName(),
            army.getTarget().getSize().name(),
            army.getTarget().getSoldiers(),
            army.getPath().getDistance());
    }

    public static void battle(Army army)
    {
        log.info("Battle at {} settlement from {}, defending with {} soldiers against {} attackers from {}",
            army.getTarget().getSize().name(),
            army.getTarget().getOwner().getName(),
            army.getTarget().getSoldiers(),
            army.getSoldiers(),
            army.getOwner().getName());
    }

    public static void joinedBattle(Army army)
    {
        log.info("Army joined Battle at {} settlement from {}, adding {} soldiers for {}",
            army.getTarget().getSize().name(),
            army.getTarget().getOwner().getName(),
            army.getSoldiers(),
            army.getOwner().getName());
    }

    public static void BattleEnded(Army army, boolean defended)
    {
        if (defended)
        {
            log.info("Battle for {} settlement from {} ended. Army from {} was defeated",
                army.getTarget().getSize().name(),
                army.getTarget().getOwner().getName(),
                army.getOwner().getName());
        }
        else
        {
            log.info("Battle for {} settlement from {} ended. Army from {} was victorious",
                army.getTarget().getSize().name(),
                army.getTarget().getOwner().getName(),
                army.getOwner().getName());
        }
    }
}