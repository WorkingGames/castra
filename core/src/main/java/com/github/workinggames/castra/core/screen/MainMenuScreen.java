package com.github.workinggames.castra.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.model.PlayerColorSchema;
import com.github.workinggames.castra.core.model.PlayerType;
import com.github.workinggames.castra.core.ui.GameOptions;
import com.github.workinggames.castra.core.ui.PlayerOptions;
import com.github.workinggames.castra.core.ui.Skins;

public class MainMenuScreen extends ScreenAdapter
{
    private final Castra game;
    private final Stage stage;
    private final TextButton startGameButton;
    private final TextButton gameOptionsButton;
    private final Texture backgroundTexture;
    private final GameOptions gameOptions;
    private final PlayerOptions player1Options;
    private final PlayerOptions player2Options;

    public MainMenuScreen(Castra game)
    {
        this.game = game;

        stage = new Stage(game.getViewport());
        game.getInputMultiplexer().addProcessor(stage);

        Skins.initialize(game);

        addTitle(game);

        gameOptions = new GameOptions(game);
        gameOptions.setSize(game.getViewport().getWorldWidth() - 200, game.getViewport().getWorldHeight() - 250);
        gameOptions.setPosition(Screens.getCenterX(gameOptions), Screens.getRelativeY(5));
        gameOptions.setVisible(false);
        gameOptions.getCloseOptionsButton().addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                gameOptions.getCloseOptionsButton().setChecked(false);
                gameOptions.setVisible(false);
                gameOptionsButton.setVisible(true);
                startGameButton.setVisible(true);
                player1Options.setVisible(true);
                player2Options.setVisible(true);
            }
        });
        stage.addActor(gameOptions);

        startGameButton = new TextButton("Start Game", game.getSkin());
        startGameButton.getLabel().setFontScale(0.95f);
        startGameButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.setScreen(new LoadingScreen(game));
                dispose();
            }
        });
        startGameButton.setPosition(Screens.getCenterX(startGameButton), Screens.getRelativeY(60));
        stage.addActor(startGameButton);

        gameOptionsButton = new TextButton("Game Options", game.getSkin());
        gameOptionsButton.getLabel().setFontScale(0.95f);
        gameOptionsButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                gameOptionsButton.setChecked(false);
                gameOptions.setVisible(true);
                gameOptionsButton.setVisible(false);
                startGameButton.setVisible(false);
                player1Options.setVisible(false);
                player2Options.setVisible(false);
            }
        });
        gameOptionsButton.setPosition(Screens.getCenterX(startGameButton), Screens.getRelativeY(52));
        stage.addActor(gameOptionsButton);

        player1Options = new PlayerOptions(game, "Player1", PlayerColorSchema.BLUE, PlayerType.HUMAN, true);
        player1Options.setPosition(Screens.getRelativeX(30), Screens.getRelativeY(20));
        stage.addActor(player1Options);

        player2Options = new PlayerOptions(game, "Player2", PlayerColorSchema.RED, PlayerType.AI, false);
        player2Options.setPosition(Screens.getRelativeX(70), Screens.getRelativeY(20));
        stage.addActor(player2Options);

        backgroundTexture = game.getTextureAtlas().findRegion("Bricks").getTexture();
        backgroundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    private void addTitle(Castra game)
    {
        Drawable drawable = game.getSkin().newDrawable("white", Color.BLACK);
        Image titleBox = new Image(drawable);

        Label title = new Label("CHARGE!", game.getSkin(), "title");
        title.setPosition(Screens.getCenterX(title), Screens.getRelativeY(90));
        titleBox.setSize(title.getWidth() + 30, title.getHeight() + 30);
        titleBox.setPosition(title.getX() - 15, title.getY() - 15);

        stage.addActor(titleBox);
        stage.addActor(title);
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
    }

    @Override
    public void dispose()
    {
        stage.dispose();
    }
}