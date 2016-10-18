package de.incub8.castra.core.renderer;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import de.incub8.castra.core.model.PlayerType;
import de.incub8.castra.core.model.Settlement;

@RequiredArgsConstructor
public class SettlementRenderable extends AbstractRenderable
{
    private final Settlement settlement;

    @Override
    public void render(SpriteBatch batch, BitmapFont font)
    {
        TextureDefinition textureDefinition = settlement.getTextureDefinition();
        GridPoint2 center = settlement.getPosition();
        GridPoint2 offset = textureDefinition.getOffset();
        Texture texture = textureDefinition.getTexture();

        float xRenderPosition = center.x - offset.x;
        float yRenderPosition = center.y - offset.y;
        batch.draw(texture, xRenderPosition, yRenderPosition, texture.getWidth(), texture.getHeight());

        // AI settlements have hidden soldier sizes
        if (!settlement.getOwner().getType().equals(PlayerType.AI))
        {
            font.draw(
                batch, "" + (settlement.getSoldiers()), center.x, center.y);
        }
    }

    @Override
    protected int getX()
    {
        return settlement.getPosition().x;
    }

    @Override
    protected int getY()
    {
        return settlement.getPosition().y;
    }
}