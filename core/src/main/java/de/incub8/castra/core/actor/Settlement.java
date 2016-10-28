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
import de.incub8.castra.core.model.PlayerColor;
import de.incub8.castra.core.model.SettlementSize;
import de.incub8.castra.core.texture.ColorizingTextureAtlasAdapter;

public class Settlement extends Group
{
    private static final PlayerColor HIGHLIGHT_COLOR = new PlayerColor(new Color(0xc8c8c8ff), new Color(0x4b4b4bff));
    private static final int IMAGE_COLUMNS = 2;
    
    private final Image image;
    private final Image highlight;
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

        highlight = createImage(getHighlightTexture());
        highlight.setVisible(false);
        applyOffset(highlight);

        image = createImage(getCastleTexture());

        setSize(image.getWidth(), image.getHeight());

        label = createLabel(fontProvider);

        hitbox = createHitbox();
    }

    private void applyOffset(Image highlight)
    {
        if (size.equals(SettlementSize.SMALL))
        {
            highlight.setX(-10);
            highlight.setY(42);
        }
        else if (size.equals(SettlementSize.MEDIUM))
        {
            highlight.setX(-11);
            highlight.setY(15);
        }
        else
        {
            highlight.setX(-14);
            highlight.setY(35);
        }
    }

    private Image createImage(TextureRegion textureRegion)
    {
        Image result = new Image(textureRegion);
        addActor(result);
        return result;
    }

    private Label createLabel(FontProvider fontProvider)
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle(fontProvider.getSoldierCountFont(), Color.BLACK);
        Label result = new Label(String.valueOf(soldiers), labelStyle);
        applyOffset(result);
        result.setVisible(!owner.isAi());
        addActor(result);
        return result;
    }

    private void applyOffset(Label label)
    {
        if (size.equals(SettlementSize.SMALL))
        {
            label.setWidth(48);
            label.setHeight(20);
            label.setX(52);
            label.setY(6);
        }
        else if (size.equals(SettlementSize.MEDIUM))
        {
            label.setWidth(56);
            label.setHeight(22);
            label.setX(52);
            label.setY(7);
        }
        else
        {
            label.setWidth(56);
            label.setHeight(22);
            label.setX(70);
            label.setY(9);
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
        highlight.setDrawable(new TextureRegionDrawable(getHighlightTexture()));
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

    private TextureRegion getHighlightTexture()
    {
        Texture allHighlights = textureAtlas.findRegion(size.getHighlightTextureName(), HIGHLIGHT_COLOR).getTexture();
        int width = allHighlights.getWidth() / IMAGE_COLUMNS;
        int height = allHighlights.getHeight();
        TextureRegion[][] highlights = TextureRegion.split(allHighlights, width, height);
        TextureRegion highlight;
        if (owner.isNeutral())
        {
            highlight = highlights[0][0];
        }
        else
        {
            highlight = highlights[0][1];
        }
        return highlight;
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