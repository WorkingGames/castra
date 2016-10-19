package de.incub8.castra.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

@ToString
@EqualsAndHashCode
public class Paths
{
    @Data
    private static class Key
    {
        private final Settlement origin;
        private final Settlement destination;
    }

    private final ObjectMap<Key, Array<GridPoint2>> paths = new ObjectMap<>();

    public void put(Settlement origin, Settlement destination, Array<GridPoint2> path)
    {
        paths.put(new Key(origin, destination), path);
    }

    public Array<GridPoint2> get(Settlement origin, Settlement destination)
    {
        return paths.get(new Key(origin, destination));
    }
}