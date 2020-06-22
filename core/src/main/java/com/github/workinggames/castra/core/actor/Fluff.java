package com.github.workinggames.castra.core.actor;

import lombok.Getter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class Fluff extends Actor
{
    @Getter
    private final ArrayMap<String, TextureRegion> textures = new ArrayMap<>();

    @Getter
    private final ArrayMap<String, Array<Vector2>> childrenMap = new ArrayMap<>();

    public Fluff(TextureAtlas textureAtlas)
    {
        textures.put("grass1", textureAtlas.findRegion("grass1"));
        textures.put("grass2", textureAtlas.findRegion("grass2"));
        textures.put("grass3", textureAtlas.findRegion("grass3"));
        textures.put("Tree3", textureAtlas.findRegion("Tree3"));
        textures.put("Tree4", textureAtlas.findRegion("Tree4"));
        textures.put("Stone1", textureAtlas.findRegion("Stone1"));
        textures.put("Stone2", textureAtlas.findRegion("Stone2"));
        textures.put("Stone3", textureAtlas.findRegion("Stone3"));
        textures.put("Stone4", textureAtlas.findRegion("Stone4"));
        textures.put("Stone5", textureAtlas.findRegion("Stone5"));
        textures.put("Stone6", textureAtlas.findRegion("Stone6"));
        textures.put("Stone7", textureAtlas.findRegion("Stone7"));

        childrenMap.put("grass1", new Array<>());
        childrenMap.put("grass2", new Array<>());
        childrenMap.put("grass3", new Array<>());
        childrenMap.put("Tree3", new Array<>());
        childrenMap.put("Tree4", new Array<>());
        childrenMap.put("Stone1", new Array<>());
        childrenMap.put("Stone2", new Array<>());
        childrenMap.put("Stone3", new Array<>());
        childrenMap.put("Stone4", new Array<>());
        childrenMap.put("Stone5", new Array<>());
        childrenMap.put("Stone6", new Array<>());
        childrenMap.put("Stone7", new Array<>());
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        for (String key : childrenMap.keys().toArray())
        {
            for (Vector2 position : childrenMap.get(key))
            {
                batch.draw(textures.get(key), position.x, position.y);
            }
            batch.flush();
        }
    }
}