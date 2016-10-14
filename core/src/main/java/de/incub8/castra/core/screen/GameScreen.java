package de.incub8.castra.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Ellipse;
import de.incub8.castra.core.Castra;
import de.incub8.castra.core.model.Coordinates;
import de.incub8.castra.core.model.PlayerType;
import de.incub8.castra.core.model.Settlement;
import de.incub8.castra.core.model.World;
import de.incub8.castra.core.pathfinding.PathEnhancer;
import de.incub8.castra.core.worldbuilding.WorldBuilder;

public class GameScreen extends ScreenAdapter
{
    private final Castra game;
    private final SpriteBatch batch;
    private final BitmapFont font;
    private final World world;

    public GameScreen(Castra game)
    {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont();

        Coordinates coordinates = new Coordinates(Castra.VIEWPORT_WIDTH, Castra.VIEWPORT_HEIGHT);

        world = new WorldBuilder(coordinates).buildWorld();

        new PathEnhancer(coordinates).enhance(world);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getCamera().update();

        batch.setProjectionMatrix(game.getCamera().combined);

        font.setColor(Color.WHITE);

        batch.begin();
        for (Settlement settlement : world.getSettlements())
        {
            Ellipse hitbox = settlement.getHitbox();
            batch.draw(settlement.getTexture(), hitbox.x, hitbox.y);
            // AI settlements have hidden soldier sizes
            if (!settlement.getOwner().getType().equals(PlayerType.AI))
            {
                font.draw(
                    batch, "" + (settlement.getSoldiers()), settlement.getPosition().x, settlement.getPosition().y);
            }
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