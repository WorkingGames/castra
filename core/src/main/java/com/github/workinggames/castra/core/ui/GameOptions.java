package com.github.workinggames.castra.core.ui;

import lombok.Getter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.model.GameSpeed;
import com.github.workinggames.castra.core.screen.Screens;

public class GameOptions extends Table
{
    private static final float MIN_STARTING_SOLDIERS = 10;
    private static final float MAX_STARTING_SOLDIERS = 200;
    private static final float STARTING_SOLDIERS_STEP_SIZE = 10;

    @Getter
    private final TextButton closeOptionsButton;

    public GameOptions(Castra game)
    {
        super(game.getSkin());

        addSeedInput(game);
        addGameSpeedSelectBox(game);
        if (game.getGameConfiguration().isDebug())
        {
            addOpponentSettlementDetailsVisible(game);
            addOpponentArmyDetailsVisible(game);
            addStartSoldiersSlider(game);
        }
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
                Gdx.input.vibrate(50);
                seedInputField.setText("");
            }
        });
        seedInputField.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Gdx.input.vibrate(50);
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
        row().padBottom(10).padTop(10);
    }

    private void addGameSpeedSelectBox(Castra game)
    {
        Skin skin = game.getSkin();
        Label gameSpeedLabel = new Label("Game speed: ", skin);
        add(gameSpeedLabel);

        SelectBox gameSpeedSelectBox = new SelectBox<>(skin);
        gameSpeedSelectBox.setItems(GameSpeed.values());
        gameSpeedSelectBox.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.input.vibrate(50);
            }
        });
        gameSpeedSelectBox.setSelected(game.getGameConfiguration().getGameSpeed());
        gameSpeedSelectBox.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Gdx.input.vibrate(50);
                SelectBox<GameSpeed> selectBox = (SelectBox<GameSpeed>) actor;
                GameSpeed selected = selectBox.getSelected();
                game.getGameConfiguration().setGameSpeed(selected);
            }
        });
        add(gameSpeedSelectBox).minWidth(200);
        row().padBottom(10).padTop(10);
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
        row().padBottom(10).padTop(10);
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
        row().padBottom(10).padTop(10);
    }

    private void addStartSoldiersSlider(Castra game)
    {
        Label optionText = new Label("Starting soldiers: ", game.getSkin());
        add(optionText);

        Slider optionInput = new Slider(MIN_STARTING_SOLDIERS,
            MAX_STARTING_SOLDIERS,
            STARTING_SOLDIERS_STEP_SIZE,
            false,
            game.getSkin());
        optionInput.setValue(game.getGameConfiguration().getStartingSoldiers());
        optionInput.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Slider slider = (Slider) actor;
                game.getGameConfiguration().setStartingSoldiers((int) slider.getValue());
            }
        });
        add(optionInput);
        row().padBottom(10).padTop(10);
    }
}