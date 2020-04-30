package com.github.workinggames.castra.core.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum SettlementSize
{
    SMALL(3, "SmallCastlePink", "SmallCastleNeutralHighlight", "SmallCastleHighlight", "SmallCastleFlags"),
    MEDIUM(2, "MediumCastlePink", "MediumCastleNeutralHighlight", "MediumCastleHighlight", "MediumCastleFlags"),
    LARGE(1, "LargeCastlePink", "LargeCastleNeutralHighlight", "LargeCastleHighlight", "LargeCastleFlags");

    private final float spawnIntervalInSeconds;
    private final String textureName;
    private final String neutralHighlight;
    private final String highlightAnimationName;
    private final String flagAnimationName;
}