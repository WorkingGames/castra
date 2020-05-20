package com.github.workinggames.castra.core.statistics.event;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArmyDeployed implements VortexEvent
{
    @RequiredArgsConstructor
    public static class Attributes
    {
        private final String gameId;
        private final PlayerDto player;
        private final int armyId;
        private final int soldiers;
        private final SettlementDto sourceSettlement;
        private final SettlementDto targetSettlement;
        private final float distance;
    }

    private final String name = "army_deployed";
    private final Attributes attributes;
    private final String timestamp;
}