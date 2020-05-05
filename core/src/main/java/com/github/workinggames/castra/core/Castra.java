package com.github.workinggames.castra.core;

import lombok.Getter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.workinggames.castra.core.font.FontProvider;
import com.github.workinggames.castra.core.screen.MainMenuScreen;
import com.github.workinggames.castra.core.worldbuilding.FluffLoader;
import com.kotcrab.vis.ui.VisUI;

public class Castra extends Game
{
    public static final float WIDTH_HEIGHT_RATIO = 0.6f;

    private static final int WORLD_WIDTH = 1366;
    private static final int WORLD_HEIGHT = 768;

    @Getter
    private final TextureAtlas textureAtlas = new TextureAtlas();

    @Getter
    private final Skin skin = new Skin();

    @Getter
    private FontProvider fontProvider;

    @Getter
    private Viewport viewport;

    @Getter
    private InputMultiplexer inputMultiplexer;

    @Override
    public void create()
    {
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);

        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);

        initializeAtlasContent();
        textureAtlas.findRegion("Background256")
            .getTexture()
            .setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        fontProvider = new FontProvider();
        VisUI.load();

        this.setScreen(new MainMenuScreen(this));
    }

    private void initializeAtlasContent()
    {
        addToAtlas("soldier");
        addToAtlas("soldiers");
        addToAtlas("horsesAndSoldiers");
        addToAtlas("Background256");
        addToAtlas("cloud");
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
        FluffLoader.addFluffToAtlas(textureAtlas);
    }

    private void addToAtlas(String name)
    {
        Texture texture = new Texture(Gdx.files.internal(name + ".png"));
        textureAtlas.addRegion(name, texture, 0, 0, texture.getWidth(), texture.getHeight());
    }

    @Override
    public void resize(int width, int height)
    {
        super.resize(width, height);
        viewport.update(width, height, true);
    }

    @Override
    public void dispose()
    {
        super.dispose();
        fontProvider.dispose();
        textureAtlas.dispose();
        skin.dispose();
        VisUI.dispose();
    }
}