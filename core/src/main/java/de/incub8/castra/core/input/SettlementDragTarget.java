package de.incub8.castra.core.input;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import de.incub8.castra.core.actor.Settlement;
import de.incub8.castra.core.stage.World;

class SettlementDragTarget extends DragAndDrop.Target
{
    private final World world;

    public SettlementDragTarget(Settlement settlement, World world)
    {
        super(settlement);
        this.world = world;
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