package com.github.workinggames.castra.core.ai;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AiType
{
    RANDY("Randy", "Easiest"),
    BILLY("Billy", "Medium"),
    FRANKY("Franky", "Hard");

    private final String label;
    private final String difficulty;

    public static AiType fromLabel(String label)
    {
        return AiType.valueOf(label.toUpperCase());
    }
}