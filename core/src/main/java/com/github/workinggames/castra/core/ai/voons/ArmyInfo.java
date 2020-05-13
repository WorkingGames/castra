package com.github.workinggames.castra.core.ai.voons;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.github.workinggames.castra.core.action.MoveAlongAction;
import com.github.workinggames.castra.core.model.ArmySize;
import com.github.workinggames.castra.core.model.Player;

@Getter
@RequiredArgsConstructor
public class ArmyInfo implements Comparable<ArmyInfo>
{
    private final int armyId;
    private final int soldiers;
    private final Player owner;
    private final ArmySize armySize;
    private final float distance;
    private final float createdAtTimestamp;

    // order by arrival
    public int compareTo(ArmyInfo o)
    {
        return Float.compare(getArrivalTimestamp(), o.getArrivalTimestamp());
    }

    public float getArrivalTimestamp()
    {
        return createdAtTimestamp + getTravelTimeInSeconds();
    }

    public float getTravelTimeInSeconds()
    {
        return distance / MoveAlongAction.PIXEL_PER_SECOND;
    }
}