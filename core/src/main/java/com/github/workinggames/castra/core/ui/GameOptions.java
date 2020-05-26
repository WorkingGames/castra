package com.github.workinggames.castra.core.ui;

import lombok.Getter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.screen.Screens;

public class GameOptions extends Table
{
    @Getter
    private final TextButton closeOptionsButton;

    public GameOptions(Castra game)
    {
        super(game.getSkin());
        addSeedInput(game);
        addOpponentSettlementDetailsVisible(game);
        addOpponentArmyDetailsVisible(game);
        setBackground(game.getSkin().newDrawable("white", Color.BLACK));

        closeOptionsButton = new TextButton("Close", game.getSkin());
        closeOptionsButton.getLabel().setFontScale(0.95f);
        closeOptionsButton.addListener(new ClickListener());
        closeOptionsButton.setPosition(Screens.getCenterX(closeOptionsButton), Screens.getRelativeY(10));
        addActor(closeOptionsButton);
    }

    private void addSeedInput(Castra game)
    {
        Label seedInputLabel = new Label("Seed: ", game.getSkin());
        add(seedInputLabel);

        TextField seedInputField = new TextField("" + game.getGameConfiguration().getSeed(), game.getSkin());
        seedInputField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        seedInputField.setMaxLength(10);
        seedInputField.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                seedInputField.setText("");
            }
        });
        seedInputField.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                TextField textField = (TextField) actor;
                String value = textField.getText();
                long result = 0L;
                if (!value.isEmpty())
                {
                    result = Long.parseLong(value);
                }
                game.getGameConfiguration().setSeed(result);
            }
        });
        add(seedInputField);
        row();
        row();
    }

    private void addOpponentSettlementDetailsVisible(Castra game)
    {
        Label optionText = new Label("Opponent settlement details visible: ", game.getSkin());
        add(optionText);

        CheckBox optionInput = new CheckBox(null, game.getSkin());
        optionInput.setChecked(game.getGameConfiguration().isOpponentSettlementDetailsVisible());
        optionInput.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                CheckBox box = (CheckBox) actor;
                game.getGameConfiguration().setOpponentSettlementDetailsVisible(box.isChecked());
            }
        });
        add(optionInput);
        row();
    }

    private void addOpponentArmyDetailsVisible(Castra game)
    {
        Label optionText = new Label("Opponent army details visible: ", game.getSkin());
        add(optionText);

        CheckBox optionInput = new CheckBox(null, game.getSkin());
        optionInput.setChecked(game.getGameConfiguration().isOpponentArmyDetailsVisible());
        optionInput.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                CheckBox box = (CheckBox) actor;
                game.getGameConfiguration().setOpponentArmyDetailsVisible(box.isChecked());
            }
        });
        add(optionInput);
        row();
    }
}