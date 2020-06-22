package com.github.workinggames.castra.core.worldbuilding;

import static com.github.workinggames.castra.core.stage.GameConfiguration.NEUTRAL_PLAYER;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.Array;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.model.SettlementSize;
import com.github.workinggames.castra.core.stage.World;
import com.github.workinggames.castra.core.task.AsyncInitializer;

@RequiredArgsConstructor
public class SettlementInitializer implements AsyncInitializer
{
    private static final int MINIMUM_NEUTRAL_SOLDIER_SIZE_SMALL_SETTLEMENT = 1;
    private static final int MAXIMUM_NEUTRAL_SOLDIER_SIZE_SMALL_SETTLEMENT = 10;
    private static final int MINIMUM_NEUTRAL_SOLDIER_SIZE_MEDIUM_SETTLEMENT = 10;
    private static final int MAXIMUM_NEUTRAL_SOLDIER_SIZE_MEDIUM_SETTLEMENT = 20;
    private static final int MINIMUM_NEUTRAL_SOLDIER_SIZE_LARGE_SETTLEMENT = 20;
    private static final int MAXIMUM_NEUTRAL_SOLDIER_SIZE_LARGE_SETTLEMENT = 30;

    private final World world;
    private RandomXS128 random;
    private boolean finished;

    public void initialize()
    {
        random = new RandomXS128(world.getGameConfiguration().getSeed());

        Array<GridPoint2> settlementPositions = getBackground1SettlementPositions();

        for (int i = 0; i < settlementPositions.size; i++)
        {
            GridPoint2 position = settlementPositions.get(i);
            if (i == 0)
            {
                createSettlement(position,
                    SettlementSize.LARGE,
                    world.getGameConfiguration().getStartingSoldiers(),
                    world.getGameConfiguration().getPlayer1());
            }
            else if (i == settlementPositions.size - 1)
            {
                createSettlement(position,
                    SettlementSize.LARGE,
                    world.getGameConfiguration().getStartingSoldiers(),
                    world.getGameConfiguration().getPlayer2());
            }
            else
            {
                SettlementSize settlementSize;
                int soldiers;
                if (getRandomBoolean(0.2f))
                {
                    settlementSize = SettlementSize.LARGE;
                    soldiers = getRandomValueInclusive(MINIMUM_NEUTRAL_SOLDIER_SIZE_LARGE_SETTLEMENT,
                        MAXIMUM_NEUTRAL_SOLDIER_SIZE_LARGE_SETTLEMENT);
                }
                else if (getRandomBoolean(0.4f))
                {
                    settlementSize = SettlementSize.MEDIUM;
                    soldiers = getRandomValueInclusive(MINIMUM_NEUTRAL_SOLDIER_SIZE_MEDIUM_SETTLEMENT,
                        MAXIMUM_NEUTRAL_SOLDIER_SIZE_MEDIUM_SETTLEMENT);
                }
                else
                {
                    settlementSize = SettlementSize.SMALL;
                    soldiers = getRandomValueInclusive(MINIMUM_NEUTRAL_SOLDIER_SIZE_SMALL_SETTLEMENT,
                        MAXIMUM_NEUTRAL_SOLDIER_SIZE_SMALL_SETTLEMENT);
                }
                createSettlement(position, settlementSize, soldiers, NEUTRAL_PLAYER);
            }
        }

        finished = true;
    }

    private void createSettlement(GridPoint2 position, SettlementSize size, int soldiers, Player owner)
    {
        world.createSettlement(size, position.x, position.y, soldiers, owner);
    }

    private Array<GridPoint2> getBackground1SettlementPositions()
    {
        Array<GridPoint2> result = new Array<>();
        result.add(new GridPoint2(100, 400));
        result.add(new GridPoint2(150, 780));
        result.add(new GridPoint2(425, 170));
        result.add(new GridPoint2(540, 500));
        result.add(new GridPoint2(888, 760));
        result.add(new GridPoint2(940, 170));
        result.add(new GridPoint2(1086, 456));
        result.add(new GridPoint2(1300, 886));
        result.add(new GridPoint2(1450, 210));
        result.add(new GridPoint2(1575, 620));
        return result;
    }

    private int getRandomValueInclusive(int minimum, int maximum)
    {
        return minimum + random.nextInt(maximum - minimum + 1);
    }

    // chance is a percent value from 0 for 0% to 1 for 100%
    private boolean getRandomBoolean(float chance)
    {
        return random.nextFloat() < chance;
    }

    @Override
    public boolean isFinished()
    {
        return finished;
    }
}