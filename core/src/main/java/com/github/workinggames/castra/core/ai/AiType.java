package com.github.workinggames.castra.core.ai;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AiType
{
    RANDY("Easiest", "Attacks randomly."),
    BILLY("Medium", "Attacks cautiously.");

    private final String difficulty;
    private final String description;
}