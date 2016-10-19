package de.incub8.castra.core.pwnage;

import lombok.RequiredArgsConstructor;

import de.incub8.castra.core.model.Army;
import de.incub8.castra.core.model.Player;
import de.incub8.castra.core.model.PlayerType;
import de.incub8.castra.core.model.Settlement;
import de.incub8.castra.core.model.World;

@RequiredArgsConstructor
public class Pwnage
{
    private final World world;

    public boolean playerLost()
    {
        boolean lost = true;
        for (Settlement settlement : world.getSettlements())
        {
            if (belongsToHuman(settlement.getOwner()))
            {
                lost = false;
                break;
            }
        }
        if (lost)
        {
            for (Army army : world.getArmies())
            {
                if (belongsToHuman(army.getOwner()))
                {
                    lost = false;
                    break;
                }
            }
        }
        return lost;
    }

    private boolean belongsToHuman(Player player)
    {
        return player.getType().equals(PlayerType.HUMAN);
    }

    private boolean belongsToAI(Player player)
    {
        return player.getType().equals(PlayerType.AI);
    }

    public boolean playerWon()
    {
        boolean won = true;
        for (Settlement settlement : world.getSettlements())
        {
            if (belongsToAI(settlement.getOwner()))
            {
                won = false;
                break;
            }
        }
        if (won)
        {
            for (Army army : world.getArmies())
            {
                if (belongsToAI(army.getOwner()))
                {
                    won = false;
                    break;
                }
            }
        }
        return won;
    }
}