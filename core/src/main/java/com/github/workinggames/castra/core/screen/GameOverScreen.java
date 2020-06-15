package com.github.workinggames.castra.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.workinggames.castra.core.Castra;

public class GameOverScreen extends ScreenAdapter
{
    private final Castra game;
    private final Stage stage;

    public GameOverScreen(Castra game, boolean player1Won, float playTime, int score)
    {
        this.game = game;
        stage = new Stage(game.getViewport());
        game.getInputMultiplexer().clear();
        game.getInputMultiplexer().addProcessor(stage);

        String message = game.getGameConfiguration().getPlayer1().getName() + " Won!";
        if (!player1Won)
        {
            message = game.getGameConfiguration().getPlayer2().getName() + " Won!";
        }
        message = message + " in " + MathUtils.ceil(playTime) + " seconds, getting a score of " + score;

        Label label = new Label(message, game.getSkin());
        label.setPosition(Screens.getCenterX(label), Screens.getRelativeY(60));
        stage.addActor(label);

        TextButton mainMenu = new TextButton("Main Menu", game.getSkin());
        mainMenu.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });
        mainMenu.setPosition(Screens.getCenterX(mainMenu), Screens.getRelativeY(50));
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
    }

    @Override
    public void dispose()
    {
        game.getInputMultiplexer().removeProcessor(stage);
        stage.dispose();
    }
}