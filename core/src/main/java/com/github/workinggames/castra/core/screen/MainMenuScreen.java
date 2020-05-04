package com.github.workinggames.castra.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.ui.Skins;

public class MainMenuScreen extends ScreenAdapter
{
    private final Castra game;
    private final Stage stage;
    private final TextButton randomSeed;

    private long seed = MathUtils.random(978234L);

    public MainMenuScreen(Castra game)
    {
        this.game = game;

        stage = new Stage(game.getViewport());
        game.getInputMultiplexer().addProcessor(stage);

        Skins.initialize(game);

        randomSeed = new TextButton("Start Game", game.getSkin());
        randomSeed.addListener(new ClickListener());
        randomSeed.setPosition(600, 400);
        stage.addActor(randomSeed);

        TextField seedInputField = new TextField("" + seed, game.getSkin());
        seedInputField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        seedInputField.setMaxLength(10);
        seedInputField.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                TextField textField = (TextField) actor;
                String value = textField.getText();
                if (!value.isEmpty())
                {
                    seed = Long.parseLong(value);
                }
                else
                {
                    seed = 0L;
                }
            }
        });
        seedInputField.setPosition(600, 500);
        stage.addActor(seedInputField);
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

        if (randomSeed.isChecked())
        {
            game.setScreen(new GameScreen(game, seed));
            dispose();
        }
    }

    @Override
    public void dispose()
    {
        stage.dispose();
    }
}