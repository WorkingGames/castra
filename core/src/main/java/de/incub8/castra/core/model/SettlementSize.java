package de.incub8.castra.core.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum SettlementSize
{
    SMALL(3, "castleNeutralSmall"), MEDIUM(2, "castleNeutralMedium"), LARGE(1, "castleNeutralLarge");

    private final float spawnIntervalInSeconds;
    private final String textureName;
}