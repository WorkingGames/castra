package de.incub8.castra.core;

import lombok.Getter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import de.incub8.castra.core.screen.MainMenuScreen;

public class Castra extends Game
{
    @Getter
    private OrthographicCamera camera;

    @Override
    public void create()
    {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1366, 768);

        this.setScreen(new MainMenuScreen(this));
    }

    public void render()
    {
        super.render();
    }
}