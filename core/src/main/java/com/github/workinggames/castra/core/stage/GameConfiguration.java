package com.github.workinggames.castra.core.stage;

import lombok.Data;

import com.badlogic.gdx.math.MathUtils;
import com.github.workinggames.castra.core.model.Player;

@Data
public class GameConfiguration
{
    private long seed = MathUtils.random(978234L);

    private Player player1;
    private Player player2;

    private boolean opponentSettlementDetailsVisible = false;
    private boolean opponentArmyDetailsVisible = false;
}