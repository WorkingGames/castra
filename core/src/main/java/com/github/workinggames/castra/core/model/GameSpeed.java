package com.github.workinggames.castra.core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameSpeed
{
    SLOW(100, 0.5f, "Slow"),
    MEDIUM(200, 0.2f, "Medium"),
    FAST(300, 0.03f, "Fast");

    private final int armySpeed;
    private final float battleProcessingInterval;
    private final String label;

    public static GameSpeed fromLabel(String label)
    {
        return GameSpeed.valueOf(label.toUpperCase());
    }
}