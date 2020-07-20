package com.github.workinggames.castra.core.statistics.event;

import com.github.workinggames.castra.core.actor.Army;
import com.github.workinggames.castra.core.model.ArmySize;

public class ArmyDto
{
    private final int armyId;
    private final ArmySize armySize;
    private final int soldiers;
    private final String ownerName;
    private final int sourceSettlementId;
    private final int targetSettlementId;

    public ArmyDto(Army army)
    {
        armyId = army.getId();
        armySize = army.getArmySize();
        soldiers = army.getSoldiers();
        ownerName = army.getOwner().getName();
        sourceSettlementId = army.getSource().getId();
        targetSettlementId = army.getTarget().getId();
    }
}