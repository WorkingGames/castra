package com.github.workinggames.castra.core.ai;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class Attack implements Comparable<Attack>
{
    private final int sourceSettlementId;
    private final int targetSettlementId;
    private final int requiredSoldiers;
    private final float breakEvenInSeconds;

    @Override
    public int compareTo(Attack o)
    {
        return Float.compare(this.getBreakEvenInSeconds(), o.breakEvenInSeconds);
    }
}