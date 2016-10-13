package de.incub8.castra.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.incub8.castra.core.Castra;

public class GameScreen extends ScreenAdapter
{
    public GameScreen(Castra game)
    {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    private final Castra game;
    private final SpriteBatch batch;
    private final BitmapFont font;

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getCamera().update();

        batch.setProjectionMatrix(game.getCamera().combined);
        batch.begin();
        font.draw(batch, "Coming Soon...", 600, 350);
        batch.end();
    }

    @Override
    public void dispose()
    {
        batch.dispose();
        font.dispose();
    }
}
