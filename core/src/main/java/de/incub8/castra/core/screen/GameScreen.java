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
import de.incub8.castra.core.model.TextureDefinition;
import de.incub8.castra.core.model.World;
import de.incub8.castra.core.renderer.AbstractRenderable;
import de.incub8.castra.core.renderer.Background;
import de.incub8.castra.core.renderer.SettlementRenderable;
import de.incub8.castra.core.worldbuilding.WorldBuilder;

public class GameScreen extends ScreenAdapter
{
    private final Castra game;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final World world;

    private Array<AbstractRenderable> renderables;

    private Background background;

    public GameScreen(Castra game)
    {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont();

        world = new WorldBuilder().buildWorld();

        renderables = new Array<>();
        background = new Background();
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getCamera().update();

        batch.setProjectionMatrix(game.getCamera().combined);
        font.setColor(Color.WHITE);

        updateRenderables();

        batch.begin();
        background.render(batch);
        for (AbstractRenderable abstractRenderable : renderables)
        {
            abstractRenderable.render(batch, font);
        }
        batch.end();
    }

    private void updateRenderables()
    {
        renderables.clear();
        for (Settlement settlement : world.getSettlements())
        {
            renderables.add(new SettlementRenderable(settlement));
        }
        renderables.sort();
    }

    @Override
    public void dispose()
    {
        background.dispose();
        TextureDefinition.disposeAll();
        batch.dispose();
        font.dispose();
    }
}