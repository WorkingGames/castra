package de.incub8.castra.core.task;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import de.incub8.castra.core.actor.Army;
import de.incub8.castra.core.actor.Settlement;
import de.incub8.castra.core.model.Battle;

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
            Settlement settlement = battle.getSettlement();

            if (army.getOwner().equals(settlement.getOwner()))
            {
                settlement.addSoldier();
            }
            else
            {
                settlement.removeSoldier();
                if (settlement.isEmpty())
                {
                    settlement.changeOwner(army.getOwner());
                }
            }

            army.removeSoldier();
            if (army.isEmpty())
            {
                battleIterator.remove();
            }
        }
    }
}