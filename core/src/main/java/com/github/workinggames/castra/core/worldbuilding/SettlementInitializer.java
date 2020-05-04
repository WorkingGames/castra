package com.github.workinggames.castra.core.worldbuilding;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.model.PlayerColor;
import com.github.workinggames.castra.core.model.PlayerType;
import com.github.workinggames.castra.core.model.SettlementSize;
import com.github.workinggames.castra.core.stage.World;

@RequiredArgsConstructor
class SettlementInitializer
{
    private static final int PADDING_LEFT = 25;
    private static final int PADDING_RIGHT = 225;
    private static final int PADDING_BOTTOM = 25;
    private static final int PADDING_TOP = 200;
    private static final int MINIMUM_DISTANCE_AI_TO_PLAYER = 500;
    private static final int SPACING_BETWEEN_SETTLEMENTS = 250;
    private static final int DISTANCE_TO_ARMY_SPLIT = 120;

    private static final int MINIMUM_SETTLEMENT_TOTAL = 8;
    private static final int MAXIMUM_SETTLEMENT_TOTAL = 10;
    private static final int MINIMUM_LARGE_SETTLEMENTS = 2;
    private static final int MAXIMUM_LARGE_SETTLEMENTS = 4;
    private static final int MINIMUM_MEDIUM_SETTLEMENTS = 3;
    private static final int MAXIMUM_MEDIUM_SETTLEMENTS = 4;

    private static final int INITIAL_SOLDIER_SIZE_HQ = 100;
    private static final int MINIMUM_NEUTRAL_SOLDIER_SIZE = 1;
    private static final int MAXIMUM_NEUTRAL_SOLDIER_SIZE = 30;

    private static final Player NEUTRAL_PLAYER = new Player(new PlayerColor(new Color(0xc8c8c8ff),
        new Color(0x4b4b4bff)), "NEUTRAL", PlayerType.NEUTRAL);

    private final World world;
    private final Viewport viewport;
    private RandomXS128 random;
    private float worldWidth;
    private float worldHeight;

    public void initialize(long seed)
    {
        random = new RandomXS128(seed);
        worldWidth = viewport.getWorldWidth();
        worldHeight = viewport.getWorldHeight();

        int totalSettlements = getRandomValueInclusive(MINIMUM_SETTLEMENT_TOTAL, MAXIMUM_SETTLEMENT_TOTAL);
        int largeSettlements = getRandomValueInclusive(MINIMUM_LARGE_SETTLEMENTS, MAXIMUM_LARGE_SETTLEMENTS);
        int mediumSettlements = getRandomValueInclusive(MINIMUM_MEDIUM_SETTLEMENTS, MAXIMUM_MEDIUM_SETTLEMENTS);

        Array<GridPoint2> settlementPositions = getSettlementPositions(totalSettlements);

        Iterator<GridPoint2> positionIterator = settlementPositions.iterator();

        createSettlement(positionIterator.next(),
            SettlementSize.LARGE,
            INITIAL_SOLDIER_SIZE_HQ,
            world.getHumanPlayer());

        createSettlement(positionIterator.next(), SettlementSize.LARGE, INITIAL_SOLDIER_SIZE_HQ, world.getAiPlayer());

        for (int i = 2; i < largeSettlements; i++)
        {
            int soldiers = getRandomValueInclusive(MINIMUM_NEUTRAL_SOLDIER_SIZE, MAXIMUM_NEUTRAL_SOLDIER_SIZE);
            createSettlement(positionIterator.next(), SettlementSize.LARGE, soldiers, NEUTRAL_PLAYER);
        }

        for (int i = 0; i < mediumSettlements; i++)
        {
            int soldiers = getRandomValueInclusive(MINIMUM_NEUTRAL_SOLDIER_SIZE, MAXIMUM_NEUTRAL_SOLDIER_SIZE);
            createSettlement(positionIterator.next(), SettlementSize.MEDIUM, soldiers, NEUTRAL_PLAYER);
        }

        for (int i = largeSettlements + mediumSettlements; i < totalSettlements; i++)
        {
            int soldiers = getRandomValueInclusive(MINIMUM_NEUTRAL_SOLDIER_SIZE, MAXIMUM_NEUTRAL_SOLDIER_SIZE);
            createSettlement(positionIterator.next(), SettlementSize.SMALL, soldiers, NEUTRAL_PLAYER);
        }
    }

    private void createSettlement(GridPoint2 position, SettlementSize size, int soldiers, Player owner)
    {
        world.createSettlement(size, position.x, position.y, soldiers, owner);
    }

    private Array<GridPoint2> getSettlementPositions(int totalSettlements)
    {
        Array<GridPoint2> result = new Array<>();
        int maxX = (int) worldWidth - PADDING_RIGHT;
        int maxY = (int) worldHeight - PADDING_TOP;
        int retries = 0;
        while (result.size < totalSettlements)
        {
            GridPoint2 position = getRandomPosition(maxX, maxY);
            if (validSettlementPosition(position, result))
            {
                result.add(position);
            }
            else
            {
                retries++;
            }
            // if finding a valid position fails too often we remove a random position
            if (retries == 20)
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
                retries = 0;
            }
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
        valid = valid && enoughSpaceToArmySplit(position);
        return valid;
    }

    private boolean enoughSpaceToArmySplit(GridPoint2 position)
    {
        GridPoint2 armySplitPosition = new GridPoint2(0, 0);
        return position.dst(armySplitPosition) >= DISTANCE_TO_ARMY_SPLIT;
    }

    private int getRandomValueInclusive(int minimum, int maximum)
    {
        return minimum + random.nextInt(maximum - minimum + 1);
    }

    private GridPoint2 getRandomPosition(int maxX, int maxY)
    {
        int xPosition = getRandomValueInclusive(PADDING_LEFT, maxX);
        int yPosition = getRandomValueInclusive(PADDING_BOTTOM, maxY);
        return new GridPoint2(xPosition, yPosition);
    }
}