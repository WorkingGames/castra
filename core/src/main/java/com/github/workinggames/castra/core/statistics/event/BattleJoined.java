package com.github.workinggames.castra.core.statistics.event;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BattleJoined implements VortexEvent
{
    @RequiredArgsConstructor
    public static class Attributes
    {
        private final String gameId;
        private final PlayerDto attacker;
        private final int battleSoldiers;
        private final int armyId;
        private final int armySoldiers;
        private final SettlementDto settlement;
    }

    private final String name = "battle_joined";
    private final Attributes attributes;
    private final String timestamp;
}