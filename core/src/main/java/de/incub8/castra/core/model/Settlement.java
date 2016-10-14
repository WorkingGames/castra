package de.incub8.castra.core.model;

import lombok.Data;

import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

@Data
public class Settlement
{
    private final SettlementSize size;
    private final Ellipse hitbox;
    private final ObjectMap<Settlement, Array<Vector2>> paths;

    private int soldiers;
    private Player owner;

    public Settlement(SettlementSize size, Vector2 position, int soldiers, Player owner)
    {
        this.size = size;
        this.soldiers = soldiers;
        this.owner = owner;
        Vector2 offset = new Vector2(size.getWidth() / 2, size.getHeight() / 2);
        this.hitbox = new Ellipse(position.sub(offset), size.getWidth(), size.getHeight());
        this.paths = new ObjectMap<>();
    }
}