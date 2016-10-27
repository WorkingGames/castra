package de.incub8.castra.core.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import de.incub8.castra.core.font.FontProvider;
import de.incub8.castra.core.model.Player;

public class ArmySplit extends Group
{
    private final Label label;
    private final TextureAtlas textureAtlas;
    private final Player player;

    public ArmySplit(TextureAtlas textureAtlas, FontProvider fontProvider, Player player)
    {
        this.textureAtlas = textureAtlas;
        this.player = player;

        createImage();
        setPosition(0, 0);

        Label.LabelStyle labelStyle = new Label.LabelStyle(fontProvider.getSoldierCountFont(), Color.BLACK);
        label = createLabel(labelStyle);
    }

    private Image createImage()
    {
        Image result = new Image(textureAtlas.findRegion("armySplit").getTexture());
        addActor(result);
        return result;
    }

    private Label createLabel(Label.LabelStyle labelStyle)
    {
        Label result = new Label(player.getSendTroopPercentage() + "%", labelStyle);
        result.setWidth(60);
        result.setHeight(50);
        result.setX(30);
        result.setY(4);
        result.setAlignment(Align.right);
        addActor(result);
        return result;
    }

    public void updateLabel()
    {
        label.setText(player.getSendTroopPercentage() + "%");
    }
}