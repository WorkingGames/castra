package com.github.workinggames.castra.core.ui;

import lombok.Getter;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.model.PlayerColor;
import com.github.workinggames.castra.core.model.PlayerType;

public class PlayerOptions extends Table
{
    @Getter
    private final Player player;

    public PlayerOptions(Castra game, String title, PlayerColor playerColor, PlayerType playerType)
    {
        super(game.getSkin());
        Skin skin = game.getSkin();
        player = new Player(playerColor, title, playerType);

        addTitle(title, skin);
        addNameInput(title, skin);
        addTypeInput(skin, playerType);
        addColor(playerColor, skin);
    }

    private void addColor(PlayerColor playerColor, Skin skin)
    {
        Pixmap pixmap = new Pixmap(20, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(playerColor.getColors().get(0));
        pixmap.fill();
        TextureRegionDrawable color1 = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        pixmap.setColor(playerColor.getColors().get(1));
        pixmap.fill();
        TextureRegionDrawable color2 = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        Label playerColor1Label = new Label("Player color1: ", skin);
        add(playerColor1Label);

        ImageButton playerColor1Button = new ImageButton(color1);
        playerColor1Button.setSize(20, 10);
        add(playerColor1Button);
        row();

        Label playerColor2Label = new Label("Player color2: ", skin);
        add(playerColor2Label);

        ImageButton playerColor2Button = new ImageButton(color2);
        playerColor2Button.setSize(20, 10);
        add(playerColor2Button);
        row();
    }

    private void addTitle(String title, Skin skin)
    {
        Label playerLabel = new Label(title, skin);
        add(playerLabel);
        row();
    }

    private void addTypeInput(Skin skin, PlayerType initial)
    {
        Label playerTypeLabel = new Label("Player type: ", skin);
        add(playerTypeLabel);
        SelectBox<PlayerType> playerTypeSelectBox = new SelectBox<>(skin);
        playerTypeSelectBox.setItems(PlayerType.HUMAN, PlayerType.AI);
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