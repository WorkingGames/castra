package com.github.workinggames.castra.core.ai;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AiType
{
    RANDY("Easiest"),
    BILLY("Medium"),
    FRANKY("Medium");

    private final String difficulty;
}