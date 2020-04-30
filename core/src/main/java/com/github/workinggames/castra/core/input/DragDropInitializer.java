package com.github.workinggames.castra.core.input;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.stage.World;

public class DragDropInitializer
{
    public void initialize(World world)
    {
        DragAndDrop dragAndDrop = new DragAndDrop();

        for (Settlement settlement : world.getSettlements())
        {
            dragAndDrop.addSource(new SettlementDragSource(settlement));
            dragAndDrop.addTarget(new SettlementDragTarget(settlement, world));
        }
    }
}