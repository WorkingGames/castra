package com.github.workinggames.castra.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.workinggames.castra.core.font.FontProvider;
import com.github.workinggames.castra.core.screen.GameState;
import com.github.workinggames.castra.core.screen.MainMenuScreen;
import com.github.workinggames.castra.core.stage.GameConfiguration;
import com.github.workinggames.castra.core.statistics.StatisticsEventCreator;
import com.github.workinggames.castra.core.statistics.TimestampFormatter;
import com.github.workinggames.castra.core.task.VortexEventSender;
import com.github.workinggames.castra.core.texture.TextureAtlasInitializer;

@RequiredArgsConstructor
public class Castra extends Game
{
    @Getter
    private final TextureAtlas textureAtlas = new TextureAtlas();

    @Getter
    private final Skin skin = new Skin();

    @Getter
    private final GameConfiguration gameConfiguration = new GameConfiguration();

    @Getter
    private FontProvider fontProvider;

    @Getter
    private Viewport viewport;

    @Getter
    private InputMultiplexer inputMultiplexer;

    @Getter
    private StatisticsEventCreator statisticsEventCreator;

    private final TimestampFormatter timestampFormatter;

    @Override
    public void pause()
    {
        super.pause();
        gameState = GameState.PAUSED;
    }

    @Override
    public void resume()
    {
        super.resume();
        gameState = GameState.RUNNING;
    }

    @Getter
    @Setter
    private GameState gameState = GameState.RUNNING;

    @Override
    public void create()
    {
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);

        new TextureAtlasInitializer().initializeAtlasContent(textureAtlas);

        textureAtlas.findRegion("Background256")
            .getTexture()
            .setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        fontProvider = new FontProvider();

        statisticsEventCreator = new StatisticsEventCreator(new VortexEventSender(), timestampFormatter);

        this.setScreen(new MainMenuScreen(this));
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
    }
}