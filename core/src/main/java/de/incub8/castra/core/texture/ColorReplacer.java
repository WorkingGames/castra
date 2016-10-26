package de.incub8.castra.core.texture;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.utils.ObjectMap;

class ColorReplacer
{
    public Texture replaceColors(Texture texture, ObjectMap<Color, Color> mappings)
    {
        Pixmap pixmap = getPixmap(texture);
        replaceColors(pixmap, mappings);
        return convertToTextureAndDispose(pixmap);
    }

    private Pixmap getPixmap(Texture texture)
    {
        TextureData textureData = texture.getTextureData();
        textureData.prepare();
        return textureData.consumePixmap();
    }

    private void replaceColors(Pixmap pixmap, ObjectMap<Color, Color> mappings)
    {
        for (int x = 0; x < pixmap.getWidth(); x++)
        {
            for (int y = 0; y < pixmap.getHeight(); y++)
            {
                Color pixelColor = new Color(pixmap.getPixel(x, y));
                if (mappings.containsKey(pixelColor))
                {
                    pixmap.setColor(mappings.get(pixelColor));
                    pixmap.drawPixel(x, y);
                }
            }
        }
    }

    private Texture convertToTextureAndDispose(Pixmap pixmap)
    {
        Texture result = new Texture(pixmap);
        pixmap.dispose();
        return result;
    }
}