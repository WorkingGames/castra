package com.github.workinggames.castra.core.task;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.github.workinggames.castra.core.actor.Army;
import com.github.workinggames.castra.core.actor.Battle;
import com.github.workinggames.castra.core.actor.Settlement;

@RequiredArgsConstructor
public class BattleProcessTask extends Timer.Task
{
    private final Array<Battle> battles;

    @Override
    public void run()
    {
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
                }
            }

            army.removeSoldier();
            if (army.isEmpty())
            {
                battle.remove();
                battleIterator.remove();
            }
        }
    }
}