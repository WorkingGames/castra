package de.incub8.castra.core.worldbuilding;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import de.incub8.castra.core.model.Coordinates;
import de.incub8.castra.core.model.Player;
import de.incub8.castra.core.model.PlayerType;
import de.incub8.castra.core.model.Settlement;
import de.incub8.castra.core.model.SettlementSize;
import de.incub8.castra.core.model.World;

@RequiredArgsConstructor
public class WorldBuilder
{
    private static final Player NEUTRAL = new Player(Color.GRAY, "NEUTRAL", PlayerType.NEUTRAL);
    private static final Player AI = new Player(Color.CYAN, "AI", PlayerType.AI);
    private static final Player HUMAN = new Player(Color.GOLDENROD, "Bob", PlayerType.HUMAN);

    private final Coordinates coordinates;

    public World buildWorld()
    {
        World world = new World(buildPlayers(), buildSettlements());
        return world;
    }

    private Array<Settlement> buildSettlements()
    {
        Array<Settlement> settlements = new Array<>();
        settlements.addAll(
            new Settlement(
                SettlementSize.LARGE,
                coordinates.get(50, 50),
                100,
                HUMAN,
                new Texture(Gdx.files.internal("castleHuman.png"))), new Settlement(
                SettlementSize.LARGE,
                coordinates.get(1300, 700),
                100,
                AI,
                new Texture(Gdx.files.internal("castleAI.png"))), new Settlement(
                SettlementSize.MEDIUM,
                coordinates.get(130, 500),
                20,
                NEUTRAL,
                new Texture(Gdx.files.internal("castleNeutralMedium.png"))), new Settlement(
                SettlementSize.MEDIUM,
                coordinates.get(300, 240),
                40,
                NEUTRAL,
                new Texture(Gdx.files.internal("castleNeutralMedium.png"))), new Settlement(
                SettlementSize.SMALL,
                coordinates.get(800, 680),
                30,
                NEUTRAL,
                new Texture(Gdx.files.internal("castleNeutralSmall.png"))), new Settlement(
                SettlementSize.SMALL,
                coordinates.get(1100, 180),
                10,
                NEUTRAL,
                new Texture(Gdx.files.internal("castleNeutralSmall.png"))), new Settlement(
                SettlementSize.SMALL,
                coordinates.get(500, 80),
                25,
                NEUTRAL,
                new Texture(Gdx.files.internal("castleNeutralSmall.png"))), new Settlement(
                SettlementSize.SMALL,
                coordinates.get(900, 400),
                5,
                NEUTRAL,
                new Texture(Gdx.files.internal("castleNeutralSmall.png"))));
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