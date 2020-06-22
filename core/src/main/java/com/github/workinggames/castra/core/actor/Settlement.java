package com.github.workinggames.castra.core.actor;

import java.util.Map;

import lombok.Getter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.model.SettlementSize;
import com.github.workinggames.castra.core.screen.Screens;

public class Settlement extends Group
{
    private final Map<Player, SettlementImage> settlementImageMap;
    private final Label label;
    private final Image image;
    private final AnimatedImage highlight;
    private final AnimatedImage flags;
    private final boolean soldierCountVisible;

    @Getter
    private final int id;

    @Getter
    private final SettlementSize size;

    @Getter
    private final Ellipse hitbox;

    @Getter
    private int soldiers;

    @Getter
    private Player owner;

    @Getter
    private float centerX;

    @Getter
    private float centerY;

    public Settlement(
        int id,
        SettlementSize size,
        float x,
        float y,
        int soldiers,
        Player owner,
        BitmapFont font,
        boolean soldierCountVisible,
        Map<Player, SettlementImage> settlementImageMap)
    {
        this.id = id;
        this.size = size;
        this.soldiers = soldiers;
        this.owner = owner;
        this.soldierCountVisible = soldierCountVisible;
        this.settlementImageMap = settlementImageMap;

        setPosition(x, y);
        SettlementImage settlementImage = settlementImageMap.get(owner);
        image = new Image(settlementImage.getImage().getDrawable());

        setSize(image.getWidth(), image.getHeight());

        highlight = settlementImage.getHighlight().copy();
        highlight.setVisible(false);

        flags = settlementImage.getFlags().copy();
        if (owner.isNeutral())
        {
            flags.setVisible(false);
        }

        boolean detailsVisible = soldierCountVisible || !owner.isAi();
        label = createLabel(font, owner, detailsVisible);

        addActor(highlight);
        addActor(image);
        addActor(flags);
        addActor(label);

        hitbox = createHitbox();
        setZIndex(1);
    }

    private Label createLabel(BitmapFont font, Player owner, boolean visible)
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        Label result = new Label(String.valueOf(soldiers), labelStyle);
        applyOffset(result);
        result.setColor(owner.getColorSchema().getFontColor());
        result.setVisible(visible);
        return result;
    }

    private void applyOffset(Label label)
    {
        if (size.equals(SettlementSize.SMALL))
        {
            label.setWidth(48);
            label.setHeight(20);
            label.setX(58);
            label.setY(6);
        }
        else if (size.equals(SettlementSize.MEDIUM))
        {
            label.setWidth(56);
            label.setHeight(22);
            label.setX(58);
            label.setY(6);
        }
        else
        {
            label.setWidth(56);
            label.setHeight(22);
            label.setX(76);
            label.setY(7);
        }
        label.setAlignment(Align.center);
    }

    private Ellipse createHitbox()
    {
        float height = getWidth() * Screens.WIDTH_HEIGHT_RATIO;
        centerX = getX() + getWidth() / 2;
        centerY = getY() + height / 2;
        return new Ellipse(centerX, centerY, getWidth(), height);
    }

    public void changeOwner(Player newOwner)
    {
        owner = newOwner;
        SettlementImage settlementImage = settlementImageMap.get(newOwner);
        image.setDrawable(settlementImage.getImage().getDrawable());

        flags.setAnimation(settlementImage.getFlags().getAnimation());
        flags.setDrawable(settlementImage.getFlags().getDrawable());
        flags.setVisible(true);

        highlight.setAnimation(settlementImage.getHighlight().getAnimation());
        highlight.setDrawable(settlementImage.getHighlight().getDrawable());

        label.setColor(newOwner.getColorSchema().getFontColor());
        label.setVisible(soldierCountVisible || !owner.isAi());
    }

    public void addSoldier()
    {
        soldiers++;
        updateLabel();
    }

    public void removeSoldier()
    {
        soldiers--;
        updateLabel();
    }

    public void removeSoldiers(int count)
    {
        soldiers = soldiers - count;
        updateLabel();
    }

    public boolean isEmpty()
    {
        return soldiers == 0;
    }

    public void setHighlight(boolean highlighted)
    {
        highlight.setVisible(highlighted);
    }

    private void updateLabel()
    {
        label.setText(String.valueOf(soldiers));
    }
}