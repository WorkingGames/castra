package com.github.workinggames.castra.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AttackSource
{
    private final int settlementId;
    private final int soldiers;
}