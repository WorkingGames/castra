package de.incub8.castra.core.input;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import de.incub8.castra.core.model.PlayerType;
import de.incub8.castra.core.model.Settlement;
import de.incub8.castra.core.model.World;

@RequiredArgsConstructor
public class MouseInputAdapter extends InputAdapter
{
    private final World world;
    private final Camera camera;

    private Settlement origin;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        Vector3 touchPoint = getTouchPoint(screenX, screenY);
        for (Settlement settlement : world.getSettlements())
        {
            if (settlementClicked(settlement, touchPoint) && belongsToHuman(settlement))
            {
                origin = settlement;
            }
        }
        return true;
    }

    private Vector3 getTouchPoint(int screenX, int screenY)
    {
        return camera.unproject(new Vector3(screenX, screenY, 0));
    }

    private boolean settlementClicked(Settlement settlement, Vector3 touchPoint)
    {
        return settlement.getClickBox().contains(touchPoint.x, touchPoint.y);
    }

    private boolean belongsToHuman(Settlement settlement)
    {
        return settlement.getOwner().getType().equals(PlayerType.HUMAN);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        Vector3 touchPoint = getTouchPoint(screenX, screenY);
        for (Settlement destination : world.getSettlements())
        {
            if (settlementClicked(destination, touchPoint))
            {
                if (origin != null && !origin.equals(destination))
                {
                    world.createArmy(origin, destination);
                    origin = null;
                }
            }
        }
        return true;
    }
}