package com.github.workinggames.castra.core.screen;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;

@RequiredArgsConstructor
public class Screens
{
    public static final float WIDTH_HEIGHT_RATIO = 0.6f;

    private final Viewport viewport;

    // x - centered image
    public float getCenterX(Actor actor)
    {
        return viewport.getWorldWidth() / 2f - actor.getWidth() * actor.getScaleX() / 2f;
    }

    public float getRelativeX(float percent)
    {
        return viewport.getWorldWidth() / 100f * percent;
    }

    // y - relative position, like 10%
    public float getRelativeY(float percent)
    {
        return viewport.getWorldHeight() / 100f * percent;
    }

    public Image toBackground(Texture texture)
    {
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion textureRegion = new TextureRegion(texture);
        textureRegion.setRegion(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        Image background = new Image(textureRegion);
        return background;
    }
}