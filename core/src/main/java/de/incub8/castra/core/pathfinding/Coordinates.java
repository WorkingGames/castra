package de.incub8.castra.core.pathfinding;

import lombok.Getter;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.viewport.Viewport;

class Coordinates
{
    @Getter
    private final int width;
    @Getter
    private final int height;
    @Getter
    private final int count;
    private final GridPoint2[][] coordinates;

    public Coordinates(Viewport viewport)
    {
        this.width = (int) viewport.getWorldWidth();
        this.height = (int) viewport.getWorldHeight();
        coordinates = initializeCoordinates();
        count = width * height;
    }

    private GridPoint2[][] initializeCoordinates()
    {
        GridPoint2[][] result = new GridPoint2[width][height];
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                result[x][y] = new GridPoint2(x + 1, y + 1);
            }
        }
        return result;
    }

    public GridPoint2 get(float x, float y)
    {
        GridPoint2 result = null;
        if (x >= 1 && x <= width && y >= 1 && y <= height)
        {
            result = coordinates[(int) x - 1][(int) y - 1];
        }
        return result;
    }
}