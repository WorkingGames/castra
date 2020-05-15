package com.github.workinggames.castra.core.worldbuilding;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.workinggames.castra.core.input.DragDropInitializer;
import com.github.workinggames.castra.core.pathfinding.PathInitializer;
import com.github.workinggames.castra.core.stage.World;

@RequiredArgsConstructor
public class WorldInitializer
{
    private final Viewport viewport;
    private final TextureAtlas textureAtlas;

    public void initialize(World world)
    {
        new SettlementInitializer(world, viewport).initialize();
        new FluffInitializer(world, viewport).initialize();
        new PathInitializer(viewport, textureAtlas).initialize(world);
        new DragDropInitializer().initialize(world);
    }
}