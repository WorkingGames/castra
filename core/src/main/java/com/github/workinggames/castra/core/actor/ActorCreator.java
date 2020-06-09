package com.github.workinggames.castra.core.actor;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.workinggames.castra.core.font.FontProvider;
import com.github.workinggames.castra.core.model.ArmySize;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.model.SettlementSize;
import com.github.workinggames.castra.core.pathfinding.LinePath;
import com.github.workinggames.castra.core.stage.GameConfiguration;
import com.github.workinggames.castra.core.texture.AnimationUtil;
import com.github.workinggames.castra.core.texture.ColorizingTextureAtlasAdapter;

public class ActorCreator
{
    private static final float SLOW_ANIMATION_SPEED = 0.2f;
    private static final float FAST_ANIMATION_SPEED = 0.05f;
    private static final int FLAG_ANIMATION_ROWS = 1;
    private static final int FLAG_ANIMATION_COLUMNS = 6;
    private static final int HIGHLIGHT_ANIMATION_ROWS = 1;
    private static final int HIGHLIGHT_ANIMATION_COLUMNS = 6;
    private static final int ARMY_ANIMATION_ROWS = 4;
    private static final int ARMY_ANIMATION_COLUMNS = 1;
    private static final int BATTLE_ANIMATION_ROWS = 2;
    private static final int BATTLE_ANIMATION_COLUMNS = 6;

    private final GameConfiguration gameConfiguration;
    private final ColorizingTextureAtlasAdapter colorizingTextureAtlasAdapter;
    private final FontProvider fontProvider;
    private final AnimationUtil animationUtil;

    private final Map<Player, SettlementImage> smallSettlementImageMap = new HashMap<>();
    private final Map<Player, SettlementImage> mediumSettlementImageMap = new HashMap<>();
    private final Map<Player, SettlementImage> largeSettlementImageMap = new HashMap<>();
    private final Map<Player, AnimatedImage> smallArmyMap = new HashMap<>();
    private final Map<Player, AnimatedImage> mediumArmyMap = new HashMap<>();
    private final Map<Player, AnimatedImage> largeArmyMap = new HashMap<>();
    private final Map<Player, AnimatedImage> battleMap = new HashMap<>();

    private int settlementId = 0;
    private int armyId = 0;

    public ActorCreator(GameConfiguration gameConfiguration, TextureAtlas textureAtlas, FontProvider fontProvider)
    {
        this.gameConfiguration = gameConfiguration;
        this.fontProvider = fontProvider;
        this.animationUtil = new AnimationUtil();
        colorizingTextureAtlasAdapter = new ColorizingTextureAtlasAdapter(textureAtlas);

        createSettlementImages(GameConfiguration.NEUTRAL_PLAYER);
        createSettlementImages(gameConfiguration.getPlayer1());
        createSettlementImages(gameConfiguration.getPlayer2());
        createArmyImages(gameConfiguration.getPlayer1());
        createArmyImages(gameConfiguration.getPlayer2());
        createBattleImage(gameConfiguration.getPlayer1());
        createBattleImage(gameConfiguration.getPlayer2());
    }

    private void createSettlementImages(Player player)
    {
        createSettlementImages(smallSettlementImageMap, SettlementSize.SMALL, player);
        createSettlementImages(mediumSettlementImageMap, SettlementSize.MEDIUM, player);
        createSettlementImages(largeSettlementImageMap, SettlementSize.LARGE, player);
    }

    private void createSettlementImages(
        Map<Player, SettlementImage> settlementImageMap, SettlementSize size, Player player)
    {
        Image settlement = new Image(getCastleTexture(size, player));
        AnimatedImage highlight;
        if (player.isNeutral())
        {
            highlight = new AnimatedImage(getNeutralHighlightTexture(size, player));
        }
        else
        {
            highlight = new AnimatedImage(getHighlightAnimation(size, player));
        }

        AnimatedImage flags = new AnimatedImage(getFlagAnimation(size, player));
        SettlementImage settlementImage = new SettlementImage(settlement, highlight, flags);
        settlementImageMap.put(player, settlementImage);
    }

    private void createArmyImages(Player player)
    {
        smallArmyMap.put(player, new AnimatedImage(getArmyAnimation(ArmySize.SMALL, player)));
        mediumArmyMap.put(player, new AnimatedImage(getArmyAnimation(ArmySize.MEDIUM, player)));
        largeArmyMap.put(player, new AnimatedImage(getArmyAnimation(ArmySize.LARGE, player)));
    }

