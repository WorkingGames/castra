package com.github.workinggames.castra.core.math;

import lombok.experimental.UtilityClass;

import com.badlogic.gdx.math.Rectangle;

@UtilityClass
public class Rectangles
{
    public boolean intersects(Rectangle a, Rectangle b)
    {
        return checkXAxis(a, b) && checkYAxis(a, b);
    }

    private boolean checkXAxis(Rectangle a, Rectangle b)
    {
        if (a.x <= b.x)
        {
            return (a.x + a.width) >= b.x;
        }
        else
        {
            return (b.x + b.width) >= a.x;
        }
    }

    private boolean checkYAxis(Rectangle a, Rectangle b)
    {
        if (a.y <= b.y)
        {
            return (a.y + a.height) >= b.y;
        }
        else
        {
            return (b.y + b.height) >= a.y;
        }
    }

    public boolean intersects(Rectangle a, Rectangle b, int spacing)
    {
        Rectangle aWithSpacing = rectangleWithSpacing(a, spacing);
        return intersects(aWithSpacing, b);
    }

    private Rectangle rectangleWithSpacing(Rectangle rectangle, int spacing)
    {
        return new Rectangle(rectangle.x - spacing,
            rectangle.y - spacing,
            rectangle.width + 2 * spacing,
            rectangle.height + 2 * spacing);
    }
}