package de.incub8.castra.core.model;

import lombok.Getter;

import com.badlogic.gdx.math.GridPoint2;

public class Coordinates
{
    @Getter
    private final int width;
    @Getter
    private final int height;
    @Getter
    private final int count;
    private final GridPoint2[][] coordinates;

    public Coordinates(int width, int height)
    {
        this.width = width;
        this.height = height;
        coordinates = initializeCoordinates(width, height);
        count = width * height;
    }

    private GridPoint2[][] initializeCoordinates(int width, int height)
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

    public GridPoint2 get(int x, int y)
    {
        GridPoint2 result = null;
        if (x >= 1 && x <= width && y >= 1 && y <= height)
        {
            result = coordinates[x - 1][y - 1];
        }
        return result;
    }
}