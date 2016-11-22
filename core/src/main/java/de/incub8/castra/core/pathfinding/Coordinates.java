package de.incub8.castra.core.pathfinding;

import lombok.Getter;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

class Coordinates
{
    @Getter
    private final int width;
    @Getter
    private final int height;
    @Getter
    private final int count;
    private final Vector2[][] coordinates;

    public Coordinates(Viewport viewport)
    {
        this.width = (int) viewport.getWorldWidth();
        this.height = (int) viewport.getWorldHeight();
        coordinates = initializeCoordinates();
        count = width * height;
    }

    private Vector2[][] initializeCoordinates()
    {
        Vector2[][] result = new Vector2[width][height];
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                result[x][y] = new Vector2(x + 1, y + 1);
            }
        }
        return result;
    }

    public Vector2 get(float x, float y)
    {
        Vector2 result = null;
        if (x >= 1 && x <= width && y >= 1 && y <= height)
        {
            result = coordinates[(int) x - 1][(int) y - 1];
        }
        return result;
    }
}