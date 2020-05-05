package com.github.workinggames.castra.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.model.PlayerColor;
import com.github.workinggames.castra.core.ui.PlayerOptions;
import com.github.workinggames.castra.core.ui.Skins;
import com.github.workinggames.castra.core.ui.WorldOptions;

public class MainMenuScreen extends ScreenAdapter
{
    private final Castra game;
    private final Stage stage;
    private final TextButton startGame;
    private final WorldOptions worldOptions;
    private final PlayerOptions player1Options;
    private final PlayerOptions player2Options;

    public MainMenuScreen(Castra game)
    {
        this.game = game;

        stage = new Stage(game.getViewport());
        game.getInputMultiplexer().addProcessor(stage);

        Skins.initialize(game);

        startGame = new TextButton("Start Game", game.getSkin());
        startGame.addListener(new ClickListener());
        startGame.setPosition(600, 400);
        stage.addActor(startGame);

        worldOptions = new WorldOptions(game);
        worldOptions.setPosition(600, 500);
        stage.addActor(worldOptions);

        player1Options = new PlayerOptions(game,
            "Player1",
            new PlayerColor(new Color(0x4d7afdff), new Color(0x023adaff)));
        player1Options.setPosition(200, 100);
        stage.addActor(player1Options);

        player2Options = new PlayerOptions(game,
            "Player2",
            new PlayerColor(new Color(0xda0205ff), new Color(0x6d0103ff)));
        player2Options.setPosition(600, 100);
        stage.addActor(player2Options);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Batch stageBatch = stage.getBatch();
        stageBatch.begin();
        stageBatch.setColor(Color.WHITE);
        stageBatch.draw(game.getTextureAtlas().findRegion("Background256").getTexture(),
            0,
            0,
            0,
            0,
            (int) game.getViewport().getWorldWidth(),
            (int) game.getViewport().getWorldHeight());
        stageBatch.end();

        stage.act(delta);
        stage.draw();

        if (startGame.isChecked())
        {
            game.setScreen(new GameScreen(game,
                worldOptions.getSeed(),
                player1Options.getPlayer(),
                player2Options.getPlayer()));
            dispose();
        }
    }

    @Override
    public void dispose()
    {
        stage.dispose();
    }
}