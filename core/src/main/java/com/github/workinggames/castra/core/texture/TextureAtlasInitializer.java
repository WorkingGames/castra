package com.github.workinggames.castra.core.texture;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

@RequiredArgsConstructor
public class TextureAtlasInitializer
{
    private final TextureAtlas textureAtlas;

    public void initializeAtlasContent(boolean humbleAssetsPresent)
    {
        addToAtlas("LargeCastlePink");
        addToAtlas("LargeCastleHighlight");
        addToAtlas("LargeCastleNeutralHighlight");
        addToAtlas("LargeCastleFlags");
        addToAtlas("MediumCastlePink");
        addToAtlas("MediumCastleHighlight");
        addToAtlas("MediumCastleNeutralHighlight");
        addToAtlas("MediumCastleFlags");
        addToAtlas("SmallCastlePink");
        addToAtlas("SmallCastleHighlight");
        addToAtlas("SmallCastleNeutralHighlight");
        addToAtlas("SmallCastleFlags");
        addToAtlas("armySplit");
        addToAtlas("armySplitOuterRim");
        addToAtlas("uncheckedBox");
        addToAtlas("checkedBox");
        addToAtlas("Bricks");
        addToAtlas("sliderKnob");
        addToAtlas("battle1");
        addToAtlas("InstructionsScreenshot");
        addToAtlas("singleSoldier");
        addToAtlas("mediumSoldiers");
        addToAtlas("LargeSoldiers");
        addToAtlas("Background1");
        addToAtlas("SelectedShade");
        if (humbleAssetsPresent)
        {
            addHumbleAssets();
        }
        else
        {
            addToAtlas("MainMenu");
            addToAtlas("PlayerMenuBackground");
            addToAtlas("PlayerMenuBanners");
            addToAtlas("PlayerMenuBannersShade");
            addToAtlas("Button");
            addToAtlas("MouseOverButton");
            addToAtlas("ClickedButton");
            addToAtlas("AiActivated");
            addToAtlas("AiDeactivated");
            addToAtlas("AiTypeSelectBox");
            addToAtlas("HumanActivated");
            addToAtlas("HumanDeactivated");
            addToAtlas("InputBackground");
            addToAtlas("TypeButtonBackground");
            addToAtlas("TypeButtonMouseOver");
            addToAtlas("ColorSelectBoxBackground");
            addToAtlas("Torch");
            addToAtlas("Title");
            addToAtlas("GameOptions");
            addToAtlas("Parchment");
            addToAtlas("GameOverMenu");
            addToAtlas("GameOverBanners");
        }
    }

    private void addToAtlas(String name)
    {
        Texture texture = new Texture(Gdx.files.internal(name + ".png"));
        textureAtlas.addRegion(name, texture, 0, 0, texture.getWidth(), texture.getHeight());
    }

    private void addHumbleAssetToAtlas(String name)
    {
        Texture texture = new Texture(Gdx.files.internal("humble-assets/" + name + ".png"));
        textureAtlas.addRegion(name, texture, 0, 0, texture.getWidth(), texture.getHeight());
    }

    private void addHumbleAssets()
    {
        addHumbleAssetToAtlas("MainMenu");
        addHumbleAssetToAtlas("PlayerMenuBackground");
        addHumbleAssetToAtlas("PlayerMenuBanners");
        addHumbleAssetToAtlas("PlayerMenuBannersShade");
        addHumbleAssetToAtlas("Button");
        addHumbleAssetToAtlas("MouseOverButton");
        addHumbleAssetToAtlas("ClickedButton");
        addHumbleAssetToAtlas("AiActivated");
        addHumbleAssetToAtlas("AiDeactivated");
        addHumbleAssetToAtlas("AiTypeSelectBox");
        addHumbleAssetToAtlas("HumanActivated");
        addHumbleAssetToAtlas("HumanDeactivated");
        addHumbleAssetToAtlas("InputBackground");
        addHumbleAssetToAtlas("TypeButtonBackground");
        addHumbleAssetToAtlas("TypeButtonMouseOver");
        addHumbleAssetToAtlas("ColorSelectBoxBackground");
        addHumbleAssetToAtlas("Torch");
        addHumbleAssetToAtlas("Title");
        addHumbleAssetToAtlas("GameOptions");
        addHumbleAssetToAtlas("Parchment");
        addHumbleAssetToAtlas("GameOverMenu");
        addHumbleAssetToAtlas("GameOverBanners");
    }
}