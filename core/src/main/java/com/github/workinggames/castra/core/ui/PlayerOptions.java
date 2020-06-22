package com.github.workinggames.castra.core.ui;

import lombok.Getter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.ai.AiType;
import com.github.workinggames.castra.core.audio.AudioManager;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.model.PlayerColorSchema;
import com.github.workinggames.castra.core.model.PlayerType;
import com.github.workinggames.castra.core.screen.Screens;

public class PlayerOptions extends Group
{
    private static final int LABEL_INDENTATION = 25;
    private static final int VALUE_INDENTATION = 125;

    @Getter
    private final Player player;
    private final Player opponent;
    private final Skin skin;
    private final TextureAtlas textureAtlas;
    private final String title;
    private final PlayerOptionsGroup optionsGroup;
    private final Image playerBanners;
    private final AudioManager audioManager;

    private SelectBox<String> aiTypeSelectBox;
    private Label aiTypeLabel;
    private Label aiDifficultyLabel;
    private Label aiDifficultyValue;
    private SelectBox<String> playerColorSelectBox;
    private ImageButton aiButton;
    private ImageButton humanButton;
    private Label playerNameLabel;
    private TextField nameInputField;
    private Label playerLabel;

    public PlayerOptions(Castra game, String title, PlayerOptionsGroup optionsGroup)
    {
        skin = game.getSkin();
        textureAtlas = game.getTextureAtlas();
        this.title = title;
        this.optionsGroup = optionsGroup;
        audioManager = game.getAudioManager();
        Screens screens = new Screens(game.getViewport());
        Image playerBackground = new Image(game.getTextureAtlas().findRegion("PlayerMenuBackground"));
        float padding = (screens.getCenterX(playerBackground) - playerBackground.getWidth()) / 2;

        float x;
        if (isPlayerOne())
        {
            player = game.getGameConfiguration().getPlayer1();
            opponent = game.getGameConfiguration().getPlayer2();
            x = padding;
        }
        else
        {
            player = game.getGameConfiguration().getPlayer2();
            opponent = game.getGameConfiguration().getPlayer1();
            x = screens.getCenterX(playerBackground) + playerBackground.getWidth() + padding;
        }

        setPosition(x, 21.6f);
        setSize(484, 667);
        setTransform(false);

        playerBanners = new Image(game.getTextureAtlas().findRegion("PlayerMenuBanners"));
        playerBanners.setColor(player.getColorSchema().getPlayerColor().getPrimaryColor());
        Image playerBannerShade = new Image(game.getTextureAtlas().findRegion("PlayerMenuBannersShade"));
        playerLabel = new Label(title, skin);
        playerLabel.setColor(player.getColorSchema().getFontColor());
        playerLabel.setPosition(185, 625);

        addActor(playerBackground);
        addActor(playerBanners);
        addActor(playerBannerShade);
        addActor(playerLabel);

        addNameInput();
        addColor();
        addTypeInput();
        addAiTypeInput();
        showAiTypeOption(player.isAi());
    }

