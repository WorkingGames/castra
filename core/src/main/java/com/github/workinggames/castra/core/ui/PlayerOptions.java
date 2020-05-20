package com.github.workinggames.castra.core.ui;

import lombok.Getter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
    private static final AiType DEFAULT_AI_TYPE = AiType.RANDY;

    @Getter
    private final Player player;

    private SelectBox<AiType> aiTypeSelectBox;
    private Label aiTypeLabel;
    private Label aiDifficultyLabel;
    private Label aiDifficultyValue;
    private Label aiDescriptionLabel;
    private Label aiDescriptionValue;

    public PlayerOptions(Castra game, String title, PlayerColorSchema color, PlayerType playerType, boolean player1)
    {
        super(game.getSkin());
        Skin skin = game.getSkin();
        player = new Player(color, title, playerType);

        if (player1)
        {
            game.getGameConfiguration().setPlayer1(player);
        }
        else
        {
            game.getGameConfiguration().setPlayer2(player);
        }

        addTitle(title, skin);
        addNameInput(title, skin);
        addColor(color, skin);
        addTypeInput(skin, playerType, player1);
        addAiTypeInput(skin);
        showAiTypeOption(!player1);
    }

    private void addColor(PlayerColorSchema color, Skin skin)
    {
        Label playerColorLabel = new Label("Player color schema: ", skin);
        add(playerColorLabel);

        SelectBox<PlayerColorSchema> playerColorSelectBox = new SelectBox<>(skin);
        Array<PlayerColorSchema> availableColorSchemas = new Array<>(PlayerColorSchema.values());
        availableColorSchemas.removeValue(PlayerColorSchema.NEUTRAL, true);
        playerColorSelectBox.setItems(availableColorSchemas);
        playerColorSelectBox.setSelected(color);
        add(playerColorSelectBox);
        row();

        Label playerColor1Label = new Label("Primary color: ", skin);
        add(playerColor1Label);

        Image playerColor1 = new Image(createColorPreview(player.getColorSchema().getPlayerColor().getPrimaryColor()));
        playerColor1.setSize(20, 10);
        add(playerColor1);
        row();

        Label playerColor2Label = new Label("Secondary color: ", skin);
        add(playerColor2Label);

        Image playerColor2 = new Image(createColorPreview(player.getColorSchema()
            .getPlayerColor()
            .getSecondaryColor()));
        playerColor2.setSize(20, 10);
        add(playerColor2);
        row();

        playerColorSelectBox.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                SelectBox<PlayerColorSchema> selectBox = (SelectBox<PlayerColorSchema>) actor;
                player.setColorSchema(selectBox.getSelected());
                playerColor1.setDrawable(createColorPreview(player.getColorSchema()
                    .getPlayerColor()
                    .getPrimaryColor()));
                playerColor2.setDrawable(createColorPreview(player.getColorSchema()
                    .getPlayerColor()
                    .getSecondaryColor()));
            }
        });
    }

    private Drawable createColorPreview(Color color)
    {
        Pixmap pixmap = new Pixmap(20, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        return new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
    }

    private void addTitle(String title, Skin skin)
    {
        Label playerLabel = new Label(title, skin);
        add(playerLabel);
        row();
    }

    private void addTypeInput(Skin skin, PlayerType initial, boolean player1)
    {
        Label playerTypeLabel = new Label("Player type: ", skin);
        add(playerTypeLabel);

        SelectBox<PlayerType> playerTypeSelectBox = new SelectBox<>(skin);
        if (player1)
        {
            playerTypeSelectBox.setItems(PlayerType.HUMAN, PlayerType.AI);
        }
        else
        {
            playerTypeSelectBox.setItems(PlayerType.AI);
            player.setAiType(DEFAULT_AI_TYPE);
        }
        playerTypeSelectBox.setSelected(initial);
        playerTypeSelectBox.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                SelectBox<PlayerType> selectBox = (SelectBox<PlayerType>) actor;
                PlayerType selected = selectBox.getSelected();
                player.setType(selected);

                boolean aiPlayer = selected.equals(PlayerType.AI);
                showAiTypeOption(aiPlayer);
                if (aiPlayer)
                {
                    player.setAiType(aiTypeSelectBox.getSelected());
                }
                else
                {
                    player.setAiType(null);
                }
            }
        });
        add(playerTypeSelectBox);
        row();
    }

    private void showAiTypeOption(boolean visible)
    {
        aiTypeSelectBox.setVisible(visible);
        aiTypeLabel.setVisible(visible);
        aiDifficultyLabel.setVisible(visible);
        aiDifficultyValue.setVisible(visible);
        aiDescriptionLabel.setVisible(visible);
        aiDescriptionValue.setVisible(visible);
    }

    private void addAiTypeInput(Skin skin)
    {
        aiTypeLabel = new Label("Ai Player: ", skin);
        add(aiTypeLabel);

        aiTypeSelectBox = new SelectBox<>(skin);
        aiTypeSelectBox.setItems(AiType.values());
        aiTypeSelectBox.setSelected(DEFAULT_AI_TYPE);
        aiTypeSelectBox.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                SelectBox<AiType> selectBox = (SelectBox<AiType>) actor;
                AiType selected = selectBox.getSelected();
                player.setAiType(selected);
                aiDifficultyValue.setText(selected.getDifficulty());
                aiDescriptionValue.setText(selected.getDescription());
            }
        });
        add(aiTypeSelectBox);
        row();

        aiDifficultyLabel = new Label("Difficulty: ", skin);
        add(aiDifficultyLabel);
        aiDifficultyValue = new Label(DEFAULT_AI_TYPE.getDifficulty(), skin);
        add(aiDifficultyValue);
        row();

        aiDescriptionLabel = new Label("Ai Player: ", skin);
        add(aiDescriptionLabel);
        aiDescriptionValue = new Label(DEFAULT_AI_TYPE.getDescription(), skin);
        add(aiDescriptionValue);
        row();
    }

    private void addNameInput(String title, Skin skin)
    {
        Label playerNameLabel = new Label("Name: ", skin);
        add(playerNameLabel);
        TextField nameInputField = new TextField(title, skin);
        nameInputField.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                TextField textField = (TextField) actor;
                player.setName(textField.getText());
            }
        });
        add(nameInputField);
        row();
    }
}