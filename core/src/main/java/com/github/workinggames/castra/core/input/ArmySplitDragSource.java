package com.github.workinggames.castra.core.input;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.github.workinggames.castra.core.actor.ArmySplit;

public class ArmySplitDragSource extends DragAndDrop.Source
{
    private final ArmySplit armySplit;

    public ArmySplitDragSource(ArmySplit armySplit)
    {
        super(armySplit.getInnerRimGroup());
        this.armySplit = armySplit;
    }

    @Override
    public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer)
    {
        armySplit.showOuterRimGroup();
        return new DragAndDrop.Payload();
    }
}