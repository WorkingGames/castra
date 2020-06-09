package com.github.workinggames.castra.core.statistics.event;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.utils.Array;

@RequiredArgsConstructor
public class GameEnded implements VortexEvent
{
    @RequiredArgsConstructor
    public static class Attributes
    {
        private final String gameId;
        private final PlayerDto winner;
        private final Array<SettlementDto> settlements;
        private final int playTime;
    }

    private final String name = "game_ended";
    private final Attributes attributes;
    private final String timestamp;
}