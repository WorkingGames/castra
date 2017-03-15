package de.incub8.castra.core.actor;

import lombok.Getter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.incub8.castra.core.Castra;
import de.incub8.castra.core.action.MoveAlongAction;
import de.incub8.castra.core.font.FontProvider;
import de.incub8.castra.core.model.ArmySize;
import de.incub8.castra.core.model.Player;
import de.incub8.castra.core.pathfinding.LinePath;
import de.incub8.castra.core.texture.AnimationUtil;
import de.incub8.castra.core.texture.ColorizingTextureAtlasAdapter;

public class Army extends Group
{
    private final AnimatedImage image;
    private final Label label;
    private final ColorizingTextureAtlasAdapter textureAtlas;
    private final AnimationUtil animationUtil;

    @Getter
    private final Player owner;

    @Getter
    private final Settlement target;

    @Getter
    private int soldiers;

    public Army(
        int soldiers,
        Player owner,
        Settlement target,
        LinePath path,
        TextureAtlas textureAtlas,
        FontProvider fontProvider)
    {
        this.soldiers = soldiers;
        this.owner = owner;
        this.target = target;
        this.textureAtlas = new ColorizingTextureAtlasAdapter(textureAtlas);
        this.animationUtil = new AnimationUtil();

        ArmySize size = ArmySize.bySoldierCount(soldiers);

        image = createAnimatedImage(getAnimation(getArmyTexture(size)));

        setSize(image.getWidth(), image.getHeight());

        this.label = createLabel(fontProvider);

        addAction(MoveAlongAction.obtain(path));
    }

    private AnimatedImage createAnimatedImage(Animation animation)
    {
        AnimatedImage animatedImage = new AnimatedImage(animation);
        addActor(animatedImage);
        return animatedImage;
    }

    private Animation getAnimation(Texture texture)
    {
        Animation animation = animationUtil.createAnimation(texture, 4, 1, 0.05f);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        return animation;
    }

    private Texture getArmyTexture(ArmySize size)
    {
        Texture texture = textureAtlas.findRegion(size.getTextureName(), owner.getColor()).getTexture();
        return texture;
    }

    private Label createLabel(FontProvider fontProvider)
    {
        Label.LabelStyle labelStyle = new Label.LabelStyle(fontProvider.getSoldierCountFont(), Color.WHITE);
        Label result = new Label(String.valueOf(soldiers), labelStyle);
        result.setX(image.getWidth() / 2);
        addActor(result);
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