    private void addNameInput()
    {
        playerNameLabel = new Label("Name: ", skin);
        playerNameLabel.setPosition(LABEL_INDENTATION, 540);
        addActor(playerNameLabel);

        nameInputField = new TextField(player.getName(), skin);
        nameInputField.setPosition(VALUE_INDENTATION, 540);
        nameInputField.setWidth(323);
        nameInputField.setAlignment(Align.center);
        nameInputField.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.input.vibrate(50);
                audioManager.playClickSound();
                nameInputField.setText("");
            }
        });
        nameInputField.setOnlyFontChars(true);
        nameInputField.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Gdx.input.vibrate(50);
                TextField textField = (TextField) actor;
                player.setName(textField.getText());
            }
        });
        addActor(nameInputField);
    }

    private void addColor()
    {
        Label playerColorLabel = new Label("Color: ", skin);
        playerColorLabel.setPosition(LABEL_INDENTATION, 460);
        addActor(playerColorLabel);

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.background = new Image(textureAtlas.findRegion("ColorSelectBoxBackground")).getDrawable();

        SelectBox.SelectBoxStyle colorSelectBoxStyle = skin.get(SelectBox.SelectBoxStyle.class);
        colorSelectBoxStyle.scrollStyle = scrollPaneStyle;

        playerColorSelectBox = new SelectBox<>(colorSelectBoxStyle);
        playerColorSelectBox.setWidth(323);
        playerColorSelectBox.setAlignment(Align.center);
        playerColorSelectBox.setItems(getColorOptions(opponent));
        playerColorSelectBox.setSelected(player.getColorSchema().getLabel());
        playerColorSelectBox.setPosition(VALUE_INDENTATION, 460);
        addActor(playerColorSelectBox);

        playerColorSelectBox.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.input.vibrate(50);
                audioManager.playClickSound();
            }
        });
        playerColorSelectBox.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Gdx.input.vibrate(50);
                audioManager.playClickSound();
                SelectBox<String> selectBox = (SelectBox<String>) actor;
                String colorSchemaLabel = selectBox.getSelected();
                PlayerColorSchema colorSchema = PlayerColorSchema.fromLabel(colorSchemaLabel);
                player.setColorSchema(colorSchema);
                playerBanners.setColor(colorSchema.getPlayerColor().getPrimaryColor());
                playerLabel.setColor(colorSchema.getFontColor());
                optionsGroup.updateOpponentOptions(isPlayerOne());
            }
        });
    }

    void updateColorOptions()
    {
        playerColorSelectBox.setItems(getColorOptions(opponent));
    }

    private Array<String> getColorOptions(Player opponent)
    {
        Array<String> result = new Array<>();
        for (PlayerColorSchema colorSchema : PlayerColorSchema.values())
        {
            if (!colorSchema.equals(PlayerColorSchema.NEUTRAL) && !colorSchema.equals(opponent.getColorSchema()))
            {
                result.add(colorSchema.getLabel());
            }
        }
        return result;
    }

    private void addTypeInput()
    {
        Label playerTypeLabel = new Label("Type: ", skin);
        playerTypeLabel.setPosition(LABEL_INDENTATION, 360);
        addActor(playerTypeLabel);

        Drawable buttonBackground = new Image(textureAtlas.findRegion("TypeButtonBackground")).getDrawable();
        Drawable aiActivated = new Image(textureAtlas.findRegion("AiActivated")).getDrawable();
        Drawable aiDeactivated = new Image(textureAtlas.findRegion("AiDeactivated")).getDrawable();
        ImageButton.ImageButtonStyle aiButtonStyle = new ImageButton.ImageButtonStyle();
        aiButtonStyle.up = buttonBackground;
        aiButtonStyle.imageUp = aiDeactivated;
        aiButtonStyle.imageChecked = aiActivated;

        aiButton = new ImageButton(aiButtonStyle);
        aiButton.setSize(150, 120);
        aiButton.setPosition(VALUE_INDENTATION, 310);
        aiButton.setChecked(player.isAi());
        aiButton.setDisabled(player.isAi());
        aiButton.setProgrammaticChangeEvents(false);
        aiButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Gdx.input.vibrate(50);
                audioManager.playClickSound();
                humanButton.setChecked(false);
                humanButton.setDisabled(false);
                aiButton.setChecked(true);
                aiButton.setDisabled(true);
                player.setType(PlayerType.AI);
                player.setAiType(Player.DEFAULT_AI_TYPE);
                showAiTypeOption(true);
                optionsGroup.updateOpponentOptions(isPlayerOne());
            }
        });
        addActor(aiButton);

        Drawable humanActivated = new Image(textureAtlas.findRegion("HumanActivated")).getDrawable();
        Drawable humanDeactivated = new Image(textureAtlas.findRegion("HumanDeactivated")).getDrawable();
        ImageButton.ImageButtonStyle humanButtonStyle = new ImageButton.ImageButtonStyle();
        humanButtonStyle.up = buttonBackground;
        humanButtonStyle.imageUp = humanDeactivated;
        humanButtonStyle.imageChecked = humanActivated;
        humanButton = new ImageButton(humanButtonStyle);
        humanButton.setSize(150, 120);
        humanButton.setPosition(VALUE_INDENTATION + (323 - 150), 310);
        humanButton.setChecked(player.isHuman());
        humanButton.setDisabled(player.isHuman());
        humanButton.setVisible(opponent.isAi());
        humanButton.setProgrammaticChangeEvents(false);
        humanButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Gdx.input.vibrate(50);
                audioManager.playClickSound();
                aiButton.setChecked(false);
                aiButton.setDisabled(false);
                humanButton.setChecked(true);
                humanButton.setDisabled(true);
                player.setType(PlayerType.HUMAN);
                player.setAiType(null);
                showAiTypeOption(false);
                player.setName(title);
                nameInputField.setText(title);
                optionsGroup.updateOpponentOptions(isPlayerOne());
            }
        });
        addActor(humanButton);
    }

    private void addAiTypeInput()
    {
        aiTypeLabel = new Label("Ai: ", skin);
        aiTypeLabel.setPosition(LABEL_INDENTATION, 230);
        addActor(aiTypeLabel);

        aiTypeSelectBox = new SelectBox<>(skin);
        aiTypeSelectBox.setWidth(323);
        aiTypeSelectBox.setAlignment(Align.center);
        aiTypeSelectBox.setPosition(VALUE_INDENTATION, 230);
        Array<String> aiTypeLabels = new Array<>();
        for (AiType aiType : AiType.values())
        {
            aiTypeLabels.add(aiType.getLabel());
        }
        aiTypeSelectBox.setItems(aiTypeLabels);
        if (player.getAiType() != null)
        {
            aiTypeSelectBox.setSelected(player.getAiType().getLabel());
            player.setName(player.getAiType().name());
        }
        aiTypeSelectBox.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.input.vibrate(50);
                audioManager.playClickSound();
            }
        });
        aiTypeSelectBox.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Gdx.input.vibrate(50);
                audioManager.playClickSound();
                SelectBox<String> selectBox = (SelectBox<String>) actor;
                String selected = selectBox.getSelected();
                AiType aiType = AiType.fromLabel(selected);
                player.setAiType(aiType);
                player.setName(selected);
                aiDifficultyValue.setText(aiType.getDifficulty());
            }
        });
        addActor(aiTypeSelectBox);

        aiDifficultyLabel = new Label("Difficulty: ", skin);
        aiDifficultyLabel.setPosition(LABEL_INDENTATION, 150);
        addActor(aiDifficultyLabel);
        String difficulty = Player.DEFAULT_AI_TYPE.getDifficulty();
        if (player.getAiType() != null)
        {
            difficulty = player.getAiType().getDifficulty();
        }
        aiDifficultyValue = new Label(difficulty, skin);
        aiDifficultyValue.setWidth(323);
        aiDifficultyValue.setAlignment(Align.center);
        aiDifficultyValue.setPosition(VALUE_INDENTATION, 150);
        addActor(aiDifficultyValue);
    }

    void updatePlayerTypeOptions()
    {
        humanButton.setVisible(opponent.isAi());
    }

    private void showAiTypeOption(boolean visible)
    {
        aiTypeSelectBox.setVisible(visible);
        aiTypeLabel.setVisible(visible);
        aiDifficultyLabel.setVisible(visible);
        aiDifficultyValue.setVisible(visible);
        playerNameLabel.setVisible(!visible);
        nameInputField.setVisible(!visible);
    }

    private boolean isPlayerOne()
    {
        return title.equals(PlayerOptionsGroup.PLAYER_1);
    }
}