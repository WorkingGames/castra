package de.incub8.castra.core.actor;

import lombok.Getter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Align;
import de.incub8.castra.core.font.FontProvider;
import de.incub8.castra.core.input.ArmySplitDragSource;
import de.incub8.castra.core.input.ArmySplitDragTarget;
import de.incub8.castra.core.model.Player;

public class ArmySplit extends Group
{
    @Getter
    private final Group innerRimGroup;

    @Getter
    private final Group outerRimGroup;

    private final Label label;
    private final TextureAtlas textureAtlas;
    private final Player player;
    private final Label.LabelStyle labelStyle;

    public ArmySplit(TextureAtlas textureAtlas, FontProvider fontProvider, Player player)
    {
        this.textureAtlas = textureAtlas;
        this.player = player;

        labelStyle = new Label.LabelStyle(fontProvider.getSoldierCountFont(), Color.BLACK);

        setPosition(0, 0);
        Image outerRim = createImage("armySplitOuterRim");
        Label maxValue = createInfoLabel(2, 128, "100%");
        Label midValue = createInfoLabel(110, 80, "50%");
        Label minValue = createInfoLabel(185, 0, "5%");
        outerRimGroup = new Group();
        outerRimGroup.addActor(outerRim);
        outerRimGroup.addActor(maxValue);
        outerRimGroup.addActor(midValue);
        outerRimGroup.addActor(minValue);
        hideOuterRimGroup();
        addActor(outerRimGroup);

        Image innerRim = createImage("armySplit");
        label = createLabel();
        innerRimGroup = new Group();
        innerRimGroup.addActor(innerRim);
        innerRimGroup.addActor(label);
        addActor(innerRimGroup);

        DragAndDrop dragAndDrop = new DragAndDrop();
        dragAndDrop.addSource(new ArmySplitDragSource(this));
        dragAndDrop.addTarget(new ArmySplitDragTarget(this));
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
        Image result = new Image(textureAtlas.findRegion(name).getTexture());
        return result;
    }

    private Label createLabel()
    {
        Label result = new Label(player.getSendTroopPercentage() + "%", labelStyle);
        result.setWidth(60);
        result.setHeight(50);
        result.setPosition(30, 4);
        result.setAlignment(Align.right);
        return result;
    }

    private Label createInfoLabel(float x, float y, String value)
    {
        Label result = new Label(value, labelStyle);
        result.setPosition(x, y);
        return result;
    }
}