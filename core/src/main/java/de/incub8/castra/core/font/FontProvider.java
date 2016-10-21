package de.incub8.castra.core.font;

import lombok.Getter;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Disposable;

public class FontProvider implements Disposable
{
    @Getter
    private BitmapFont font;

    public FontProvider()
    {
        font = new BitmapFont();
    }

    public void resize(int width, int height)
    {
        /*
         * TODO CST-35: use freetype fonts to resize font without it looking blurred. Remember to dispose the current
         * font beforehand
         */
    }

    @Override
    public void dispose()
    {
        font.dispose();
    }
}