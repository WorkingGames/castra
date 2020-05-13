package com.github.workinggames.castra.core.ai.voons;

import java.util.Comparator;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import com.github.workinggames.castra.core.actor.Settlement;

@RequiredArgsConstructor
public class SettlementDistanceComparator implements Comparator<Settlement>
{
    private final SettlementInfo target;

    @Override
    public int compare(Settlement o1, Settlement o2)
    {
        Map<Integer, Float> settlementDistancesInTicks = target.getSettlementDistancesInTicks();
        return Float.compare(settlementDistancesInTicks.get(o1.getId()), settlementDistancesInTicks.get(o2.getId()));
    }
}