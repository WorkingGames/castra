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
        Pixmap.Blending originalBlending = Pixmap.getBlending();
        Pixmap.setBlending(Pixmap.Blending.None);
        replaceColors(pixmap, mappings);
        Pixmap.setBlending(originalBlending);
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
                Color pixelColor = getColor(x, y, pixmap);
                Color opaqueColor = makeOpaque(pixelColor);
                if (mappings.containsKey(opaqueColor))
                {
                    Color newOpaqueColor = mappings.get(opaqueColor);
                    Color newColor = addTransparency(newOpaqueColor, pixelColor.a);
                    draw(x, y, newColor, pixmap);
                }
            }
        }
    }

    private Color getColor(int x, int y, Pixmap pixmap)
    {
        return new Color(pixmap.getPixel(x, y));
    }

    private Color makeOpaque(Color pixelColor)
    {
        return addTransparency(pixelColor, 1.0f);
    }

    private Color addTransparency(Color color, float alpha)
    {
        Color result = new Color(color);
        result.a = alpha;
        return result;
    }

    private void draw(int x, int y, Color color, Pixmap pixmap)
    {
        pixmap.setColor(color);
        pixmap.drawPixel(x, y);
    }

    private Texture convertToTextureAndDispose(Pixmap pixmap)
    {
        Texture result = new Texture(pixmap);
        pixmap.dispose();
        return result;
    }
}