package com.github.workinggames.castra.core.texture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.github.workinggames.castra.core.model.PlayerColor;
import com.github.workinggames.castra.core.model.PlayerColorSchema;

public class ColorizingTextureAtlasAdapter
{
    private final TextureAtlas textureAtlas;
    private final ColorReplacer colorReplacer;

    public ColorizingTextureAtlasAdapter(TextureAtlas textureAtlas)
    {
        this.textureAtlas = textureAtlas;
        colorReplacer = new ColorReplacer();
    }

    public TextureAtlas.AtlasRegion findRegion(String name, PlayerColor playerColor)
    {
        String coloredRegionName = name + playerColor.getRegionNameSuffix();
        TextureAtlas.AtlasRegion result = textureAtlas.findRegion(coloredRegionName);
        if (result == null)
        {
            addColoredRegion(coloredRegionName, name, playerColor);
            result = textureAtlas.findRegion(coloredRegionName);
        }
        return result;
    }

    public TextureAtlas.AtlasRegion findRegion(String name, PlayerColorSchema playerColorSchema)
    {
        PlayerColor playerColor = playerColorSchema.getPlayerColor();
        return findRegion(name, playerColor);
    }

    private void addColoredRegion(String coloredRegionName, String originalRegionName, PlayerColor playerColor)
    {
        TextureAtlas.AtlasRegion originalRegion = textureAtlas.findRegion(originalRegionName);
        Texture originalTexture = originalRegion.getTexture();
        Texture coloredTexture = colorReplacer.replaceColors(originalTexture, playerColor.getColorMapping());
        textureAtlas.addRegion(coloredRegionName,
            coloredTexture,
            0,
            0,
            coloredTexture.getWidth(),
            coloredTexture.getHeight());
    }
}