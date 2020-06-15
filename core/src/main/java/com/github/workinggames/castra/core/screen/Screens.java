package com.github.workinggames.castra.core.screen;

import lombok.experimental.UtilityClass;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;

@UtilityClass
public class Screens
{
    public final float WIDTH_HEIGHT_RATIO = 0.6f;

    // x - centered image
    public float getCenterX(Actor actor)
    {
        return Gdx.graphics.getWidth() / 2f - actor.getWidth() * actor.getScaleX() / 2f;
    }

    public float getRelativeX(float percent)
    {
        return Gdx.graphics.getWidth() / 100f * percent;
    }

    // y - relative position, like 10%
    public float getRelativeY(float percent)
    {
        return Gdx.graphics.getHeight() / 100f * percent;
    }

    //
    public float getUiScale()
    {
        return 1.8f;
    }

    public Image toBackground(Texture texture, Viewport viewport)
    {
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion textureRegion = new TextureRegion(texture);
        textureRegion.setRegion(0, 0, (int) viewport.getWorldWidth(), (int) viewport.getWorldHeight());
        Image background = new Image(textureRegion);
        background.setZIndex(10000);
        return background;
    }
}