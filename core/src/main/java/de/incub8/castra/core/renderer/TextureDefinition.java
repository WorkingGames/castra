package de.incub8.castra.core.renderer;

import lombok.Data;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

@Data
public class TextureDefinition
{
    private final Texture texture;
    private final Vector2 offset;
}