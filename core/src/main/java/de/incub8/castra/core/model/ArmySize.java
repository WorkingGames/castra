package de.incub8.castra.core.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

@RequiredArgsConstructor
public enum ArmySize
{
    SMALL(1, null), MEDIUM(50, null), LARGE(100, null);

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

    @Getter
    private final TextureRegion[] textureRegions;
}