package de.incub8.castra.core.renderer;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import de.incub8.castra.core.model.Army;
import de.incub8.castra.core.model.TextureDefinition;

@RequiredArgsConstructor
public class ArmyRenderable extends AbstractRenderable
{
    private final Army army;

    @Override
    public void render(SpriteBatch batch, BitmapFont font)
    {
        TextureDefinition textureDefinition = army.getTextureDefinition();
        GridPoint2 center = army.getPosition();
        Vector2 offset = textureDefinition.getOffset();
        Texture texture = textureDefinition.getTexture();

        float xRenderPosition = center.x - offset.x;
        float yRenderPosition = center.y - offset.y;
        batch.draw(texture, xRenderPosition, yRenderPosition, texture.getWidth(), texture.getHeight());

        font.draw(batch, String.valueOf(army.getSoldiers()), center.x, center.y);
    }

    @Override
    protected int getX()
    {
        return army.getPosition().x;
    }

    @Override
    protected int getY()
    {
        return army.getPosition().y;
    }
}