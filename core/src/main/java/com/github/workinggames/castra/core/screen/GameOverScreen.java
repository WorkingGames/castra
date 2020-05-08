package com.github.workinggames.castra.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.workinggames.castra.core.Castra;

public class GameOverScreen extends ScreenAdapter
{
    private final Castra game;
    private final Stage stage;
    private final TextButton reMatch;
    private final TextButton mainMenu;

    public GameOverScreen(Castra game, boolean won)
    {
        this.game = game;

        stage = new Stage(game.getViewport());
        game.getInputMultiplexer().addProcessor(stage);

        String message = "You Won!";
        if (!won)
        {
            message = "You Loose!";
        }

        Label label = new Label(message, game.getSkin());
        label.setPosition(600, 600);
        stage.addActor(label);

        reMatch = new TextButton("Rematch", game.getSkin());
        reMatch.addListener(new ClickListener());
        reMatch.setPosition(600, 500);
        stage.addActor(reMatch);

        mainMenu = new TextButton("Main Menu", game.getSkin());
        mainMenu.addListener(new ClickListener());
        mainMenu.setPosition(600, 450);
        stage.addActor(mainMenu);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        stage.getBatch().setColor(Color.WHITE);
        stage.getBatch()
            .draw(game.getTextureAtlas().findRegion("Background256").getTexture(),
                0,
                0,
                0,
                0,
                (int) game.getViewport().getWorldWidth(),
                (int) game.getViewport().getWorldHeight());
        stage.getBatch().end();

        stage.act(delta);

        stage.draw();

        if (reMatch.isChecked())
        {
            game.setScreen(new GameScreen(game));
            dispose();
        }
        if (mainMenu.isChecked())
        {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override
    public void dispose()
    {
        stage.dispose();
    }
}