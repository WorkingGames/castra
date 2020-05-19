package com.github.workinggames.castra.core.actor;

import lombok.Getter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.action.MoveAlongAction;
import com.github.workinggames.castra.core.model.ArmySize;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.pathfinding.LinePath;

public class Army extends Group
{
    @Getter
    private final int id;

    private final AnimatedImage image;

    @Getter
    private final Player owner;

    @Getter
    private final Settlement source;

    @Getter
    private final Settlement target;

    @Getter
    private final ArmySize armySize;

    @Getter
    private final LinePath path;

    @Getter
    private int soldiers;

    public Army(
        int id,
        int soldiers,
        Player owner,
        Settlement source,
        Settlement target,
        LinePath path,
        BitmapFont font,
        AnimatedImage animatedImage,
        boolean soldierCountVisible)
    {
        this.id = id;
        this.soldiers = soldiers;
        this.owner = owner;
        this.source = source;
        this.target = target;
        this.path = path;

        armySize = ArmySize.bySoldierCount(soldiers);

        image = animatedImage.copy();

        setSize(image.getWidth(), image.getHeight());
        setZIndex(100);

        // flip image, moving from right to left
        if (path.valueAt(0).x > path.valueAt(1).x)
        {
            image.setOriginX(image.getWidth() / 2);
            image.setScaleX(-1);
        }
        addActor(image);

        boolean detailsVisible = soldierCountVisible || !owner.isAi();
        Label label = createLabel(font, detailsVisible);
        addActor(label);

        // without this sometimes the armies would flash in the lower left corner until the movement is started
        Vector2 initialPosition = path.valueAt(0);
        setPosition(initialPosition.x, initialPosition.y);

        addAction(MoveAlongAction.obtain(path));
    }

    private Label createLabel(BitmapFont font, boolean visible)
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        Label result = new Label(String.valueOf(soldiers), labelStyle);
        result.setX(image.getWidth() / 2);
        result.setVisible(visible);
        return result;
    }

    public boolean isAtTarget()
    {
        float height = getWidth() * Castra.WIDTH_HEIGHT_RATIO;
        float centerX = getX() + getWidth() / 2;
        float centerY = getY() + height / 2;
        return target.getHitbox().contains(centerX, centerY);
    }

    public void removeSoldier()
    {
        soldiers--;
    }

    public void addSoldiers(int count)
    {
        this.soldiers += count;
    }

    public boolean isEmpty()
    {
        return soldiers == 0;
    }
}