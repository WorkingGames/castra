package com.github.workinggames.castra.core.texture;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class TextureAtlasInitializer
{
    public static final List<String> FLUFF_ALL = Arrays.asList("Stone1",
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
    public static final List<String> FLUFF_STONES = Arrays.asList("Stone1",
        "Stone2",
        "Stone3",
        "Stone4",
        "Stone5",
        "Stone6",
        "Stone7");
    public static final List<String> FLUFF_TREES = Arrays.asList("Bush1", "Tree1", "Tree2", "Tree3", "Tree4");
    public static final List<String> FLUFF_OTHER = Arrays.asList("BrokenColumn", "OldWagon");

    public void initializeAtlasContent(TextureAtlas textureAtlas)
    {
        addToAtlas("soldier", textureAtlas);
        addToAtlas("soldiers", textureAtlas);
        addToAtlas("horsesAndSoldiers", textureAtlas);
        addToAtlas("Background256", textureAtlas);
        addToAtlas("cloud", textureAtlas);
        addToAtlas("LargeCastlePink", textureAtlas);
        addToAtlas("LargeCastleHighlight", textureAtlas);
        addToAtlas("LargeCastleNeutralHighlight", textureAtlas);
        addToAtlas("LargeCastleFlags", textureAtlas);
        addToAtlas("MediumCastlePink", textureAtlas);
        addToAtlas("MediumCastleHighlight", textureAtlas);
        addToAtlas("MediumCastleNeutralHighlight", textureAtlas);
        addToAtlas("MediumCastleFlags", textureAtlas);
        addToAtlas("SmallCastlePink", textureAtlas);
        addToAtlas("SmallCastleHighlight", textureAtlas);
        addToAtlas("SmallCastleNeutralHighlight", textureAtlas);
        addToAtlas("SmallCastleFlags", textureAtlas);
        addToAtlas("armySplit", textureAtlas);
        addToAtlas("armySplitOuterRim", textureAtlas);
        addToAtlas("uncheckedBox", textureAtlas);
        addToAtlas("checkedBox", textureAtlas);
        addToAtlas("Bricks", textureAtlas);
        addToAtlas("sliderKnob", textureAtlas);
        addToAtlas("battle1", textureAtlas);
        for (String name : FLUFF_ALL)
        {
            addToAtlas(name, textureAtlas);
        }
    }

    private void addToAtlas(String name, TextureAtlas textureAtlas)
    {
        Texture texture = new Texture(getFile(name));
        textureAtlas.addRegion(name, texture, 0, 0, texture.getWidth(), texture.getHeight());
    }

    // visible for testing
    public FileHandle getFile(String name)
    {
        return Gdx.files.internal(name + ".png");
    }
}