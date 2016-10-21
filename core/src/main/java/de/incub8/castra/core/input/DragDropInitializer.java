package de.incub8.castra.core.input;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import de.incub8.castra.core.actor.Settlement;
import de.incub8.castra.core.stage.World;

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