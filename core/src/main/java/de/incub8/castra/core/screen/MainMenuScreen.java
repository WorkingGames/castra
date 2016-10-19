package de.incub8.castra.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.incub8.castra.core.Castra;
import de.incub8.castra.core.renderer.Background;

public class MainMenuScreen extends ScreenAdapter
{
    private final Castra game;
    private final SpriteBatch batch;
    private final BitmapFont font;

    private Background background;

    public MainMenuScreen(Castra game)
    {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont();

        background = new Background();
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getCamera().update();

        batch.setProjectionMatrix(game.getCamera().combined);
        batch.begin();
        background.render(batch);
        font.draw(batch, "Welcome to Charge", 600, 350);
        font.draw(batch, "Tap anywhere to begin!", 600, 400);
        batch.end();

        if (Gdx.input.isTouched())
        {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void dispose()
    {
        background.dispose();
        batch.dispose();
        font.dispose();
    }
}