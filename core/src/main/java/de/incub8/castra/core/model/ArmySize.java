package de.incub8.castra.core.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ArmySize
{
    SMALL(1, "army"), MEDIUM(50, "army"), LARGE(100, "army");

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

    @Getter(AccessLevel.PRIVATE)
    private final int minimumSoldiers;

    private final String textureName;
}