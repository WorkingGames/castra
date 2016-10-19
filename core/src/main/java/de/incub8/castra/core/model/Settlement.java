package de.incub8.castra.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Shape2D;

@Data
@EqualsAndHashCode(of = { "position" })
public class Settlement
{
    private final SettlementSize size;
    private final Ellipse hitbox;
    private final Shape2D clickBox;
    private final GridPoint2 position;
    private final TextureDefinition textureDefinition;

    private int soldiers;
    private Player owner;

    public Settlement(
        SettlementSize size, GridPoint2 position, int soldiers, Player owner, TextureDefinition textureDefinition)
    {
        this.size = size;
        this.soldiers = soldiers;
        this.owner = owner;
        this.position = position;
        this.hitbox = new Ellipse(position.x, position.y, size.getWidth(), size.getHeight());
        // we use the hitbox for now until we have the textures and can provide a better approximation
        this.clickBox = this.hitbox;
        this.textureDefinition = textureDefinition;
    }

    public void addSoldier()
    {
        soldiers++;
    }

    public void removeSoldier()
    {
        soldiers--;
    }

    public void removeSoldiers(int count)
    {
        soldiers = soldiers - count;
    }

    public boolean isEmpty()
    {
        return soldiers == 0;
    }
}