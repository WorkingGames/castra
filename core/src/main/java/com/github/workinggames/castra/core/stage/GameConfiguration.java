package com.github.workinggames.castra.core.stage;

import lombok.Data;

import com.badlogic.gdx.math.MathUtils;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.model.PlayerColorSchema;
import com.github.workinggames.castra.core.model.PlayerType;

@Data
public class GameConfiguration
{
    public static final Player NEUTRAL_PLAYER = new Player(PlayerColorSchema.NEUTRAL,
        "NEUTRAL",
        PlayerType.NEUTRAL,
        false);

    private long seed = MathUtils.random(978234L);

    private Player player1 = new Player(PlayerColorSchema.RED, "Player 1", PlayerType.HUMAN, true);
    private Player player2 = new Player(PlayerColorSchema.BLUE, "Player 2", PlayerType.AI, false);

    private boolean opponentSettlementDetailsVisible = false;
    private boolean opponentArmyDetailsVisible = false;
    private float armyTravelSpeedInPixelPerSecond = 300;
    private float battleProcessingInterval = 0.1f;
    private int startingSoldiers = 100;
    private boolean debugAI = false;
}