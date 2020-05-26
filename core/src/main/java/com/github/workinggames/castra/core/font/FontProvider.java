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
    private final BitmapFont menuLarge;

    @Getter
    private final BitmapFont menuMedium;

    @Getter
    private final BitmapFont menuSmall;

    @Getter
    private final BitmapFont title;

    public FontProvider()
    {
        defaultFont = new BitmapFont();
        soldierCount = new BitmapFont(Gdx.files.internal("fonts/SoldierCount.fnt"),
            Gdx.files.internal("fonts/SoldierCount.png"),
            false);
        splitInfo = new BitmapFont(Gdx.files.internal("fonts/SplitInfoText.fnt"),
            Gdx.files.internal("fonts/SplitInfoText.png"),
            false);
        menuLarge = new BitmapFont(Gdx.files.internal("fonts/MenuLarge.fnt"),
            Gdx.files.internal("fonts/MenuLarge.png"),
            false);
        menuMedium = new BitmapFont(Gdx.files.internal("fonts/MenuMedium.fnt"),
            Gdx.files.internal("fonts/MenuMedium.png"),
            false);
        menuSmall = new BitmapFont(Gdx.files.internal("fonts/MenuSmall.fnt"),
            Gdx.files.internal("fonts/MenuSmall.png"),
            false);
        title = new BitmapFont(Gdx.files.internal("fonts/Title.fnt"), Gdx.files.internal("fonts/Title.png"), false);
    }

    @Override
    public void dispose()
    {
        defaultFont.dispose();
        soldierCount.dispose();
    }
}