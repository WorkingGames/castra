package de.incub8.castra.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import de.incub8.castra.core.Castra;
import de.incub8.castra.core.model.Settlement;
import de.incub8.castra.core.model.World;
import de.incub8.castra.core.renderer.AbstractRenderable;
import de.incub8.castra.core.renderer.SettlementRenderable;
import de.incub8.castra.core.worldbuilding.WorldBuilder;

public class GameScreen extends ScreenAdapter
{
    private final Castra game;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final World world;

    private Array<AbstractRenderable> elements;

    public GameScreen(Castra game)
    {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont();
        world = new WorldBuilder().buildWorld();
        elements = new Array<>();
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getCamera().update();

        batch.setProjectionMatrix(game.getCamera().combined);
        font.setColor(Color.WHITE);

        for (Settlement settlement : world.getSettlements())
        {
            elements.add(new SettlementRenderable(settlement));
        }
        elements.sort();

        batch.begin();
        for (AbstractRenderable abstractRenderable : elements)
        {
            abstractRenderable.render(batch, font);
        }
        batch.end();
    }

    @Override
    public void dispose()
    {
        batch.dispose();
        font.dispose();
    }
}