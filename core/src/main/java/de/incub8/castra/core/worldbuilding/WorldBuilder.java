package de.incub8.castra.core.worldbuilding;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import de.incub8.castra.core.model.Paths;
import de.incub8.castra.core.model.Player;
import de.incub8.castra.core.model.PlayerType;
import de.incub8.castra.core.model.Settlement;
import de.incub8.castra.core.model.SettlementSize;
import de.incub8.castra.core.model.TextureDefinition;
import de.incub8.castra.core.model.World;
import de.incub8.castra.core.pathfinding.PathCreator;

public class WorldBuilder
{
    private static final Player NEUTRAL = new Player(Color.GRAY, "NEUTRAL", PlayerType.NEUTRAL);
    private static final Player AI = new Player(Color.CYAN, "AI", PlayerType.AI);
    private static final Player HUMAN = new Player(Color.GOLDENROD, "Bob", PlayerType.HUMAN);

    public World buildWorld()
    {
        Array<Player> players = buildPlayers();
        Array<Settlement> settlements = buildSettlements();
        Paths paths = new PathCreator().create(settlements);
        World world = new World(players, settlements, paths);
        return world;
    }

    private Array<Settlement> buildSettlements()
    {
        Array<Settlement> settlements = new Array<>();
        settlements.addAll(
            new Settlement(
                SettlementSize.LARGE, new GridPoint2(50, 50), 100, HUMAN, TextureDefinition.NEUTRAL_LARGE),
            new Settlement(
                SettlementSize.LARGE, new GridPoint2(1300, 700), 100, AI, TextureDefinition.NEUTRAL_LARGE),
            new Settlement(
                SettlementSize.MEDIUM, new GridPoint2(130, 500), 20, NEUTRAL, TextureDefinition.NEUTRAL_MEDIUM),
            new Settlement(
                SettlementSize.MEDIUM, new GridPoint2(300, 240), 40, NEUTRAL, TextureDefinition.NEUTRAL_MEDIUM),
            new Settlement(
                SettlementSize.LARGE, new GridPoint2(800, 680), 60, NEUTRAL, TextureDefinition.NEUTRAL_LARGE),
            new Settlement(
                SettlementSize.SMALL, new GridPoint2(1100, 180), 10, NEUTRAL, TextureDefinition.NEUTRAL_SMALL),
            new Settlement(
                SettlementSize.SMALL, new GridPoint2(500, 80), 25, NEUTRAL, TextureDefinition.NEUTRAL_SMALL),
            new Settlement(
                SettlementSize.SMALL, new GridPoint2(900, 400), 5, NEUTRAL, TextureDefinition.NEUTRAL_SMALL));
        return settlements;
    }

    private Array<Player> buildPlayers()
    {
        Array<Player> players = new Array<>();
        players.add(NEUTRAL);
        players.add(AI);
        players.add(HUMAN);
        return players;
    }
}