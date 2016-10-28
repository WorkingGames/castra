package de.incub8.castra.core.input;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import de.incub8.castra.core.actor.ArmySplit;

public class ArmySplitDragTarget extends DragAndDrop.Target
{
    private static final float DEGREES_FOR_STEP = 4.5f;
    private static final float STEP_SIZE = 5;
    private final ArmySplit armySplit;

    public ArmySplitDragTarget(ArmySplit armySplit)
    {
        super(armySplit.getOuterRimGroup());
        this.armySplit = armySplit;
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
    }

    private int calculateArmySplitPercentage(float x, float y)
    {
        float angle = new Vector2(x, y).angle();
        int percentage = (int) (Math.round(angle / DEGREES_FOR_STEP) * STEP_SIZE);
        return percentage;
    }

    @Override
    public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload)
    {
        armySplit.hideOuterRimGroup();
        super.reset(source, payload);
    }
}