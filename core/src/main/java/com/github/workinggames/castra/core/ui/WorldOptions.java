package com.github.workinggames.castra.core.ui;

import lombok.Getter;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.workinggames.castra.core.Castra;

public class WorldOptions extends Table
{
    @Getter
    private long seed = MathUtils.random(978234L);

    public WorldOptions(Castra game)
    {
        super(game.getSkin());

        Label seedInputLabel = new Label("Seed: ", game.getSkin());
        add(seedInputLabel);

        TextField seedInputField = new TextField("" + seed, game.getSkin());
        seedInputField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        seedInputField.setMaxLength(10);
        seedInputField.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                TextField textField = (TextField) actor;
                String value = textField.getText();
                if (!value.isEmpty())
                {
                    seed = Long.parseLong(value);
                }
                else
                {
                    seed = 0L;
                }
            }
        });
        add(seedInputField);
    }
}