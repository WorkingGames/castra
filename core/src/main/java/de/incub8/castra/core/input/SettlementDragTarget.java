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

        return origin != destination;
    }

    @Override
    public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer)
    {
        Settlement origin = (Settlement) source.getActor();
        Settlement destination = (Settlement) getActor();

        world.createArmy(origin, destination);
    }
}