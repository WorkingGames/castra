package de.incub8.castra.core.font;

import lombok.Getter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Disposable;

public class FontProvider implements Disposable
{
    @Getter
    private BitmapFont defaultFont;

    @Getter
    private BitmapFont soldierCountFont;

    @Getter
    private BitmapFont splitInfoFont;

    public FontProvider()
    {
        defaultFont = new BitmapFont();
        soldierCountFont = new BitmapFont(
            Gdx.files.internal("fonts/SoldierCount.fnt"), Gdx.files.internal("fonts/SoldierCount.png"), false);
        splitInfoFont = new BitmapFont(
            Gdx.files.internal("fonts/SplitInfoText.fnt"), Gdx.files.internal("fonts/SplitInfoText.png"), false);
    }

    @Override
    public void dispose()
    {
        defaultFont.dispose();
        soldierCountFont.dispose();
    }
}