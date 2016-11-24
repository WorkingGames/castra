package de.incub8.castra.core.texture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationUtil
{
    public Animation createAnimation(Texture texture, int rows, int columns, float frameDuration)
    {
        TextureRegion[][] tmp = TextureRegion.split(
            texture, texture.getWidth() / columns, texture.getHeight() / rows);
        TextureRegion[] animationFrames = new TextureRegion[rows * columns];
        int index = 0;
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                animationFrames[index++] = tmp[i][j];
            }
        }
        Animation animation = new Animation(frameDuration, animationFrames);
        return animation;
    }
}