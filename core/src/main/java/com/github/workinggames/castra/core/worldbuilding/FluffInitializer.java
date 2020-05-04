package com.github.workinggames.castra.core.worldbuilding;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.math.Rectangles;
import com.github.workinggames.castra.core.stage.World;

@Slf4j
@RequiredArgsConstructor
class FluffInitializer
{
    private static final int PADDING_LEFT = 25;
    private static final int PADDING_RIGHT = 100;
    private static final int PADDING_BOTTOM = 25;
    private static final int PADDING_TOP = 120;

    private static final int MINIMUM_SPACING_TO_SETTLEMENTS = 30;
    private static final int MINIMUM_SPACING_TO_OTHER_FLUFF = 10;
    private static final int DISTANCE_TO_ARMY_SPLIT = 120;

    private static final int MINIMUM_TREES = 3;
    private static final int MAXIMUM_TREES = 10;
    private static final int MINIMUM_STONES = 3;
    private static final int MAXIMUM_STONES = 10;
    private static final int MINIMUM_OTHER = 0;
    private static final int MAXIMUM_OTHER = 2;

    private final World world;
    private final Viewport viewport;
    private final Map<String, Texture> fluffTextures = new HashMap<>();

    private RandomXS128 random;
    private float worldWidth;
    private float worldHeight;

    public void initialize()
    {
        random = new RandomXS128(world.getSeed());
        worldWidth = viewport.getWorldWidth();
        worldHeight = viewport.getWorldHeight();

        for (String name : FluffLoader.ALL)
        {
            fluffTextures.put(name, world.getTextureAtlas().findRegion(name).getTexture());
        }

        int trees = getRandomValueInclusive(MINIMUM_TREES, MAXIMUM_TREES);
        int stones = getRandomValueInclusive(MINIMUM_STONES, MAXIMUM_STONES);
        int other = getRandomValueInclusive(MINIMUM_OTHER, MAXIMUM_OTHER);

        Array<Image> fluff = new Array<>();
        addFluff(fluff, FluffLoader.TREES, trees);
        addFluff(fluff, FluffLoader.STONES, stones);
        addFluff(fluff, FluffLoader.OTHER, other);
        addSpecialFluff(fluff);

        for (Image image : fluff)
        {
            world.createFluff(image);
        }
    }

    private void addFluff(Array<Image> fluffs, List<String> names, int amount)
    {
        Array<Image> result = new Array<>();
        int maxX = (int) worldWidth - PADDING_RIGHT;
        int maxY = (int) worldHeight - PADDING_TOP;
        int retries = 0;

        while (result.size < amount)
        {
            Image fluff = new Image(fluffTextures.get(getRandomName(names)));
            GridPoint2 position = getRandomPosition(maxX, maxY);

            if (validFluffPosition(fluff, position, result))
            {
                fluff.setPosition(position.x, position.y);
                result.add(fluff);
            }
            else
            {
                retries++;
            }
            // if finding a valid position fails too often we remove a random position
            if (retries == 20)
            {
                int index = getRandomValueInclusive(0, result.size - 1);
                result.removeIndex(index);
                retries = 0;
            }
        }
        fluffs.addAll(result);
    }

    private void addSpecialFluff(Array<Image> fluffs)
    {
        int special = getRandomValueInclusive(0, 1000);
        if (special < 200)
        {
            addFluff(fluffs, Collections.singletonList("NoSwordStone"), 1);
        }
        else if (special == 999)
        {
            addFluff(fluffs, Collections.singletonList("SwordStone"), 1);
        }
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

    private String getRandomName(List<String> names)
    {
        return names.get(getRandomValueInclusive(0, names.size() - 1));
    }

    private boolean validFluffPosition(Image fluff, GridPoint2 position, Array<Image> fluffs)
    {
        boolean valid = true;
        Rectangle fluffImage = new Rectangle(position.x, position.y, fluff.getWidth(), fluff.getHeight());
        // Check distance to Settlements
        for (Settlement settlement : world.getSettlements())
        {
            Rectangle settlementImage = new Rectangle(settlement.getX(),
                settlement.getY(),
                settlement.getWidth(),
                settlement.getHeight());
            valid = valid && !Rectangles.intersects(settlementImage, fluffImage, MINIMUM_SPACING_TO_SETTLEMENTS);
        }
        // Check distance between other fluff
        if (fluffs.size > 1)
        {
            for (Image otherFluff : fluffs)
            {
                Rectangle otherFluffImage = new Rectangle(otherFluff.getX(),
                    otherFluff.getY(),
                    otherFluff.getWidth(),
                    otherFluff.getHeight());
                valid = valid && !Rectangles.intersects(otherFluffImage, fluffImage, MINIMUM_SPACING_TO_OTHER_FLUFF);
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
}