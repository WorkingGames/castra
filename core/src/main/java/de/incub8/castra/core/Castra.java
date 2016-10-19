package de.incub8.castra.core;

import lombok.Getter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import de.incub8.castra.core.screen.MainMenuScreen;

public class Castra extends Game
{
    public static final int VIEWPORT_WIDTH = 1366;
    public static final int VIEWPORT_HEIGHT = 768;

    @Getter
    private OrthographicCamera camera;

    @Getter
    private InputMultiplexer inputMultiplexer;

    @Override
    public void create()
    {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);

        this.setScreen(new MainMenuScreen(this));
    }

    public void render()
    {
        super.render();
    }
}