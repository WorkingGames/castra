package com.github.workinggames.castra.core.ai;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.github.workinggames.castra.core.actor.Settlement;

@Data
@AllArgsConstructor
public class SettlementBar implements Comparable<SettlementBar>
{
    private Settlement settlement;
    private float cost;

    @Override
    public int compareTo(SettlementBar o)
    {
        // reversed as lowest cost should be first after sorting
        return Float.compare(o.getCost(), this.getCost());
    }
}