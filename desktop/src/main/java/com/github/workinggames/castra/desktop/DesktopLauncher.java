package com.github.workinggames.castra.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.workinggames.castra.core.Castra;

public class DesktopLauncher
{
    public static void main(String[] args)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Charge";
        config.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());
        Castra game = new Castra(new DesktopTimestampFormatter());
        String debug = System.getProperty("debug", "false");
        game.getGameConfiguration().setDebug(Boolean.parseBoolean(debug));
        new LwjglApplication(game, config);
    }
}