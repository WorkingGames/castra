package com.github.workinggames.castra.core.ui;

import lombok.Getter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.audio.AudioManager;
import com.github.workinggames.castra.core.model.GameSpeed;
import com.github.workinggames.castra.core.screen.Screens;

public class GameOptions extends WidgetGroup
{
    private static final float MIN_STARTING_SOLDIERS = 10;
    private static final float MAX_STARTING_SOLDIERS = 200;
    private static final float STARTING_SOLDIERS_STEP_SIZE = 10;

    @Getter
    private final TextButton closeOptionsButton;
    private final Skin skin;
    private final Screens screens;
    private final AudioManager audioManager;

    public GameOptions(Castra game)
    {
        skin = game.getSkin();
        screens = new Screens(game.getViewport());
        audioManager = game.getAudioManager();

        Image background = new Image(game.getTextureAtlas().findRegion("GameOptions"));
        background.setPosition(screens.getCenterX(background), screens.getRelativeY(20));
        addActor(background);

        addSeedInput(game);
        addGameSpeedSelectBox(game);
        if (game.getGameConfiguration().isDebug())
        {
            addOpponentSettlementDetailsVisible(game);
            addOpponentArmyDetailsVisible(game);
            addStartSoldiersSlider(game);
        }

        closeOptionsButton = new TextButton("Close", skin);
        closeOptionsButton.setPosition(screens.getCenterX(closeOptionsButton), screens.getRelativeY(27));
        addActor(closeOptionsButton);
    }

    private void addSeedInput(Castra game)
    {
        Label seedInputLabel = new Label("Seed: ", skin);
        seedInputLabel.setPosition(screens.getRelativeX(40), screens.getRelativeY(64));
        addActor(seedInputLabel);

        TextField seedInputField = new TextField("" + game.getGameConfiguration().getSeed(), skin);
        seedInputField.setPosition(screens.getRelativeX(50), screens.getRelativeY(64));
        seedInputField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        seedInputField.setMaxLength(10);
        seedInputField.setWidth(200);
        seedInputField.setAlignment(Align.center);
        seedInputField.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.input.vibrate(50);
                audioManager.playClickSound();
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
        addActor(seedInputField);
    }

    private void addGameSpeedSelectBox(Castra game)
    {
        Label gameSpeedLabel = new Label("Game speed: ", skin);
        gameSpeedLabel.setPosition(screens.getRelativeX(40), screens.getRelativeY(57));
        addActor(gameSpeedLabel);

        SelectBox<String> gameSpeedSelectBox = new SelectBox<>(skin);
        gameSpeedSelectBox.setWidth(200);
        gameSpeedSelectBox.setAlignment(Align.center);
        gameSpeedSelectBox.setPosition(screens.getRelativeX(50), screens.getRelativeY(57));
        Array<String> items = new Array<>();
        for (GameSpeed gameSpeed : GameSpeed.values())
        {
            items.add(gameSpeed.getLabel());
        }
        gameSpeedSelectBox.setItems(items);
        gameSpeedSelectBox.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.input.vibrate(50);
                audioManager.playClickSound();
            }
        });
        gameSpeedSelectBox.setSelected(game.getGameConfiguration().getGameSpeed().getLabel());
        gameSpeedSelectBox.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Gdx.input.vibrate(50);
                audioManager.playClickSound();
                SelectBox<String> selectBox = (SelectBox<String>) actor;
                GameSpeed selected = GameSpeed.fromLabel(selectBox.getSelected());
                game.getGameConfiguration().setGameSpeed(selected);
            }
        });
        addActor(gameSpeedSelectBox);
    }

    private void addOpponentSettlementDetailsVisible(Castra game)
    {
        Label optionText = new Label("Opponent settlement details visible: ", game.getSkin());
        optionText.setPosition(screens.getRelativeX(30), screens.getRelativeY(49));
        addActor(optionText);

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
        optionInput.setPosition(screens.getRelativeX(60), screens.getRelativeY(49));
        addActor(optionInput);
    }

    private void addOpponentArmyDetailsVisible(Castra game)
    {
        Label optionText = new Label("Opponent army details visible: ", game.getSkin());
        optionText.setPosition(screens.getRelativeX(30), screens.getRelativeY(42));
        addActor(optionText);

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
        optionInput.setPosition(screens.getRelativeX(60), screens.getRelativeY(42));
        addActor(optionInput);
    }

    private void addStartSoldiersSlider(Castra game)
    {
        Label optionText = new Label("Starting soldiers: ", game.getSkin());
        optionText.setPosition(screens.getRelativeX(30), screens.getRelativeY(35));
        addActor(optionText);

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
        optionInput.setPosition(screens.getRelativeX(60), screens.getRelativeY(35));
        addActor(optionInput);
    }
}