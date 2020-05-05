package com.github.workinggames.castra.core.ui;

import lombok.Getter;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.model.PlayerColor;
import com.github.workinggames.castra.core.model.PlayerType;

public class PlayerOptions extends Table
{
    @Getter
    private final Player player;

    public PlayerOptions(Castra game, String title, PlayerColor playerColor)
    {
        super(game.getSkin());
        Skin skin = game.getSkin();
        player = new Player(playerColor, title, PlayerType.HUMAN);
        
        Label playerLabel = new Label(title, skin);
        add(playerLabel);
        row();

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

        Label playerTypeLabel = new Label("Player type: ", skin);
        add(playerTypeLabel);
        SelectBox<PlayerType> playerTypeSelectBox = new SelectBox<>(skin);
        playerTypeSelectBox.setItems(PlayerType.HUMAN, PlayerType.AI);
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

        // add player color option
    }
}