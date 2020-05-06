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
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.model.PlayerColorSchema;
import com.github.workinggames.castra.core.model.PlayerType;

public class PlayerOptions extends Table
{
    @Getter
    private final Player player;

    public PlayerOptions(Castra game, String title, PlayerColorSchema color, PlayerType playerType, boolean player1)
    {
        super(game.getSkin());
        Skin skin = game.getSkin();
        player = new Player(color.getPlayerColor(), title, playerType);

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
        addTypeInput(skin, playerType, player1);
        addColor(color, skin);
    }

    private void addColor(PlayerColorSchema color, Skin skin)
    {
        Label playerColorLabel = new Label("Player color schema: ", skin);
        add(playerColorLabel);

        SelectBox<PlayerColorSchema> playerColorSelectBox = new SelectBox<>(skin);
        playerColorSelectBox.setItems(PlayerColorSchema.values());
        playerColorSelectBox.setSelected(color);
        add(playerColorSelectBox);
        row();

        Label playerColor1Label = new Label("Primary color: ", skin);
        add(playerColor1Label);

        Image playerColor1 = new Image(createColorPreview(player.getColor().getPrimaryColor()));
        playerColor1.setSize(20, 10);
        add(playerColor1);
        row();

        Label playerColor2Label = new Label("Secondary color: ", skin);
        add(playerColor2Label);

        Image playerColor2 = new Image(createColorPreview(player.getColor().getSecondaryColor()));
        playerColor2.setSize(20, 10);
        add(playerColor2);
        row();

        playerColorSelectBox.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                SelectBox<PlayerColorSchema> selectBox = (SelectBox<PlayerColorSchema>) actor;
                player.setColor(selectBox.getSelected().getPlayerColor());
                playerColor1.setDrawable(createColorPreview(player.getColor().getPrimaryColor()));
                playerColor2.setDrawable(createColorPreview(player.getColor().getSecondaryColor()));
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
        }
        playerTypeSelectBox.setSelected(initial);
        playerTypeSelectBox.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                SelectBox<PlayerType> selectBox = (SelectBox<PlayerType>) actor;
                player.setType(selectBox.getSelected());
            }
        });
        add(playerTypeSelectBox);
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