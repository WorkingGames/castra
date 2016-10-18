package de.incub8.castra.core.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum SettlementSize
{
    SMALL(1, 50, 28), MEDIUM(2, 70, 39), LARGE(3, 90, 50);

    private final int spawnAmount;
    private final int width;
    private final int height;
}