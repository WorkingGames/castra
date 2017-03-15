package de.incub8.castra.core.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ArmySize
{
    SMALL(1, "soldiers"), MEDIUM(50, "soldiers"), LARGE(100, "soldiers");

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