package de.incub8.castra.core.model;

import lombok.Data;

import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.GridPoint2;

@Data
public class Settlement
{
    private final SettlementSize size;
    private final Ellipse hitbox;
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
        this.textureDefinition = textureDefinition;
    }

    public void addSoldier()
    {
        soldiers++;
    }
}