package de.incub8.castra.core.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum SettlementSize
{
    SMALL(3, "SmallCastles", "SmallShadow"),
    MEDIUM(2, "MediumCastles", "MediumShadow"),
    LARGE(1, "LargeCastles", "LargeShadow");

    private final float spawnIntervalInSeconds;
    private final String textureName;
    private final String highlightTextureName;
}