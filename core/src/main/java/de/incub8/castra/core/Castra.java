package de.incub8.castra.core;

import lombok.Getter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.incub8.castra.core.screen.MainMenuScreen;

public class Castra extends Game
{
    public static final int WORLD_WIDTH = 1366;
    public static final int WORLD_HEIGHT = 768;

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

        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void resize(int width, int height)
    {
        super.resize(width, height);
        viewport.update(width, height);
    }
}