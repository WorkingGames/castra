package com.github.workinggames.castra.core.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.audio.AudioManager;

class SettlementDragSource extends DragAndDrop.Source
{
    private final AudioManager audioManager;

    public SettlementDragSource(
        Settlement settlement, AudioManager audioManager)
    {
        super(settlement);
        this.audioManager = audioManager;
    }

    @Override
    public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer)
    {
        Settlement settlement = (Settlement) getActor();
        DragAndDrop.Payload result = null;
        if (settlement.getOwner().isHuman())
        {
            settlement.setHighlight(true);
            result = new DragAndDrop.Payload();
            Gdx.input.vibrate(50);
            audioManager.playClickSound();
        }
        return result;
    }

    @Override
    public void dragStop(
        InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target)
    {
        Settlement settlement = (Settlement) getActor();
        settlement.setHighlight(false);
        super.dragStop(event, x, y, pointer, payload, target);
    }
}