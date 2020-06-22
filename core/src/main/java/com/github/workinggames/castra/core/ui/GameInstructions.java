package com.github.workinggames.castra.core.ui;

import lombok.Getter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.github.workinggames.castra.core.Castra;

public class GameInstructions extends Table
{
    @Getter
    private final TextButton closeInstructionsButton;

    public GameInstructions(Castra game)
    {
        super(game.getSkin());

        Skin skin = game.getSkin();

        Label instructions = new Label("The goal of this game is to defeat the opponent as fast as possible." +
            "\nThe game is won when the opponent does not control any settlement and when his last army was defeated." +
            "\n" +
            "\nThere are three types of settlements:" +
            "\n  Village (1 soldier every 3 seconds)" +
            "\n  City    (1 soldier every 2 seconds)" +
            "\n  Castle  (1 soldier every second)" +
            "\nThe only difference is how fast new soldiers are recruited." +
            "\nThe flag on top of a settlements indicates if it is neutral or which player owns it." +
            "\nAt the bottom of the settlement is the number of defending soldiers." +
            "\n" +
            "\nIn order to conquer a settlement the player can create armies." +
            "\nTo create an army, the player must drag from one of his owned settlements and drop on the target settlement." +
            "\nThe number of soldiers used for this army depends on the percentage of the army split in the lower left corner." +
            "\nIf set to 50% half of the defending soldiers will be sent out to attack the target." +
            "\nTo change the army split, the player must click it and hold the button down." +
            "\nA dial appears and the player can move to the desired percentage and then release the button." +
            "\n" +
            "\nAll armies travel with the same speed, there are three different sizes:" +
            "\n  Small  (1 to 19 soldiers)" +
            "\n  Medium (20 to 49 soldiers)" +
            "\n  Large  (50+ soldiers)" +
            "\nWhen an army arrives at its target, a battle is started." +
            "\n" +
            "\nBattles are not resolved immediately, the fighting takes some time and the defenders will disappear accordingly." +
            "\nIn order to capture a settlement the army needs to have at least 1 more soldier than the settlement has defenders." +
            "\nAfter a settlement is conquered, it takes some time for the remaining army soldiers until they are ready to defend." +
            "\n" +
            "\nBoth players start with the same amount of soldiers." +
            "\nThe opponents soldiers are hidden, as well as battle progress." +
            "\nThe configurable game speed affects the army travel speed and how fast battles are resolved." +
            "\nThe player must conquer settlements to gain an edge over the opponent." +
            "\n" +
            "\nScreenshot of the game, descriptions are yellow:", skin, "VinqueLarge", Color.BLACK);
        add(instructions).align(Align.left).padLeft(35).padTop(25);
        row();

        Image screenshot = new Image(game.getTextureAtlas().findRegion("InstructionsScreenshot"));
        screenshot.setScaling(Scaling.fit);
        add(screenshot).align(Align.left).padLeft(35).padRight(35).padBottom(10);
        row();

        closeInstructionsButton = new TextButton("Close", skin);
        add(closeInstructionsButton).center();
    }
}