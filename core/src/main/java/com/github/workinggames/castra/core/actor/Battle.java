package com.github.workinggames.castra.core.actor;

import lombok.Getter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.github.workinggames.castra.core.texture.AnimationUtil;

public class Battle extends Group
{
    private static final int FRAME_COLS = 6;
    private static final int FRAME_ROWS = 4;

    @Getter
    private final Army army;

    private final AnimationUtil animationUtil;

    public Battle(Army attacker, TextureAtlas textureAtlas)
    {
        this.animationUtil = new AnimationUtil();
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
        Animation battleAnimation = animationUtil.createAnimation(battleTexture, FRAME_ROWS, FRAME_COLS, 0.3f);
        battleAnimation.setPlayMode(Animation.PlayMode.LOOP_RANDOM);
        return battleAnimation;
    }
}