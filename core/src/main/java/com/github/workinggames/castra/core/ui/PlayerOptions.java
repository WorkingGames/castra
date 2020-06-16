package com.github.workinggames.castra.core.ui;

import lombok.Getter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.ai.AiType;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.model.PlayerColorSchema;
import com.github.workinggames.castra.core.model.PlayerType;

public class PlayerOptions extends Table
{
    @Getter
    private final Player player;
    private final Player opponent;
    private final Skin skin;
    private final String title;
    private final PlayerOptionsGroup optionsGroup;

    private SelectBox<AiType> aiTypeSelectBox;
    private Label aiTypeLabel;
    private Label aiDifficultyLabel;
    private Label aiDifficultyValue;
    private Label aiDescriptionLabel;
    private Label aiDescriptionValue;
    private SelectBox<PlayerColorSchema> playerColorSelectBox;
    private SelectBox<PlayerType> playerTypeSelectBox;
    private Label playerNameLabel;
    private TextField nameInputField;

    public PlayerOptions(Castra game, String title, PlayerOptionsGroup optionsGroup)
    {
        super(game.getSkin());
        skin = game.getSkin();
        this.title = title;
        this.optionsGroup = optionsGroup;
        if (isPlayerOne())
        {
            player = game.getGameConfiguration().getPlayer1();
            opponent = game.getGameConfiguration().getPlayer2();
        }
        else
        {
            player = game.getGameConfiguration().getPlayer2();
            opponent = game.getGameConfiguration().getPlayer1();
        }

        addTitle();
        addNameInput();
        addColor();
        addTypeInput();
        addAiTypeInput();
        showAiTypeOption(player.isAi());
    }

