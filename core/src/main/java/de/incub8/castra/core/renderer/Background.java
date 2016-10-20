package de.incub8.castra.core.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.incub8.castra.core.Castra;

public class Background
{
    private final Texture texture;

    public Background()
    {
        texture = new Texture("Background128.png");
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    public void render(Batch batch)
    {
        batch.draw(texture, 0, 0, 0, 0, Castra.WORLD_WIDTH, Castra.WORLD_HEIGHT);
    }

    public void dispose()
    {
        texture.dispose();
    }
}