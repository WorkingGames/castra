package com.github.workinggames.castra.core.font;

import lombok.Getter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Disposable;

public class FontProvider implements Disposable
{
    @Getter
    private final BitmapFont defaultFont;

    @Getter
    private final BitmapFont soldierCount;

    @Getter
    private final BitmapFont splitInfo;

    @Getter
    private final BitmapFont vinque;

    @Getter
    private final BitmapFont vinqueLarge;

    public FontProvider(boolean humbleAssetsPresent)
    {
        defaultFont = new BitmapFont();
        soldierCount = new BitmapFont(Gdx.files.internal("fonts/SoldierCount.fnt"),
            Gdx.files.internal("fonts/SoldierCount.png"),
            false);
        splitInfo = new BitmapFont(Gdx.files.internal("fonts/SplitInfoText.fnt"),
            Gdx.files.internal("fonts/SplitInfoText.png"),
            false);
        if (humbleAssetsPresent)
        {
            vinque = new BitmapFont(Gdx.files.internal("humble-assets/fonts/Vinque30.fnt"),
                Gdx.files.internal("humble-assets/fonts/Vinque30.png"),
                false);
            vinqueLarge = new BitmapFont(Gdx.files.internal("humble-assets/fonts/Vinque34.fnt"),
                Gdx.files.internal("humble-assets/fonts/Vinque34.png"),
                false);
        }
        else
        {
            vinque = soldierCount;
            vinqueLarge = soldierCount;
        }
    }

    @Override
    public void dispose()
    {
        defaultFont.dispose();
        soldierCount.dispose();
        splitInfo.dispose();
        vinque.dispose();
        vinqueLarge.dispose();
    }
}