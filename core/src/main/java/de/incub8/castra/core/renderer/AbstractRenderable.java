package de.incub8.castra.core.renderer;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class AbstractRenderable implements Comparable<AbstractRenderable>
{
    public abstract void render(SpriteBatch batch, BitmapFont font);

    protected abstract int getX();

    protected abstract int getY();

    @Override
    public int compareTo(AbstractRenderable other)
    {
        if (getY() < other.getY())
        {
            return 1;
        }
        else if (getY() > other.getY())
        {
            return -1;
        }
        else
        {
            if (getX() > other.getX())
            {
                return -1;
            }
            else if (getX() < other.getX())
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
    }
}