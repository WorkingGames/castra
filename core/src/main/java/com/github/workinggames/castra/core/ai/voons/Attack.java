package com.github.workinggames.castra.core.ai.voons;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.badlogic.gdx.utils.Array;

@Getter
@ToString
@RequiredArgsConstructor
public class Attack implements Comparable<Attack>
{
    private final Array<AttackSource> attackSources;
    private final int targetSettlementId;

    @Setter
    private float breakEvenInSeconds;

    @Override
    public int compareTo(Attack o)
    {
        return Float.compare(this.getBreakEvenInSeconds(), o.breakEvenInSeconds);
    }
}