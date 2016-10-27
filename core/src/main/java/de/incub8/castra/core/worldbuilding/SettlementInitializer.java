package de.incub8.castra.core.worldbuilding;

import com.badlogic.gdx.graphics.Color;
import de.incub8.castra.core.model.Player;
import de.incub8.castra.core.model.PlayerColor;
import de.incub8.castra.core.model.PlayerType;
import de.incub8.castra.core.model.SettlementSize;
import de.incub8.castra.core.stage.World;

class SettlementInitializer
{
    public void initialize(World world)
    {
        Player humanPlayer = world.getHumanPlayer();
        Player aiPlayer = world.getAiPlayer();
        Player neutralPlayer = new Player(
            new PlayerColor(new Color(0xc8c8c8ff), new Color(0x4b4b4bff)), 0, "NEUTRAL", PlayerType.NEUTRAL);

        world.createSettlement(SettlementSize.LARGE, 50, 50, 100, humanPlayer);
        world.createSettlement(SettlementSize.LARGE, 1100, 580, 100, aiPlayer);
        world.createSettlement(SettlementSize.MEDIUM, 130, 500, 20, neutralPlayer);
        world.createSettlement(SettlementSize.MEDIUM, 300, 240, 40, neutralPlayer);
        world.createSettlement(SettlementSize.LARGE, 700, 500, 60, neutralPlayer);
        world.createSettlement(SettlementSize.SMALL, 1000, 180, 10, neutralPlayer);
        world.createSettlement(SettlementSize.SMALL, 500, 80, 25, neutralPlayer);
        world.createSettlement(SettlementSize.SMALL, 900, 400, 5, neutralPlayer);
    }
}