package com.github.workinggames.castra.core.statistics.event;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.utils.Array;

@RequiredArgsConstructor
public class GameCanceled implements VortexEvent
{
    @RequiredArgsConstructor
    public static class Attributes
    {
        private final String gameId;
        private final long seed;
        private final Array<SettlementDto> settlements;
        private final Array<ArmyDto> armies;
        private final int playTime;
    }

    private final String name = "game_canceled";
    private final Attributes attributes;
    private final String timestamp;
}