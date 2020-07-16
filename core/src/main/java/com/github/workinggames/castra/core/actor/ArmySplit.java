package com.github.workinggames.castra.core.actor;

import lombok.Getter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Align;
import com.github.workinggames.castra.core.audio.AudioManager;
import com.github.workinggames.castra.core.font.FontProvider;
import com.github.workinggames.castra.core.input.ArmySplitDragSource;
import com.github.workinggames.castra.core.input.ArmySplitDragTarget;
import com.github.workinggames.castra.core.model.Player;

public class ArmySplit extends Group
{
    @Getter
    private final Group innerRimGroup;

    @Getter
    private final Group outerRimGroup;

    private final Label label;
    private final TextureAtlas textureAtlas;
    private final FontProvider fontProvider;
    private final Player player;

    public ArmySplit(TextureAtlas textureAtlas, FontProvider fontProvider, Player player, AudioManager audioManager)
    {
        this.textureAtlas = textureAtlas;
        this.fontProvider = fontProvider;
        this.player = player;

        setPosition(0, 0);
        setZIndex(0);
        Image outerRim = createImage("armySplitOuterRim");
        setSize(outerRim.getWidth(), outerRim.getHeight());
        Label firstInfo = createInfoLabel(155, 62, "25%");
        Label secondInfo = createInfoLabel(100, 105, "50%");
        Label thirdInfo = createInfoLabel(40, 128, "75%");
        outerRimGroup = new Group();
        outerRimGroup.addActor(outerRim);
        outerRimGroup.addActor(firstInfo);
        outerRimGroup.addActor(secondInfo);
        outerRimGroup.addActor(thirdInfo);
        hideOuterRimGroup();
        addActor(outerRimGroup);

        Image innerRim = createImage("armySplit");
        label = createLabel();
        innerRimGroup = new Group();
        innerRimGroup.addActor(innerRim);
        innerRimGroup.addActor(label);
        addActor(innerRimGroup);

        DragAndDrop dragAndDrop = new DragAndDrop();
        dragAndDrop.addSource(new ArmySplitDragSource(this, audioManager));
        ArmySplitDragTarget dragTarget = new ArmySplitDragTarget(this, audioManager);
        dragAndDrop.addTarget(dragTarget);

        innerRimGroup.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if (!outerRimGroup.isVisible())
                {
                    showOuterRimGroup();
                }
                else
                {
                    hideOuterRimGroup();
                }
                Gdx.input.vibrate(50);
                audioManager.playClickSound();
            }
        });

        outerRimGroup.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                int armySplitPercentage = dragTarget.calculateArmySplitPercentage(x, y);
                updatePercentage(armySplitPercentage);
                Gdx.input.vibrate(50);
                audioManager.playClickSound();
                hideOuterRimGroup();
            }
        });
    }

    public void updateLabel()
    {
        label.setText(player.getSendTroopPercentage() + "%");
    }

    public void showOuterRimGroup()
    {
        outerRimGroup.setVisible(true);
    }

    public void hideOuterRimGroup()
    {
        outerRimGroup.setVisible(false);
    }

    public void updatePercentage(int percentage)
    {
        player.setSendTroopPercentage(percentage);
        updateLabel();
    }

    private Image createImage(String name)
    {
        return new Image(textureAtlas.findRegion(name).getTexture());
    }

    private Label createLabel()
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle(fontProvider.getSoldierCount(), Color.BLACK);
        Label result = new Label(player.getSendTroopPercentage() + "%", labelStyle);
        result.setWidth(60);
        result.setHeight(50);
        result.setPosition(30, 4);
        result.setAlignment(Align.right);
        return result;
    }

    private Label createInfoLabel(float x, float y, String value)
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle(fontProvider.getSplitInfo(), Color.BLACK);
        Label result = new Label(value, labelStyle);
        result.setPosition(x, y);
        return result;
    }
}