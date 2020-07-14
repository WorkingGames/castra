package com.github.workinggames.castra.core.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.audio.AudioManager;
import com.github.workinggames.castra.core.stage.World;

class SettlementDragTarget extends DragAndDrop.Target
{
    private final World world;
    private final AudioManager audioManager;

    public SettlementDragTarget(
        Settlement settlement, World world, AudioManager audioManager)
    {
        super(settlement);
        this.world = world;
        this.audioManager = audioManager;
    }

    @Override
    public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer)
    {
        Settlement origin = (Settlement) source.getActor();
        Settlement destination = (Settlement) getActor();
        boolean valid = origin != destination;
        if (valid)
        {
            destination.setHighlight(true);
        }
        return valid;
    }

    @Override
    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer)
    {
        Settlement origin = (Settlement) source.getActor();
        Settlement destination = (Settlement) getActor();
        Gdx.input.vibrate(50);
        audioManager.playClickSound();
        world.createArmy(origin, destination);
    }

    @Override
    public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload)
    {
        Settlement origin = (Settlement) source.getActor();
        Settlement destination = (Settlement) getActor();
        if (origin != destination)
        {
            destination.setHighlight(false);
        }
        super.reset(source, payload);
    }
}