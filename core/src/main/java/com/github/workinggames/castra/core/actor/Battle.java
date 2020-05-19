package com.github.workinggames.castra.core.actor;

import lombok.Getter;

import com.badlogic.gdx.scenes.scene2d.Group;

public class Battle extends Group
{

    @Getter
    private final Army army;

    public Battle(Army attacker, AnimatedImage animatedImage)
    {
        army = attacker;

        AnimatedImage image = animatedImage.copy();
        addActor(image);

        setSize(image.getWidth(), image.getHeight());
        setPosition((int) (army.getX() + army.getWidth() / 2 - getWidth() / 2),
            (int) (army.getY() + army.getHeight() / 2 - getHeight() / 2));
    }
}