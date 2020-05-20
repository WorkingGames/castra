package com.github.workinggames.castra.core.task;

import java.util.Iterator;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.github.workinggames.castra.core.actor.Army;
import com.github.workinggames.castra.core.actor.Battle;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.ai.voons.MessageType;
import com.github.workinggames.castra.core.statistics.StatisticsEventCreator;

@RequiredArgsConstructor
public class BattleProcessTask extends Timer.Task
{
    private final Array<Battle> battles;
    private final UUID gameId;
    private final StatisticsEventCreator statisticsEventCreator;

    private boolean captured = false;

    @Override
    public void run()
    {
        MessageDispatcher messageManager = MessageManager.getInstance();

        Iterator<Battle> battleIterator = battles.iterator();
        while (battleIterator.hasNext())
        {
            Battle battle = battleIterator.next();

            Army army = battle.getArmy();
            Settlement settlement = army.getTarget();
            if (army.getOwner().equals(settlement.getOwner()))
            {
                settlement.addSoldier();
            }
            else
            {
                if (!settlement.isEmpty())
                {
                    settlement.removeSoldier();
                }
                else
                {
                    settlement.changeOwner(army.getOwner());
                    settlement.addSoldier();
                    captured = true;
                }
            }

            army.removeSoldier();
            if (army.isEmpty())
            {
                battle.remove();
                battleIterator.remove();

                messageManager.dispatchMessage(0, null, null, MessageType.BATTLE_ENDED, battle);
                statisticsEventCreator.battleEnded(gameId, army, captured);
            }
        }
    }
}