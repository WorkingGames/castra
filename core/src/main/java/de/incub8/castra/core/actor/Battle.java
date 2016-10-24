package de.incub8.castra.core.actor;

import lombok.Getter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;

public class Battle extends Group
{
    private static final int FRAME_COLS = 6;
    private static final int FRAME_ROWS = 4;

    @Getter
    private final Army army;

    public Battle(Army attacker, TextureAtlas textureAtlas)
    {
        army = attacker;

        AnimatedImage animatedImage = new AnimatedImage(createAnimation(textureAtlas));
        addActor(animatedImage);

        setSize(animatedImage.getWidth(), animatedImage.getHeight());
        setPosition(
            (int) (army.getX() + army.getWidth() / 2 - getWidth() / 2),
            (int) (army.getY() + army.getHeight() / 2 - getHeight() / 2));
    }

    private Animation createAnimation(TextureAtlas textureAtlas)
    {
        Texture battleTexture = textureAtlas.findRegion("cloud").getTexture();
        TextureRegion[][] tmp = TextureRegion.split(
            battleTexture, battleTexture.getWidth() / FRAME_COLS, battleTexture.getHeight() / FRAME_ROWS);
        TextureRegion[] battleFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++)
        {
            for (int j = 0; j < FRAME_COLS; j++)
            {
                battleFrames[index++] = tmp[i][j];
            }
        }
        Animation battleAnimation = new Animation(0.3f, battleFrames);
        battleAnimation.setPlayMode(Animation.PlayMode.LOOP_RANDOM);
        return battleAnimation;
    }
}