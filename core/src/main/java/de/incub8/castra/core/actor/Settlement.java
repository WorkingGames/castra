package de.incub8.castra.core.actor;

import lombok.Getter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import de.incub8.castra.core.Castra;
import de.incub8.castra.core.font.FontProvider;
import de.incub8.castra.core.model.Player;
import de.incub8.castra.core.model.SettlementSize;
import de.incub8.castra.core.texture.ColorizingTextureAtlasAdapter;

public class Settlement extends Group
{
    private static final int IMAGE_COLUMNS = 4;
    private final Image image;
    private final Label label;
    private final ColorizingTextureAtlasAdapter textureAtlas;

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
        SettlementSize size,
        float x,
        float y,
        int soldiers,
        Player owner,
        TextureAtlas textureAtlas,
        FontProvider fontProvider)
    {
        this.textureAtlas = new ColorizingTextureAtlasAdapter(textureAtlas);
        this.size = size;
        this.soldiers = soldiers;
        this.owner = owner;
        setPosition(x, y);

        image = createImage();

        setSize(image.getWidth(), image.getHeight());

        label = createLabel(fontProvider);

        hitbox = createHitbox();
    }

    private Image createImage()
    {
        Image result = new Image(getCastleTexture());
        addActor(result);
        return result;
    }

    private Label createLabel(FontProvider fontProvider)
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle(fontProvider.getFont(), Color.BLACK);
        Label result = new Label(String.valueOf(soldiers), labelStyle);
        scaleAndAlignLabel(result);
        result.setVisible(!owner.isAi());
        addActor(result);
        return result;
    }

    private void scaleAndAlignLabel(Label label)
    {
        label.setFontScale(1.8f);
        if (size.equals(SettlementSize.SMALL))
        {
            label.setX(getWidth() / 2 - 5);
            label.setY(5);
        }
        else if (size.equals(SettlementSize.MEDIUM))
        {
            label.setX(getWidth() / 2 - 10);
            label.setY(5);
        }
        else
        {
            label.setX(getWidth() / 2 - 15);
            label.setY(7);
        }
        label.setAlignment(Align.center);
    }

    private Ellipse createHitbox()
    {
        float height = getWidth() * Castra.WIDTH_HEIGHT_RATIO;
        centerX = getX() + getWidth() / 2;
        centerY = getY() + height / 2;
        Ellipse result = new Ellipse(centerX, centerY, getWidth(), height);
        return result;
    }

    public void changeOwner(Player newOwner)
    {
        owner = newOwner;
        image.setDrawable(new TextureRegionDrawable(getCastleTexture()));
        label.setVisible(!owner.isAi());
    }

    private TextureRegion getCastleTexture()
    {
        Texture allCastleColors = textureAtlas.findRegion(size.getTextureName(), owner.getColor()).getTexture();
        int width = allCastleColors.getWidth() / IMAGE_COLUMNS;
        int height = allCastleColors.getHeight();
        TextureRegion[][] castles = TextureRegion.split(allCastleColors, width, height);
        TextureRegion castle = castles[0][owner.getTextureIndex()];
        return castle;
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