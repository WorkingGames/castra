package de.incub8.castra.core.ai;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.utils.Array;
import de.incub8.castra.core.model.Player;
import de.incub8.castra.core.model.PlayerType;
import de.incub8.castra.core.model.Settlement;
import de.incub8.castra.core.model.World;

@RequiredArgsConstructor
public class AiUtils
{
    private final World world;

    public Player getAiPlayer()
    {
        Player ai = null;
        for (Player player : world.getPlayers())
        {
            if (belongsTo(player, PlayerType.AI))
            {
                ai = player;
            }
        }
        return ai;
    }

    public Array<Settlement> getOwnedSettlements()
    {
        Array<Settlement> owned = new Array<>();
        for (Settlement settlement : world.getSettlements())
        {
            if (belongsTo(settlement.getOwner(), PlayerType.AI))
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
            if (belongsTo(settlement.getOwner(), PlayerType.NEUTRAL))
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
            if (belongsTo(settlement.getOwner(), PlayerType.HUMAN))
            {
                player.add(settlement);
            }
        }
        return player;
    }

    private boolean belongsTo(Player player, PlayerType playerType)
    {
        return player.getType().equals(playerType);
    }
}