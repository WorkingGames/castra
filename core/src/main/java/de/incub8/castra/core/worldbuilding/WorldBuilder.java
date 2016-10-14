package de.incub8.castra.core.worldbuilding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import de.incub8.castra.core.model.Player;
import de.incub8.castra.core.model.PlayerType;
import de.incub8.castra.core.model.Settlement;
import de.incub8.castra.core.model.SettlementSize;
import de.incub8.castra.core.model.World;

public class WorldBuilder
{
    private static final Player NEUTRAL = new Player(Color.GRAY, "NEUTRAL", PlayerType.NEUTRAL);
    private static final Player AI = new Player(Color.CYAN, "AI", PlayerType.AI);
    private static final Player HUMAN = new Player(Color.GOLDENROD, "Bob", PlayerType.HUMAN);

    private static final Settlement SETTLEMENT_1 = new Settlement(
        SettlementSize.LARGE, new GridPoint2(50, 50), 100, HUMAN, new Texture(Gdx.files.internal("castleHuman.png")));
    private static final Settlement SETTLEMENT_2 = new Settlement(
        SettlementSize.LARGE, new GridPoint2(1300, 700), 100, AI, new Texture(Gdx.files.internal("castleAI.png")));
    private static final Settlement SETTLEMENT_3 = new Settlement(
        SettlementSize.MEDIUM,
        new GridPoint2(130, 500),
        20,
        NEUTRAL,
        new Texture(Gdx.files.internal("castleNeutralMedium.png")));
    private static final Settlement SETTLEMENT_4 = new Settlement(
        SettlementSize.MEDIUM,
        new GridPoint2(300, 240),
        40,
        NEUTRAL,
        new Texture(Gdx.files.internal("castleNeutralMedium.png")));
    private static final Settlement SETTLEMENT_5 = new Settlement(
        SettlementSize.SMALL,
        new GridPoint2(800, 680),
        30,
        NEUTRAL,
        new Texture(Gdx.files.internal("castleNeutralSmall.png")));
    private static final Settlement SETTLEMENT_6 = new Settlement(
        SettlementSize.SMALL,
        new GridPoint2(1100, 180),
        10,
        NEUTRAL,
        new Texture(Gdx.files.internal("castleNeutralSmall.png")));
    private static final Settlement SETTLEMENT_7 = new Settlement(
        SettlementSize.SMALL,
        new GridPoint2(500, 80),
        25,
        NEUTRAL,
        new Texture(Gdx.files.internal("castleNeutralSmall.png")));
    private static final Settlement SETTLEMENT_8 = new Settlement(
        SettlementSize.SMALL,
        new GridPoint2(900, 400),
        5,
        NEUTRAL,
        new Texture(Gdx.files.internal("castleNeutralSmall.png")));

    public World buildWorld()
    {
        World world = new World(buildPlayers(), buildSettlements());
        return world;
    }

    private Array<Settlement> buildSettlements()
    {
        Array<Settlement> settlements = new Array<>();
        settlements.addAll(
            SETTLEMENT_1,
            SETTLEMENT_2,
            SETTLEMENT_3,
            SETTLEMENT_4,
            SETTLEMENT_5,
            SETTLEMENT_6,
            SETTLEMENT_7,
            SETTLEMENT_8);
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