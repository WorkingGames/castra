package com.github.workinggames.castra.core.statistics.event;

import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.model.SettlementSize;

public class SettlementDto
{
    private final int settlementId;
    private final SettlementSize settlementSize;
    private final int soldiers;
    private final String ownerName;

    public SettlementDto(Settlement settlement)
    {
        settlementId = settlement.getId();
        settlementSize = settlement.getSize();
        soldiers = settlement.getSoldiers();
        ownerName = settlement.getOwner().getName();
    }
}