    private void addColor()
    {
        Label playerColorLabel = new Label("Color schema: ", skin);
        add(playerColorLabel);

        playerColorSelectBox = new SelectBox<>(skin);
        playerColorSelectBox.setItems(getColorOptions(opponent));
        playerColorSelectBox.setSelected(player.getColorSchema());
        add(playerColorSelectBox).minWidth(300);
        row();

        Label playerColor1Label = new Label("Primary color: ", skin);
        add(playerColor1Label);

        Image playerColor1 = new Image(createColorPreview(player.getColorSchema().getPlayerColor().getPrimaryColor()));
        playerColor1.setSize(20, 10);
        add(playerColor1);
        row().padTop(10);

        playerColorSelectBox.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.input.vibrate(50);
            }
        });
        playerColorSelectBox.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Gdx.input.vibrate(50);
                SelectBox<PlayerColorSchema> selectBox = (SelectBox<PlayerColorSchema>) actor;
                player.setColorSchema(selectBox.getSelected());
                playerColor1.setDrawable(createColorPreview(player.getColorSchema()
                    .getPlayerColor()
                    .getPrimaryColor()));
                optionsGroup.updateOpponentOptions(isPlayerOne());
            }
        });
    }

    void updateColorOptions()
    {
        playerColorSelectBox.setItems(getColorOptions(opponent));
    }

    private Array<PlayerColorSchema> getColorOptions(Player opponent)
    {
        Array<PlayerColorSchema> availableColorSchemas = new Array<>(PlayerColorSchema.values());
        availableColorSchemas.removeValue(PlayerColorSchema.NEUTRAL, true);
        availableColorSchemas.removeValue(opponent.getColorSchema(), true);
        return availableColorSchemas;
    }

    private Drawable createColorPreview(Color color)
    {
        Pixmap pixmap = new Pixmap(20, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        return new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
    }

    private void addTitle()
    {
        Label playerLabel = new Label(title, skin);
        add(playerLabel);
        row().padBottom(10);
    }

    private void addTypeInput()
    {
        Label playerTypeLabel = new Label("Player type: ", skin);
        add(playerTypeLabel);

        playerTypeSelectBox = new SelectBox<>(skin);
        playerTypeSelectBox.setItems(getPlayerTypeOptions(opponent));
        playerTypeSelectBox.setSelected(player.getType());
        playerTypeSelectBox.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.input.vibrate(50);
            }
        });
        playerTypeSelectBox.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Gdx.input.vibrate(50);
                SelectBox<PlayerType> selectBox = (SelectBox<PlayerType>) actor;
                PlayerType selected = selectBox.getSelected();
                player.setType(selected);
                showAiTypeOption(player.isAi());
                if (!player.isAi())
                {
                    player.setAiType(null);
                    player.setName(title);
                    nameInputField.setText(title);
                }
                else if (player.getAiType() == null)
                {
                    player.setAiType(Player.DEFAULT_AI_TYPE);
                }
                optionsGroup.updateOpponentOptions(isPlayerOne());
            }
        });
        add(playerTypeSelectBox).minWidth(300);
        row().padTop(10);
    }

    private Array<PlayerType> getPlayerTypeOptions(Player opponent)
    {
        Array<PlayerType> result = new Array<>();
        result.add(PlayerType.AI);
        if (opponent.isAi())
        {
            result.add(PlayerType.HUMAN);
        }
        return result;
    }

    void updatePlayerTypeOptions()
    {
        playerTypeSelectBox.setItems(getPlayerTypeOptions(opponent));
    }

    private void showAiTypeOption(boolean visible)
    {
        aiTypeSelectBox.setVisible(visible);
        aiTypeLabel.setVisible(visible);
        aiDifficultyLabel.setVisible(visible);
        aiDifficultyValue.setVisible(visible);
        aiDescriptionLabel.setVisible(visible);
        aiDescriptionValue.setVisible(visible);
        playerNameLabel.setVisible(!visible);
        nameInputField.setVisible(!visible);
    }

    private void addAiTypeInput()
    {
        aiTypeLabel = new Label("Ai Player: ", skin);
        add(aiTypeLabel);

        aiTypeSelectBox = new SelectBox<>(skin);
        aiTypeSelectBox.setItems(AiType.values());
        if (player.getAiType() != null)
        {
            aiTypeSelectBox.setSelected(player.getAiType());
            player.setName(player.getAiType().name());
        }
        aiTypeSelectBox.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.input.vibrate(50);
            }
        });
        aiTypeSelectBox.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Gdx.input.vibrate(50);
                SelectBox<AiType> selectBox = (SelectBox<AiType>) actor;
                AiType selected = selectBox.getSelected();
                player.setAiType(selected);
                player.setName(selected.name());
                aiDifficultyValue.setText(selected.getDifficulty());
                aiDescriptionValue.setText(selected.getDescription());
            }
        });
        add(aiTypeSelectBox).minWidth(300);
        row();

        aiDifficultyLabel = new Label("Difficulty: ", skin);
        add(aiDifficultyLabel);
        String difficulty = Player.DEFAULT_AI_TYPE.getDifficulty();
        if (player.getAiType() != null)
        {
            difficulty = player.getAiType().getDifficulty();
        }
        aiDifficultyValue = new Label(difficulty, skin);
        add(aiDifficultyValue);
        row();

        aiDescriptionLabel = new Label("Description: ", skin);
        add(aiDescriptionLabel);
        String description = Player.DEFAULT_AI_TYPE.getDescription();
        if (player.getAiType() != null)
        {
            description = player.getAiType().getDescription();
        }
        aiDescriptionValue = new Label(description, skin);
        add(aiDescriptionValue);
    }

    private void addNameInput()
    {
        playerNameLabel = new Label("Name: ", skin);
        add(playerNameLabel);
        nameInputField = new TextField(player.getName(), skin);
        nameInputField.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.input.vibrate(50);
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
        add(nameInputField).minWidth(400);
        row().padBottom(10);
    }

    private boolean isPlayerOne()
    {
        return title.equals(PlayerOptionsGroup.PLAYER_1);
    }
}