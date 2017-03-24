package de.incub8.castra.core.pathfinding;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.utils.Array;
import de.incub8.castra.core.actor.Settlement;
import de.incub8.castra.core.model.ArmySize;

public class PathUtils
{
    private final int armyWidth;
    private final int armyHeight;

    public PathUtils(TextureAtlas textureAtlas)
    {
        Texture armyTexture = textureAtlas.findRegion(ArmySize.LARGE.getTextureName()).getTexture();
        armyWidth = armyTexture.getWidth();
        armyHeight = armyTexture.getHeight() / 4;
    }

    public Ellipse getHitboxWithArmySpacing(Settlement settlement)
    {
        Ellipse hitboxWithArmySpacing = new Ellipse(settlement.getHitbox());
        hitboxWithArmySpacing.setSize(
            hitboxWithArmySpacing.width + armyWidth, hitboxWithArmySpacing.height + armyHeight);
        return hitboxWithArmySpacing;
    }

    public boolean intersectsWithOtherSettlements(
        Settlement origin, Settlement destination, Line line, Array<Settlement> settlements)
    {
        Array<Ellipse> hitBoxes = new Array<>();
        for (Settlement settlement : settlements)
        {
            if (!settlement.equals(origin) && !settlement.equals(destination))
            {
                hitBoxes.add(getHitboxWithArmySpacing(settlement));
            }
        }
        return lineIntersectsHitboxEllipse(line, hitBoxes);
    }

    public boolean lineIntersectsHitboxEllipse(Line line, Array<Ellipse> settlementHitboxes)
    {
        boolean intersects = false;
        for (Ellipse ellipse : settlementHitboxes)
        {
            intersects = line.intersectsEllipse(ellipse);
            if (intersects)
            {
                break;
            }
        }
        return intersects;
    }

    public Array<Line> reverse(Array<Line> lines)
    {
        Array<Line> result = new Array<>();
        for (int i = lines.size - 1; i >= 0; i--)
        {
            Line temp = lines.get(i);
            Line reversed = new Line(temp.getEnd(), temp.getStart());
            result.add(reversed);
        }
        return result;
    }
}