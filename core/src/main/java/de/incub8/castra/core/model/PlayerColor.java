package de.incub8.castra.core.model;

import lombok.Data;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ObjectMap;

@Data
public class PlayerColor
{
    private static final Color REPLACEMENT_COLOR1 = Color.valueOf("ff69b4ff");
    private static final Color REPLACEMENT_COLOR2 = Color.valueOf("ff69b4ff");

    private final ObjectMap<Color, Color> colorMapping;
    private final String regionNameSuffix;

    public PlayerColor(Color color1, Color color2)
    {
        colorMapping = createColorMapping(color1, color2);
        regionNameSuffix = createRegionNameSuffix(color1, color2);
    }

    private ObjectMap<Color, Color> createColorMapping(Color color1, Color color2)
    {
        ObjectMap<Color, Color> result = new ObjectMap<>();
        result.put(REPLACEMENT_COLOR1, color1);
        result.put(REPLACEMENT_COLOR2, color2);
        return result;
    }

    private String createRegionNameSuffix(Color color1, Color color2)
    {
        return color1.toString() + color2.toString();
    }
}