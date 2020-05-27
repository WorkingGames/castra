package com.github.workinggames.castra.core.screen;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoadingState
{
    private static LoadingState instance;

    private boolean settlementsInitialized;
    private boolean fluffInitialized;
    private boolean pathInitialized;
    private boolean dragDropInitialized;
    private boolean aiInitialized;

    private LoadingState()
    {
    }

    public static synchronized LoadingState getInstance()
    {
        if (LoadingState.instance == null)
        {
            LoadingState.instance = new LoadingState();
        }
        return LoadingState.instance;
    }

    public static synchronized void reset()
    {
        LoadingState loadingState = getInstance();
        loadingState.setSettlementsInitialized(false);
        loadingState.setFluffInitialized(false);
        loadingState.setPathInitialized(false);
        loadingState.setDragDropInitialized(false);
        loadingState.setAiInitialized(false);
    }
}