package com.github.workinggames.castra.core.statistics.event;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.utils.Array;

@RequiredArgsConstructor
public class GameStarted implements VortexEvent
{
    @RequiredArgsConstructor
    public static class Attributes
    {
        private final String gameId;
        private final PlayerDto player1;
        private final PlayerDto player2;
        private final long seed;
        private final Application.ApplicationType platform;
        private final Array<SettlementDto> settlements;
    }

    private final String name = "game_started";
    private final Attributes attributes;
    private final String timestamp;
}