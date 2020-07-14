package com.github.workinggames.castra.core.ui;

import lombok.Getter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
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
    private static final int LABEL_BEGIN = 39;
    private static final int VALUE_BEGIN = 51;

    @Getter
    private final TextButton closeOptionsButton;
    private final Skin skin;
    private final Screens screens;
    private final AudioManager audioManager;
    private final Castra game;

    public GameOptions(Castra game)
    {
        this.game = game;
        skin = game.getSkin();
        screens = new Screens(game.getViewport());
        audioManager = game.getAudioManager();

        Image background = new Image(game.getTextureAtlas().findRegion("GameOptions"));
        background.setPosition(screens.getCenterX(background), screens.getRelativeY(20));
        addActor(background);

        addSeedInput();
        addGameSpeedSelectBox();
        if (game.isHumbleAssetsPresent())
        {
            addMusicVolumeInput();
            addSoundVolumeInput();
        }
        if (game.getGameConfiguration().isDebug())
        {
            addOpponentSettlementDetailsVisible();
            addOpponentArmyDetailsVisible();
            addStartSoldiersSlider();
        }

        closeOptionsButton = new TextButton("Close", skin);
        closeOptionsButton.setPosition(screens.getCenterX(closeOptionsButton), screens.getRelativeY(27));
        addActor(closeOptionsButton);
    }

    private void addSeedInput()
    {
        Label seedInputLabel = new Label("Seed: ", skin);
        seedInputLabel.setPosition(screens.getRelativeX(LABEL_BEGIN), screens.getRelativeY(64));
        addActor(seedInputLabel);

        TextField seedInputField = new TextField("" + game.getGameConfiguration().getSeed(), skin);
        seedInputField.setPosition(screens.getRelativeX(VALUE_BEGIN), screens.getRelativeY(64));
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

    private void addGameSpeedSelectBox()
    {
        Label gameSpeedLabel = new Label("Game speed: ", skin);
        gameSpeedLabel.setPosition(screens.getRelativeX(LABEL_BEGIN), screens.getRelativeY(57));
        addActor(gameSpeedLabel);

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.background = new Image(game.getTextureAtlas().findRegion("AiTypeSelectBox")).getDrawable();

        SelectBox.SelectBoxStyle selectBoxStyle = skin.get(SelectBox.SelectBoxStyle.class);
        selectBoxStyle.scrollStyle = scrollPaneStyle;

        SelectBox<String> gameSpeedSelectBox = new SelectBox<>(selectBoxStyle);
        gameSpeedSelectBox.setWidth(200);
        gameSpeedSelectBox.setAlignment(Align.center);
        gameSpeedSelectBox.setPosition(screens.getRelativeX(VALUE_BEGIN), screens.getRelativeY(57));
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

    private void addMusicVolumeInput()
    {
        Label optionText = new Label("Music Volume: ", skin);
        optionText.setPosition(screens.getRelativeX(LABEL_BEGIN), screens.getRelativeY(50));
        addActor(optionText);

        Slider optionInput = new Slider(0, 1, 0.05f, false, skin);
        optionInput.setValue(game.getAudioManager().getMusicVolume());
        optionInput.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Slider slider = (Slider) actor;
                game.getAudioManager().updateMusicVolume(slider.getValue());
            }
        });
        optionInput.setSize(200, optionInput.getHeight());
        optionInput.setPosition(screens.getRelativeX(VALUE_BEGIN), screens.getRelativeY(50));
        addActor(optionInput);
    }

    private void addSoundVolumeInput()
    {
        Label optionText = new Label("Sound Volume: ", skin);
        optionText.setPosition(screens.getRelativeX(LABEL_BEGIN), screens.getRelativeY(43));
        addActor(optionText);

        Slider optionInput = new Slider(0, 1, 0.05f, false, skin);
        optionInput.setValue(game.getAudioManager().getSoundVolume());
        optionInput.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Slider slider = (Slider) actor;
                game.getAudioManager().updateSoundVolume(slider.getValue());
            }
        });
        optionInput.setSize(200, optionInput.getHeight());
        optionInput.setPosition(screens.getRelativeX(VALUE_BEGIN), screens.getRelativeY(43));
        addActor(optionInput);
    }

    private void addOpponentSettlementDetailsVisible()
    {
        Label optionText = new Label("Opponent settlement details visible: ", skin);
        optionText.setPosition(screens.getRelativeX(30), screens.getRelativeY(49));
        addActor(optionText);

        CheckBox optionInput = new CheckBox(null, skin);
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

    private void addOpponentArmyDetailsVisible()
    {
        Label optionText = new Label("Opponent army details visible: ", skin);
        optionText.setPosition(screens.getRelativeX(30), screens.getRelativeY(42));
        addActor(optionText);

        CheckBox optionInput = new CheckBox(null, skin);
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

    private void addStartSoldiersSlider()
    {
        Label optionText = new Label("Starting soldiers: ", skin);
        optionText.setPosition(screens.getRelativeX(30), screens.getRelativeY(35));
        addActor(optionText);

        Slider optionInput = new Slider(MIN_STARTING_SOLDIERS,
            MAX_STARTING_SOLDIERS,
            STARTING_SOLDIERS_STEP_SIZE,
            false,
            skin);
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