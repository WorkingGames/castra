package de.incub8.castra.core.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum SettlementSize
{
    SMALL(3, "SmallCastles"), MEDIUM(2, "MediumCastles"), LARGE(1, "LargeCastles");

    private final float spawnIntervalInSeconds;
    private final String textureName;
}