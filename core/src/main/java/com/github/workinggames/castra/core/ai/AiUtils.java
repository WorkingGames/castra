package com.github.workinggames.castra.core.ai;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.utils.Array;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.stage.World;

@RequiredArgsConstructor
public class AiUtils
{
    private final World world;

    public Array<Settlement> getOwnedSettlements(Player player)
    {
        Array<Settlement> owned = new Array<>();
        for (Settlement settlement : world.getSettlements())
        {
            if (settlement.getOwner().equals(player))
            {
                owned.add(settlement);
            }
        }
        return owned;
    }

    public Array<Settlement> getNeutralSettlements()
    {
        Array<Settlement> neutral = new Array<>();
        for (Settlement settlement : world.getSettlements())
        {
            if (settlement.getOwner().isNeutral())
            {
                neutral.add(settlement);
            }
        }
        return neutral;
    }

    public Array<Settlement> getOpponentSettlements(Player player)
    {
        Array<Settlement> opponent = new Array<>();
        for (Settlement settlement : world.getSettlements())
        {
            Player owner = settlement.getOwner();
            if (!owner.isNeutral() && !owner.equals(player))
            {
                opponent.add(settlement);
            }
        }
        return opponent;
    }
}