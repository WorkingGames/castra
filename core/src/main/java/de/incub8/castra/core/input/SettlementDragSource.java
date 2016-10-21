package de.incub8.castra.core.input;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import de.incub8.castra.core.actor.Settlement;

class SettlementDragSource extends DragAndDrop.Source
{
    public SettlementDragSource(Settlement settlement)
    {
        super(settlement);
    }

    @Override
    public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer)
    {
        return new DragAndDrop.Payload();
    }
}