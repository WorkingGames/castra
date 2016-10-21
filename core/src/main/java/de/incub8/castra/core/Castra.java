package de.incub8.castra.core;

import lombok.Getter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.incub8.castra.core.font.FontProvider;
import de.incub8.castra.core.screen.MainMenuScreen;

public class Castra extends Game
{
    public static final float WIDTH_HEIGHT_RATIO = 0.3f;

    private static final int WORLD_WIDTH = 1366;
    private static final int WORLD_HEIGHT = 768;

    @Getter
    private Viewport viewport;

    @Getter
    private InputMultiplexer inputMultiplexer;

    @Getter
    private TextureAtlas textureAtlas;

    @Getter
    private FontProvider fontProvider;

    @Override
    public void create()
    {
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);

        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);

        textureAtlas = initializeAtlas();
        textureAtlas.findRegion("Background128")
            .getTexture()
            .setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        fontProvider = new FontProvider();

        this.setScreen(new MainMenuScreen(this));
    }

    private TextureAtlas initializeAtlas()
    {
        /* TODO CST-51: read atlas from file (inline method) */
        TextureAtlas result = new TextureAtlas();
        addToAtlas("army", result);
        addToAtlas("Background128", result);
        addToAtlas("castleNeutralLarge", result);
        addToAtlas("castleNeutralMedium", result);
        addToAtlas("castleNeutralSmall", result);
        return result;
    }

    private void addToAtlas(String name, TextureAtlas textureAtlas)
    {
        Texture texture = new Texture(Gdx.files.internal(name + ".png"));
        textureAtlas.addRegion(name, texture, 0, 0, texture.getWidth(), texture.getHeight());
    }

    @Override
    public void resize(int width, int height)
    {
        super.resize(width, height);
        viewport.update(width, height);
        fontProvider.resize(width, height);
    }

    @Override
    public void dispose()
    {
        super.dispose();
        fontProvider.dispose();
        textureAtlas.dispose();
    }
}