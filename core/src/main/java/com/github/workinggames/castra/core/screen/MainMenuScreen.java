package com.github.workinggames.castra.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.model.PlayerColorSchema;
import com.github.workinggames.castra.core.model.PlayerType;
import com.github.workinggames.castra.core.ui.PlayerOptions;
import com.github.workinggames.castra.core.ui.Skins;
import com.github.workinggames.castra.core.ui.WorldOptions;

public class MainMenuScreen extends ScreenAdapter
{
    private final Castra game;
    private final Stage stage;
    private final TextButton startGame;
    private final Texture backgroundTexture;

    public MainMenuScreen(Castra game)
    {
        this.game = game;

        stage = new Stage(game.getViewport());
        game.getInputMultiplexer().addProcessor(stage);

        Skins.initialize(game);

        Image title = new Image(game.getTextureAtlas().findRegion("title"));
        title.setPosition(600, 700);
        stage.addActor(title);

        startGame = new TextButton("Start Game", game.getSkin());
        startGame.addListener(new ClickListener());
        startGame.setPosition(600, 400);
        stage.addActor(startGame);

        WorldOptions worldOptions = new WorldOptions(game);
        worldOptions.setPosition(600, 500);
        stage.addActor(worldOptions);

        PlayerOptions player1Options = new PlayerOptions(game,
            "Player1",
            PlayerColorSchema.BLUE,
            PlayerType.HUMAN,
            true);
        player1Options.setPosition(400, 200);
        stage.addActor(player1Options);

        PlayerOptions player2Options = new PlayerOptions(game, "Player2", PlayerColorSchema.RED, PlayerType.AI, false);
        player2Options.setPosition(900, 200);
        stage.addActor(player2Options);

        backgroundTexture = game.getTextureAtlas().findRegion("Bricks").getTexture();
        backgroundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Batch stageBatch = stage.getBatch();
        stageBatch.begin();
        stageBatch.setColor(Color.WHITE);
        stageBatch.draw(backgroundTexture,
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
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void dispose()
    {
        stage.dispose();
    }
}