package com.github.workinggames.castra.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.workinggames.castra.core.audio.AudioManager;
import com.github.workinggames.castra.core.font.FontProvider;
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

    private final TimestampFormatter timestampFormatter;

    @Getter
    private AudioManager audioManager;

    @Getter
    private FontProvider fontProvider;

    @Getter
    private Viewport viewport;

    @Getter
    private InputMultiplexer inputMultiplexer;

    @Getter
    private StatisticsEventCreator statisticsEventCreator;

    @Getter
    private boolean humbleAssetsPresent;

    @Override
    public void pause()
    {
        super.pause();
    }

    @Override
    public void resume()
    {
        super.resume();
    }

    @Override
    public void create()
    {
        viewport = new FitViewport(1920, 1080);
        humbleAssetsPresent = tryIfHumbleAssetsPresent();

        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);

        // disabling the android back button, to handle it on our own
        Gdx.input.setCatchKey(Input.Keys.BACK, true);

        if (humbleAssetsPresent)
        {
            Pixmap cursorPixmap = new Pixmap(Gdx.files.internal("humble-assets/Cursor.png"));
            Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursorPixmap, 6, 5));
            cursorPixmap.dispose();
        }

        new TextureAtlasInitializer(textureAtlas).initializeAtlasContent(humbleAssetsPresent);

        fontProvider = new FontProvider(humbleAssetsPresent);

        statisticsEventCreator = new StatisticsEventCreator(new VortexEventSender(), timestampFormatter);

        audioManager = new AudioManager(humbleAssetsPresent);
        audioManager.initializeSounds();

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

    public boolean tryIfHumbleAssetsPresent()
    {
        return Gdx.files.internal("humble-assets/cursor.png").exists();
    }
}