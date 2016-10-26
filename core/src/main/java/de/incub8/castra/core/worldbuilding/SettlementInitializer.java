package de.incub8.castra.core.worldbuilding;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.incub8.castra.core.model.Player;
import de.incub8.castra.core.model.PlayerType;
import de.incub8.castra.core.model.SettlementSize;
import de.incub8.castra.core.stage.World;

class SettlementInitializer
{
    private static final int PADDING_X_LEFT = 25;
    private static final int PADDING_X_RIGHT = 225;
    private static final int PADDING_Y_BOTTOM = 25;
    private static final int PADDING_Y_TOP = 200;
    private static final int MINIMUM_DISTANCE_AI_TO_PLAYER = 500;
    private static final int SPACING_BETWEEN_SETTLEMENTS = 250;

    private static final int MINIMUM_SETTLEMENT_TOTAL = 8;
    private static final int MAXIMUM_SETTLEMENT_TOTAL = 10;
    private static final int MINIMUM_LARGE_SETTLEMENTS = 2;
    private static final int MAXIMUM_LARGE_SETTLEMENTS = 4;
    private static final int MINIMUM_MEDIUM_SETTLEMENTS = 3;
    private static final int MAXIMUM_MEDIUM_SETTLEMENTS = 4;

    private static final int INITIAL_SOLDIER_SIZE_HQ = 100;
    private static final int MINIMUM_NEUTRAL_SOLDIER_SIZE = 1;
    private static final int MAXIMUM_NEUTRAL_SOLDIER_SIZE = 30;

    private static final Player NEUTRAL_PLAYER = new Player(Color.GRAY, 0, "NEUTRAL", PlayerType.NEUTRAL);

    private World world;
    private RandomXS128 random;
    private float worldWidth;
    private float worldHeight;

    private boolean humanSettlementPlaced;
    private boolean aiSettlementPlaced;

    private int totalSettlements;
    private int largeSettlements;
    private int mediumSettlements;

    public void initialize(World world, long seed, Viewport viewport)
    {
        this.world = world;
        random = new RandomXS128(seed);
        worldWidth = viewport.getWorldWidth();
        worldHeight = viewport.getWorldHeight();

        totalSettlements = getRandomValueInclusive(MINIMUM_SETTLEMENT_TOTAL, MAXIMUM_SETTLEMENT_TOTAL);
        largeSettlements = getRandomValueInclusive(MINIMUM_LARGE_SETTLEMENTS, MAXIMUM_LARGE_SETTLEMENTS);
        mediumSettlements = getRandomValueInclusive(MINIMUM_MEDIUM_SETTLEMENTS, MAXIMUM_MEDIUM_SETTLEMENTS);

        Array<GridPoint2> settlementPositions = getSettlementPositions();
        for (GridPoint2 position : settlementPositions)
        {
            Player owner = getOwner();
            world.createSettlement(getSize(), position.x, position.y, getSoldiers(), owner);
            humanSettlementPlaced = humanSettlementPlaced || owner.isHuman();
            aiSettlementPlaced = aiSettlementPlaced || owner.isAi();
        }
    }

    private Array<GridPoint2> getSettlementPositions()
    {
        Array<GridPoint2> result = new Array<>();
        int maxX = (int) worldWidth - PADDING_X_RIGHT;
        int maxY = (int) worldHeight - PADDING_Y_TOP;
        int retries = 20;
        while (result.size < totalSettlements)
        {
            GridPoint2 position = getRandomPosition(maxX, maxY);
            if (validSettlementPosition(position, result))
            {
                result.add(position);
            }
            else
            {
                retries--;
            }
            // if finding a valid position fails too often we remove a random position
            if (retries == 0)
            {
                // we don't remove the HQs, but other settlements
                if (result.size > 2)
                {
                    int index = getRandomValueInclusive(2, result.size - 1);
                    result.removeIndex(index);
                }
                // the placement of the first HQ was so bad that we remove it
                else
                {
                    result.removeValue(result.first(), true);
                }
                retries = 20;
            }
        }
        return result;
    }

    private Player getOwner()
    {
        Player owner;
        if (!humanSettlementPlaced)
        {
            owner = world.getHumanPlayer();
        }
        else if (!aiSettlementPlaced)
        {
            owner = world.getAiPlayer();
        }
        else
        {
            owner = NEUTRAL_PLAYER;
        }
        return owner;
    }

    private SettlementSize getSize()
    {
        SettlementSize result;
        if (largeSettlements > 0)
        {
            result = SettlementSize.LARGE;
            largeSettlements--;
        }
        else if (mediumSettlements > 0)
        {
            result = SettlementSize.MEDIUM;
            mediumSettlements--;
        }
        else
        {
            result = SettlementSize.SMALL;
        }
        return result;
    }

    private int getSoldiers()
    {
        int result;
        if (!humanSettlementPlaced || !aiSettlementPlaced)
        {
            result = INITIAL_SOLDIER_SIZE_HQ;
        }
        else
        {
            result = getRandomValueInclusive(MINIMUM_NEUTRAL_SOLDIER_SIZE, MAXIMUM_NEUTRAL_SOLDIER_SIZE);
        }
        return result;
    }

    private boolean validSettlementPosition(GridPoint2 position, Array<GridPoint2> settlements)
    {
        boolean valid = true;
        // Check distance between HQs
        if (settlements.size == 1)
        {
            float distance = position.dst(settlements.first());
            valid = distance >= MINIMUM_DISTANCE_AI_TO_PLAYER;
        }
        // Check distance between other settlements
        if (settlements.size > 1)
        {
            for (GridPoint2 settlement : settlements)
            {
                float distance = position.dst(settlement);
                if (distance <= SPACING_BETWEEN_SETTLEMENTS)
                {
                    valid = false;
                    break;
                }
            }
        }
        return valid;
    }

    private int getRandomValueInclusive(int minimum, int maximum)
    {
        return minimum + random.nextInt(maximum - minimum + 1);
    }

    private GridPoint2 getRandomPosition(int maxX, int maxY)
    {
        int xPosition = getRandomValueInclusive(PADDING_X_LEFT, maxX);
        int yPosition = getRandomValueInclusive(PADDING_Y_BOTTOM, maxY);
        return new GridPoint2(xPosition, yPosition);
    }
}