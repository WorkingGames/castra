package com.github.workinggames.castra.core.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.github.workinggames.castra.core.actor.ArmySplit;
import com.github.workinggames.castra.core.audio.AudioManager;

public class ArmySplitDragTarget extends DragAndDrop.Target
{
    private static final float DEGREES_FOR_STEP = 4.5f;
    private static final float STEP_SIZE = 5;

    private final ArmySplit armySplit;
    private final AudioManager audioManager;

    public ArmySplitDragTarget(ArmySplit armySplit, AudioManager audioManager)
    {
        super(armySplit.getOuterRimGroup());
        this.armySplit = armySplit;
        this.audioManager = audioManager;
    }

    @Override
    public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer)
    {
        int percentage = calculateArmySplitPercentage(x, y);
        armySplit.updatePercentage(percentage);
        return true;
    }

    @Override
    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer)
    {
        armySplit.hideOuterRimGroup();
        Gdx.input.vibrate(50);
        audioManager.playClickSound();
    }

    private int calculateArmySplitPercentage(float x, float y)
    {
        float angle = new Vector2(x, y).angle();
        return (int) (Math.round(angle / DEGREES_FOR_STEP) * STEP_SIZE);
    }

    @Override
    public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload)
    {
        armySplit.hideOuterRimGroup();
        super.reset(source, payload);
    }
}