    private void createBattleImage(Player player)
    {
        Texture texture = colorizingTextureAtlasAdapter.findRegion("battle1", player.getColorSchema()).getTexture();
        Animation<TextureRegion> battleAnimation = getAnimation(texture,
            BATTLE_ANIMATION_ROWS,
            BATTLE_ANIMATION_COLUMNS,
            SLOW_ANIMATION_SPEED);
        battleMap.put(player, new AnimatedImage(battleAnimation));
    }

    public Settlement createSettlement(SettlementSize size, int x, int y, int soldiers, Player player)
    {
        Map<Player, SettlementImage> settlementImageMap = null;
        switch (size)
        {
            case SMALL:
                settlementImageMap = smallSettlementImageMap;
                break;
            case MEDIUM:
                settlementImageMap = mediumSettlementImageMap;
                break;
            case LARGE:
                settlementImageMap = largeSettlementImageMap;
                break;
        }

        Settlement settlement = new Settlement(settlementId,
            size,
            x,
            y,
            soldiers,
            player,
            fontProvider.getSoldierCount(),
            gameConfiguration.isOpponentSettlementDetailsVisible(),
            settlementImageMap);
        settlementId++;
        return settlement;
    }

    private TextureRegion getCastleTexture(SettlementSize size, Player player)
    {
        return new TextureRegion(colorizingTextureAtlasAdapter.findRegion(size.getTextureName(),
            player.getColorSchema()));
    }

    private Animation<TextureRegion> getAnimation(
        Texture texture, int animationRows, int animationColumns, float animationSpeed)
    {
        Animation<TextureRegion> animation = animationUtil.createAnimation(texture,
            animationRows,
            animationColumns,
            animationSpeed);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        return animation;
    }

    private Animation<TextureRegion> getFlagAnimation(SettlementSize size, Player player)
    {
        Texture texture = colorizingTextureAtlasAdapter.findRegion(size.getFlagAnimationName(), player.getColorSchema())
            .getTexture();
        return getAnimation(texture, FLAG_ANIMATION_ROWS, FLAG_ANIMATION_COLUMNS, SLOW_ANIMATION_SPEED);
    }

    private Animation<TextureRegion> getHighlightAnimation(SettlementSize size, Player player)
    {
        Texture texture = colorizingTextureAtlasAdapter.findRegion(size.getHighlightAnimationName(),
            player.getColorSchema()).getTexture();
        return getAnimation(texture, HIGHLIGHT_ANIMATION_ROWS, HIGHLIGHT_ANIMATION_COLUMNS, SLOW_ANIMATION_SPEED);
    }

    private TextureRegion getNeutralHighlightTexture(SettlementSize size, Player player)
    {
        return new TextureRegion(colorizingTextureAtlasAdapter.findRegion(size.getNeutralHighlight(),
            player.getColorSchema()));
    }

    public Army createArmy(Settlement source, Settlement target, LinePath path, int soldiers)
    {
        AnimatedImage armyImage = null;
        ArmySize armySize = ArmySize.bySoldierCount(soldiers);
        switch (armySize)
        {
            case SMALL:
                armyImage = smallArmyMap.get(source.getOwner());
                break;
            case MEDIUM:
                armyImage = mediumArmyMap.get(source.getOwner());
                break;
            case LARGE:
                armyImage = largeArmyMap.get(source.getOwner());
                break;
        }
        Army army = new Army(armyId,
            soldiers,
            source.getOwner(),
            source,
            target,
            path,
            fontProvider.getSoldierCount(),
            armyImage,
            gameConfiguration.isOpponentArmyDetailsVisible(),
            gameConfiguration.getGameSpeed().getArmySpeed());
        armyId++;
        return army;
    }

    private Animation<TextureRegion> getArmyAnimation(ArmySize size, Player player)
    {
        Texture texture = getArmyTexture(size, player);
        return getAnimation(texture, ARMY_ANIMATION_ROWS, ARMY_ANIMATION_COLUMNS, FAST_ANIMATION_SPEED);
    }

    private Texture getArmyTexture(ArmySize size, Player player)
    {
        return colorizingTextureAtlasAdapter.findRegion(size.getTextureName(), player.getColorSchema()).getTexture();
    }

    public Battle createBattle(Army attacker)
    {
        return new Battle(attacker, battleMap.get(attacker.getOwner()));
    }
}