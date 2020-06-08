package com.github.workinggames.castra.android;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.github.workinggames.castra.core.Castra;

public class AndroidLauncher extends AndroidApplication
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
        config.useImmersiveMode = true;
        initialize(new Castra(new AndroidTimestampFormatter()), config);
    }
}