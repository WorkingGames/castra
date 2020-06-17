package com.github.workinggames.castra.core.ui;

import lombok.Getter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.screen.Screens;

public class GameInstructions extends Table
{
    @Getter
    private final TextButton closeInstructionsButton;

    public GameInstructions(Castra game)
    {
        super(game.getSkin());

        setSize(Screens.getRelativeY(100), Screens.getRelativeY(100));

        setBackground(game.getSkin().newDrawable("white", Color.BLACK));

        Label settlements = new Label("There are three types of settlements:" +
            "\n  Village (1 soldier every 3 seconds)" +
            "\n  City    (1 soldier every 2 seconds)" +
            "\n  Castle  (1 soldier every second)" +
            "\nThey don't only differ in size but also how fast new soldiers are recruited." +
            "\nNon neutral settlements have a player colored flag on top of them." +
            "\nAt the bottom of the settlement is the number of defending soldiers." +
            "\nThe number of soldiers defending the opponents settlements are hidden.", game.getSkin());
        add(settlements).align(Align.left);
        row().padTop(20).padBottom(20);

        Label armies = new Label("In order to capture a settlement the players can create armies." +
            "\nThis is done by dragging from the source settlement and dropping on the target settlement." +
            "\nThere are three army sizes:" +
            "\n  Small  (1 to 19 soldiers)" +
            "\n  Medium (20 to 49 soldiers)" +
            "\n  Large  (50+ soldiers" +
            "\nAll armies travel with the same speed." +
            "\nThe number of soldiers in an army are hidden from the opponent but can be guessed according to size." +
            "\nThe army split in the lower left corner can be used to control the percentage of soldiers to send out.",
            game.getSkin());
        add(armies).align(Align.left);
        row().padBottom(20);

        Label goal = new Label("The goal of this game is to annihilate the opponent as fast as possible." +
            "\nAs both players start with the same amount of soldiers, neutral settlements should be used" +
            "\nto gain an edge until it is enough to capture the opponents settlements.", game.getSkin());
        add(goal).align(Align.left);
        row().padBottom(20);

        closeInstructionsButton = new TextButton("Close", game.getSkin());
        closeInstructionsButton.getLabel().setFontScale(0.95f);
        add(closeInstructionsButton).center();
    }
}