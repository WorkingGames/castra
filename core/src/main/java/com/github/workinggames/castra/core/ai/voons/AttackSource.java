package com.github.workinggames.castra.core.ai.voons;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class AttackSource
{
    private final int settlementId;
    private final int soldiers;
}