package de.incub8.castra.core.model;

import lombok.Data;

import com.badlogic.gdx.utils.ObjectMap;
import de.incub8.castra.core.actor.Settlement;
import de.incub8.castra.core.pathfinding.LinePath;

public class Paths
{
    @Data
    private static class Key
    {
        private final Settlement origin;
        private final Settlement destination;
    }

    private final ObjectMap<Key, LinePath> paths = new ObjectMap<>();

    public void put(Settlement origin, Settlement destination, LinePath path)
    {
        paths.put(new Key(origin, destination), path);
    }

    public LinePath get(Settlement origin, Settlement destination)
    {
        return paths.get(new Key(origin, destination));
    }
}