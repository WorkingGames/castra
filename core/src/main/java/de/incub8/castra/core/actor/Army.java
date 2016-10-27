package de.incub8.castra.core.actor;

import lombok.Getter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import de.incub8.castra.core.Castra;
import de.incub8.castra.core.font.FontProvider;
import de.incub8.castra.core.model.ArmySize;
import de.incub8.castra.core.model.Player;
import de.incub8.castra.core.texture.ColorizingTextureAtlasAdapter;

public class Army extends Group
{
    private static final float SPEED = 200;

    private final Array<GridPoint2> path;
    private final Image image;
    private final Label label;

    @Getter
    private final Player owner;

    @Getter
    private final Settlement target;

    @Getter
    private int soldiers;

    private int pathPosition;

    public Army(
        int soldiers,
        Player owner,
        Settlement target,
        Array<GridPoint2> path,
        TextureAtlas textureAtlas,
        FontProvider fontProvider)
    {
        this.soldiers = soldiers;
        this.owner = owner;
        this.target = target;
        this.path = path;

        pathPosition = 0;

        ArmySize size = ArmySize.bySoldierCount(soldiers);

        image = createImage(size, new ColorizingTextureAtlasAdapter(textureAtlas));

        setSize(image.getWidth(), image.getHeight());

        this.label = createLabel(fontProvider);
    }

    private Image createImage(ArmySize size, ColorizingTextureAtlasAdapter textureAtlas)
    {
        TextureAtlas.AtlasRegion atlasRegion = textureAtlas.findRegion(size.getTextureName(), owner.getColor());
        Image result = new Image(atlasRegion.getTexture());
        addActor(result);
        return result;
    }

    private Label createLabel(FontProvider fontProvider)
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle(fontProvider.getFont(), Color.WHITE);
        Label result = new Label(String.valueOf(soldiers), labelStyle);
        result.setX(image.getWidth() / 2);
        addActor(result);
        return result;
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);
        pathPosition = Math.min(pathPosition + (int) (SPEED * delta), path.size - 1);
        GridPoint2 position = path.get(pathPosition);
        setPosition(position.x, position.y);
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
        updateLabel();
    }

    public void addSoldiers(int count)
    {
        this.soldiers += count;
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