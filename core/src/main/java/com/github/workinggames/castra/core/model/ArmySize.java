package com.github.workinggames.castra.core.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ArmySize
{
    SMALL(1, "soldier"),
    MEDIUM(20, "soldiers"),
    LARGE(50, "horsesAndSoldiers");

    public static ArmySize bySoldierCount(int soldiers)
    {
        ArmySize result = null;
        for (ArmySize armySize : ArmySize.values())
        {
            if (soldiers >= armySize.getMinimumSoldiers())
            {
                result = armySize;
            }
        }
        return result;
    }

    @Getter
    private final int minimumSoldiers;

    private final String textureName;
}