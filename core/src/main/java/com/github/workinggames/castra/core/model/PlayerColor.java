package com.github.workinggames.castra.core.model;

import lombok.Data;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ObjectMap;

@Data
public class PlayerColor
{
    private static final Color REPLACEMENT_COLOR = Color.valueOf("ff00ffff");

    private final ObjectMap<Color, Color> colorMapping;
    private final String regionNameSuffix;

    public PlayerColor(Color color)
    {
        colorMapping = createColorMapping(color);
        regionNameSuffix = createRegionNameSuffix(color);
    }

    public Color getPrimaryColor()
    {
        return colorMapping.get(REPLACEMENT_COLOR);
    }

    private ObjectMap<Color, Color> createColorMapping(Color color)
    {
        ObjectMap<Color, Color> result = new ObjectMap<>();
        result.put(REPLACEMENT_COLOR, color);
        return result;
    }

    private String createRegionNameSuffix(Color color)
    {
        return color.toString();
    }
}