package com.github.workinggames.castra.core.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.github.workinggames.castra.core.actor.ArmySplit;
import com.github.workinggames.castra.core.audio.AudioManager;

public class ArmySplitDragSource extends DragAndDrop.Source
{
    private final ArmySplit armySplit;
    private final AudioManager audioManager;

    public ArmySplitDragSource(ArmySplit armySplit, AudioManager audioManager)
    {
        super(armySplit.getInnerRimGroup());
        this.armySplit = armySplit;
        this.audioManager = audioManager;
    }

    @Override
    public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer)
    {
        armySplit.showOuterRimGroup();
        Gdx.input.vibrate(50);
        audioManager.playClickSound();
        return new DragAndDrop.Payload();
    }
}