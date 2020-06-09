package com.github.workinggames.castra.core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GameSpeed
{
    SLOW(100, 0.5f),
    MEDIUM(200, 0.2f),
    FAST(300, 0.03f);

    @Getter
    private final int armySpeed;

    @Getter
    private final float battleProcessingInterval;
}