package de.incub8.castra.core.model;

import lombok.Data;

import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

@Data
public class Settlement
{
    private final SettlementSize size;
    private final Ellipse hitbox;
    private final ObjectMap<Settlement, Array<GridPoint2>> paths;
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
        Vector2 offset = new Vector2(size.getWidth() / 2, size.getHeight() / 2);
        Vector2 positionVector = new Vector2(position.x, position.y);
        this.hitbox = new Ellipse(positionVector.sub(offset), size.getWidth(), size.getHeight());
        this.paths = new ObjectMap<>();
        this.textureDefinition = textureDefinition;
    }
}