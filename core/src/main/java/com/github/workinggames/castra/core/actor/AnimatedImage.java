package com.github.workinggames.castra.core.actor;

import lombok.Getter;
import lombok.Setter;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AnimatedImage extends Image
{
    @Getter
    @Setter
    protected Animation<TextureRegion> animation = null;

    private float stateTime = 0;

    public AnimatedImage(Animation<TextureRegion> animation)
    {
        super(animation.getKeyFrame(0));
        this.animation = animation;
    }

    public AnimatedImage(TextureRegion textureRegion)
    {
        super(textureRegion);
    }

    public AnimatedImage(Drawable drawable)
    {
        super(drawable);
    }

    public AnimatedImage copy()
    {
        if (animation != null)
        {
            return new AnimatedImage(animation);
        }
        else
        {
            return new AnimatedImage(getDrawable());
        }
    }

    @Override
    public void act(float delta)
    {
        if (animation != null)
        {
            ((TextureRegionDrawable) getDrawable()).setRegion(animation.getKeyFrame(stateTime += delta, true));
        }
        super.act(delta);
    }
}