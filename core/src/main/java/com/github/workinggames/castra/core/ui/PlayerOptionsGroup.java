package com.github.workinggames.castra.core.ui;

import lombok.Getter;

import com.github.workinggames.castra.core.Castra;

public class PlayerOptionsGroup
{
    static final String PLAYER_1 = "Player 1";
    static final String PLAYER_2 = "Player 2";

    @Getter
    private final PlayerOptions player1Options;

    @Getter
    private final PlayerOptions player2Options;

    public PlayerOptionsGroup(Castra game)
    {
        player1Options = new PlayerOptions(game, PLAYER_1, this);
        player2Options = new PlayerOptions(game, PLAYER_2, this);
    }

    void updateOpponentOptions(boolean player1)
    {
        if (player1)
        {
            player2Options.updateColorOptions();
            player2Options.updatePlayerTypeOptions();
        }
        else
        {
            player1Options.updateColorOptions();
            player1Options.updatePlayerTypeOptions();
        }
    }
}