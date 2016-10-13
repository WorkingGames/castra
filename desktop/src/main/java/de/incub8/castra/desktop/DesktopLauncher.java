package de.incub8.castra.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.incub8.castra.core.Castra;

public class DesktopLauncher
{
    public static void main(String[] args)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Charge";
        config.width = 1366;
        config.height = 768;
        new LwjglApplication(new Castra(), config);
    }
}
