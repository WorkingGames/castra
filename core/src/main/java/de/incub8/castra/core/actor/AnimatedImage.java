package de.incub8.castra.core.actor;

import lombok.Setter;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AnimatedImage extends Image
{
    @Setter
    protected Animation animation = null;
    private float stateTime = 0;

    public AnimatedImage(Animation animation)
    {
        super(animation.getKeyFrame(0));
        this.animation = animation;
    }

    public AnimatedImage(TextureRegion textureRegion)
    {
        super(textureRegion);
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