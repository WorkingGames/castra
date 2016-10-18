package de.incub8.castra.core.model;

import lombok.Getter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

@Getter
public enum TextureDefinition
{
    CASTLE_HUMAN("castleHuman.png", 45, 28),
    CASTLE_AI("castleAI.png", 45, 28),
    NEUTRAL_MEDIUM("castleNeutralMedium.png", 35, 21),
    NEUTRAL_SMALL("castleNeutralSmall.png", 25, 20);

    private final Texture texture;
    private final Vector2 offset;

    private TextureDefinition(String fileName, float offsetX, float offsetY)
    {
        texture = new Texture(Gdx.files.internal(fileName));
        offset = new Vector2(offsetX, offsetY);
    }

    public static void disposeAll()
    {
        for (TextureDefinition textureDefinition : values())
        {
            textureDefinition.getTexture().dispose();
        }
    }
}