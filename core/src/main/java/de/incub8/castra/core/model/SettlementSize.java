package de.incub8.castra.core.model;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.graphics.Texture;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SettlementSize
{
    SMALL(1, null), MEDIUM(2, null), LARGE(3, null);

    private final int spawnAmount;
    private final Texture texture;
}