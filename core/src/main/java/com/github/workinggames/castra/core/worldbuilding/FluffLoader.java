package com.github.workinggames.castra.core.worldbuilding;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class FluffLoader
{
    public static final List<String> ALL = Arrays.asList("Stone1",
        "Stone2",
        "Stone3",
        "Stone4",
        "Stone5",
        "Stone6",
        "Stone7",
        "Bush1",
        "Tree1",
        "Tree2",
        "Tree3",
        "Tree4",
        "BrokenColumn",
        "OldWagon",
        "NoSwordStone",
        "SwordStone");
    public static final List<String> STONES = Arrays.asList("Stone1",
        "Stone2",
        "Stone3",
        "Stone4",
        "Stone5",
        "Stone6",
        "Stone7");
    public static final List<String> TREES = Arrays.asList("Bush1", "Tree1", "Tree2", "Tree3", "Tree4");
    public static final List<String> OTHER = Arrays.asList("BrokenColumn", "OldWagon");

    public static void addFluffToAtlas(TextureAtlas textureAtlas)
    {
        for (String name : ALL)
        {
            addFluff(textureAtlas, name);
        }
    }

    private static void addFluff(TextureAtlas textureAtlas, String name)
    {
        Texture texture = new Texture(Gdx.files.internal("fluff/" + name + ".png"));
        textureAtlas.addRegion(name, texture, 0, 0, texture.getWidth(), texture.getHeight());
    }
}