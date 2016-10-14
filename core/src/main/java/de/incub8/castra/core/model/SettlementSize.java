package de.incub8.castra.core.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.graphics.Texture;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum SettlementSize
{
    SMALL(1, null, 50, 28), MEDIUM(2, null, 70, 39), LARGE(3, null, 90, 50);

    private final int spawnAmount;
    private final Texture texture;
    private final int width;
    private final int height;
}