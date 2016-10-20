package de.incub8.castra.core.renderer;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import de.incub8.castra.core.model.PlayerType;
import de.incub8.castra.core.model.Settlement;
import de.incub8.castra.core.model.TextureDefinition;

@RequiredArgsConstructor
public class SettlementRenderable extends AbstractRenderable
{
    private final Settlement settlement;

    @Override
    public void render(SpriteBatch batch, BitmapFont font)
    {
        TextureDefinition textureDefinition = settlement.getTextureDefinition();
        GridPoint2 center = settlement.getPosition();
        Vector2 offset = textureDefinition.getOffset();
        Texture texture = textureDefinition.getTexture();

        float xRenderPosition = center.x - offset.x;
        float yRenderPosition = center.y - offset.y;
        Color originalColor = batch.getColor();
        batch.setColor(settlement.getOwner().getColor());
        batch.draw(texture, xRenderPosition, yRenderPosition, texture.getWidth(), texture.getHeight());
        batch.setColor(originalColor);
        // AI settlements have hidden soldier sizes
        if (!settlement.getOwner().getType().equals(PlayerType.AI))
        {
            font.draw(batch, String.valueOf(settlement.getSoldiers()), center.x, center.y);
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