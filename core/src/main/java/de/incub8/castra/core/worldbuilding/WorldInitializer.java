package de.incub8.castra.core.worldbuilding;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.incub8.castra.core.input.DragDropInitializer;
import de.incub8.castra.core.pathfinding.PathInitializer;
import de.incub8.castra.core.stage.World;

@RequiredArgsConstructor
public class WorldInitializer
{
    private final Viewport viewport;
    private final TextureAtlas textureAtlas;
    private final long seed;

    public void initialize(World world)
    {
        new SettlementInitializer(world, viewport).initialize(seed);
        new PathInitializer(viewport, textureAtlas).initialize(world);
        new DragDropInitializer().initialize(world);
    }
}