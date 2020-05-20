package com.github.workinggames.castra.core.statistics.event;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BattleEnded implements VortexEvent
{
    @RequiredArgsConstructor
    public static class Attributes
    {
        private final String gameId;
        private final PlayerDto attacker;
        private final SettlementDto settlement;
        private final boolean captured;
    }

    private final String name = "battle_ended";
    private final Attributes attributes;
    private final String timestamp;
}