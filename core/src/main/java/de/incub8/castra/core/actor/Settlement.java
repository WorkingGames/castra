package de.incub8.castra.core.actor;

import lombok.Getter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
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
    private static final int FLAG_ANIMATION_COLUMNS = 6;
    private static final int HIGHLIGHT_ANIMATION_COLUMNS = 6;

    private final Image image;
    private final AnimatedImage highlight;
    private final AnimatedImage flags;
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

        if (owner.isNeutral())
        {
            highlight = createAnimatedImage(getNeutralHighlightTexture());
        }
        else
        {
            highlight = createAnimatedImage(getHighlightAnimation());
        }
        highlight.setVisible(false);

        image = createImage(getCastleTexture());

        setSize(image.getWidth(), image.getHeight());

        flags = createAnimatedImage(getFlagAnimation());
        if (owner.isNeutral())
        {
            flags.setVisible(false);
        }

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

    private AnimatedImage createAnimatedImage(Animation animation)
    {
        AnimatedImage animatedImage = new AnimatedImage(animation);
        addActor(animatedImage);
        return animatedImage;
    }

    private AnimatedImage createAnimatedImage(TextureRegion textureRegion)
    {
        AnimatedImage animatedImage = new AnimatedImage(textureRegion);
        addActor(animatedImage);
        return animatedImage;
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

        flags.setAnimation(getFlagAnimation());
        flags.setVisible(true);

        highlight.setAnimation(getHighlightAnimation());

        label.setVisible(!owner.isAi());
    }

    private TextureRegion getCastleTexture()
    {
        Texture allCastleColors = textureAtlas.findRegion(size.getTextureName(), owner.getColor()).getTexture();
        return new TextureRegion(allCastleColors);
    }

    private Animation getHighlightAnimation()
    {
        Texture highlightTexture = getHighlightAnimationTexture();
        TextureRegion[][] tmp = TextureRegion.split(
            highlightTexture, highlightTexture.getWidth() / HIGHLIGHT_ANIMATION_COLUMNS, highlightTexture.getHeight());
        TextureRegion[] highlightFrames = new TextureRegion[HIGHLIGHT_ANIMATION_COLUMNS];
        int index = 0;
        for (int j = 0; j < HIGHLIGHT_ANIMATION_COLUMNS; j++)
        {
            highlightFrames[index++] = tmp[0][j];
        }
        Animation highlightAnimation = new Animation(0.2f, highlightFrames);
        highlightAnimation.setPlayMode(Animation.PlayMode.LOOP);
        return highlightAnimation;
    }

    private Animation getFlagAnimation()
    {
        Texture flagTexture = getFlagTexture();
        TextureRegion[][] tmp = TextureRegion.split(
            flagTexture, flagTexture.getWidth() / FLAG_ANIMATION_COLUMNS, flagTexture.getHeight());
        TextureRegion[] flagFrames = new TextureRegion[FLAG_ANIMATION_COLUMNS];
        int index = 0;
        for (int j = 0; j < FLAG_ANIMATION_COLUMNS; j++)
        {
            flagFrames[index++] = tmp[0][j];
        }
        Animation flagAnimation = new Animation(0.2f, flagFrames);
        flagAnimation.setPlayMode(Animation.PlayMode.LOOP);
        return flagAnimation;
    }

    private Texture getFlagTexture()
    {
        Texture flagTexture = textureAtlas.findRegion(size.getFlagAnimationName(), owner.getColor()).getTexture();
        return flagTexture;
    }

    private TextureRegion getNeutralHighlightTexture()
    {
        Texture neutralHighlight = textureAtlas.findRegion(size.getNeutralHighlight(), owner.getColor()).getTexture();
        return new TextureRegion(neutralHighlight);
    }

    private Texture getHighlightAnimationTexture()
    {
        Texture highlightTexture = textureAtlas.findRegion(size.getHighlightAnimationName(), owner.getColor())
            .getTexture();
        return highlightTexture;
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