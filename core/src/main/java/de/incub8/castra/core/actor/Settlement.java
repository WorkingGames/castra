package de.incub8.castra.core.actor;

import lombok.Getter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.incub8.castra.core.Castra;
import de.incub8.castra.core.font.FontProvider;
import de.incub8.castra.core.model.Player;
import de.incub8.castra.core.model.SettlementSize;

public class Settlement extends Group
{
    private final Image image;
    private final Label label;

    @Getter
    private final SettlementSize size;

    @Getter
    private final Ellipse hitbox;

    @Getter
    private int soldiers;

    @Getter
    private Player owner;

    public Settlement(
        SettlementSize size,
        float x,
        float y,
        int soldiers,
        Player owner,
        TextureAtlas textureAtlas,
        FontProvider fontProvider)
    {
        this.size = size;
        this.soldiers = soldiers;
        this.owner = owner;
        setPosition(x, y);

        image = createImage(size, textureAtlas);

        setSize(image.getWidth(), image.getHeight());

        label = createLabel(fontProvider);

        hitbox = createHitbox();
    }

    private Image createImage(SettlementSize size, TextureAtlas textureAtlas)
    {
        Image result = new Image(textureAtlas.findRegion(size.getTextureName()).getTexture());
        result.setColor(owner.getColor());
        addActor(result);
        return result;
    }

    private Label createLabel(FontProvider fontProvider)
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle(fontProvider.getFont(), Color.WHITE);
        Label result = new Label(String.valueOf(soldiers), labelStyle);
        result.setX(image.getWidth() / 2);
        result.setVisible(!owner.isAi());
        addActor(result);
        return result;
    }

    private Ellipse createHitbox()
    {
        float height = getWidth() * Castra.WIDTH_HEIGHT_RATIO;
        float centerX = getX() + getWidth() / 2;
        float centerY = getY() + height / 2;
        Ellipse result = new Ellipse(centerX, centerY, getWidth(), height);
        return result;
    }

    public void changeOwner(Player newOwner)
    {
        owner = newOwner;
        image.setColor(owner.getColor());
        label.setVisible(!owner.isAi());
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

    private void updateLabel()
    {
        label.setText(String.valueOf(soldiers));
    }
}