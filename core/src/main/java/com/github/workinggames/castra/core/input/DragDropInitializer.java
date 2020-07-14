package com.github.workinggames.castra.core.input;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.audio.AudioManager;
import com.github.workinggames.castra.core.stage.World;
import com.github.workinggames.castra.core.task.AsyncInitializer;

@RequiredArgsConstructor
public class DragDropInitializer implements AsyncInitializer
{
    private final World world;
    private final AudioManager audioManager;

    private boolean finished;

    public void initialize()
    {
        DragAndDrop dragAndDrop = new DragAndDrop();

        for (Settlement settlement : world.getSettlements())
        {
            dragAndDrop.addSource(new SettlementDragSource(settlement, audioManager));
            dragAndDrop.addTarget(new SettlementDragTarget(settlement, world, audioManager));
        }

        finished = true;
    }

    @Override
    public boolean isFinished()
    {
        return finished;
    }
}