package com.github.workinggames.castra.core;

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