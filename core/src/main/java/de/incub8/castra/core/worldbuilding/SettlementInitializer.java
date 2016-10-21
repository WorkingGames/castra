package de.incub8.castra.core.worldbuilding;

import com.badlogic.gdx.graphics.Color;
import de.incub8.castra.core.model.Player;
import de.incub8.castra.core.model.PlayerType;
import de.incub8.castra.core.model.SettlementSize;
import de.incub8.castra.core.stage.World;

class SettlementInitializer
{
    public void initialize(World world)
    {
        Player humanPlayer = world.getHumanPlayer();
        Player aiPlayer = world.getAiPlayer();
        Player neutralPlayer = new Player(Color.GRAY, "NEUTRAL", PlayerType.NEUTRAL);

        world.createSettlement(SettlementSize.LARGE, 50, 50, 100, humanPlayer);
        world.createSettlement(SettlementSize.LARGE, 1200, 700, 100, aiPlayer);
        world.createSettlement(SettlementSize.MEDIUM, 130, 500, 20, neutralPlayer);
        world.createSettlement(SettlementSize.MEDIUM, 300, 240, 40, neutralPlayer);
        world.createSettlement(SettlementSize.LARGE, 800, 680, 60, neutralPlayer);
        world.createSettlement(SettlementSize.SMALL, 1100, 180, 10, neutralPlayer);
        world.createSettlement(SettlementSize.SMALL, 500, 80, 25, neutralPlayer);
        world.createSettlement(SettlementSize.SMALL, 900, 400, 5, neutralPlayer);
    }
}