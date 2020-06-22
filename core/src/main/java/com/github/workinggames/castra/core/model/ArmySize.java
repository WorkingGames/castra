package com.github.workinggames.castra.core.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ArmySize
{
    SMALL(1, "singleSoldier", 53),
    MEDIUM(20, "mediumSoldiers", 35),
    LARGE(50, "LargeSoldiers", 40);

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

    private final int minimumSoldiers;

    private final String textureName;

    private final int animationOffset;
}