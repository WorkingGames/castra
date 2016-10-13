package de.incub8.castra.html;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import de.incub8.castra.core.Castra;

public class HtmlLauncher extends GwtApplication
{
    @Override
    public GwtApplicationConfiguration getConfig()
    {
        return new GwtApplicationConfiguration(1366, 768);
    }

    @Override
    public ApplicationListener createApplicationListener()
    {
        return new Castra();
    }
}