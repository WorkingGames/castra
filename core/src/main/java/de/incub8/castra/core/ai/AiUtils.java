package de.incub8.castra.core.ai;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.utils.Array;
import de.incub8.castra.core.actor.Settlement;
import de.incub8.castra.core.stage.World;

@RequiredArgsConstructor
class AiUtils
{
    private final World world;

    public Array<Settlement> getOwnedSettlements()
    {
        Array<Settlement> owned = new Array<>();
        for (Settlement settlement : world.getSettlements())
        {
            if (settlement.getOwner().isAi())
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

    public Array<Settlement> getPlayerSettlements()
    {
        Array<Settlement> player = new Array<>();
        for (Settlement settlement : world.getSettlements())
        {
            if (settlement.getOwner().isHuman())
            {
                player.add(settlement);
            }
        }
        return player;
    }
}