package de.incub8.castra.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

@Data
@AllArgsConstructor
public class Settlement
{
    private final SettlementSize size;
    private final Ellipse hitbox;
    private final ObjectMap<Settlement, Array<Vector2>> paths;

    private int soldiers;
    private Player owner;
}