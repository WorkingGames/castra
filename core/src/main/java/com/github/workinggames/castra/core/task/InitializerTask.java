package com.github.workinggames.castra.core.task;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.utils.Timer;

@RequiredArgsConstructor
public class InitializerTask extends Timer.Task
{
    private final AsyncInitializer asyncInitializer;

    @Override
    public void run()
    {
        asyncInitializer.initialize();
    }
}