package com.github.workinggames.castra.core.ai.voons;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ArrayMap;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.model.SettlementSize;
import com.github.workinggames.castra.core.stage.GameConfiguration;

@Getter
public class SettlementInfo
{
    private final Settlement settlement;
    private final Player aiPlayer;
    private final Map<Integer, Float> settlementDistancesInTicks = new HashMap<>();
    private final ArrayMap<Integer, ArmyInfo> inboundArmies = new ArrayMap<>();
    private final ArrayMap<Integer, BattleInfo> battles = new ArrayMap<>();
    private Label defendersDebug;

    private int defenders;

    public SettlementInfo(Settlement settlement, Player aiPlayer, GameConfiguration gameConfiguration)
    {
        this.settlement = settlement;
        this.aiPlayer = aiPlayer;
        if (gameConfiguration.isDebug() && !gameConfiguration.isOpponentSettlementDetailsVisible())
        {
            addDebugLabel(settlement, aiPlayer);
        }
    }

    private void addDebugLabel(Settlement settlement, Player aiPlayer)
    {
        BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/SoldierCount.fnt"),
            Gdx.files.internal("fonts/SoldierCount.png"),
            false);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font,
            aiPlayer.getColorSchema().getPlayerColor().getPrimaryColor());
        defendersDebug = new Label("" + settlement.getSoldiers(), labelStyle);
        if (aiPlayer.isPlayerOne())
        {
            defendersDebug.setPosition(settlement.getOriginX() + getOffset(settlement.getSize()),
                settlement.getOriginY());
        }
        else
        {
            defendersDebug.setPosition(settlement.getOriginX(), settlement.getOriginY());
        }
        defendersDebug.setVisible(true);
        settlement.addActor(defendersDebug);
    }

    private float getOffset(SettlementSize size)
    {
        float offset = 125;
        if (size.equals(SettlementSize.LARGE))
        {
            offset = 145;
        }
        return offset;
    }

    public void setDefenders(int defenders)
    {
        this.defenders = defenders;
        if (defendersDebug != null)
        {
            defendersDebug.setText(defenders);
        }
    }

    float getBreakEvenInSeconds(float requiredSoldiers)
    {
        float secondsUntilReimbursed = requiredSoldiers * settlement.getSize().getSpawnIntervalInSeconds();
        float ownerScore;
        if (settlement.getOwner().isNeutral())
        {
            ownerScore = 1;
        }
        else
        {
            // we kill the opponent soldiers, so the break even is cheaper
            ownerScore = 0.5f;
        }
        return ownerScore * secondsUntilReimbursed;
    }

    int getAvailableSoldiers()
    {
        int available = settlement.getSoldiers();
        for (ArmyInfo armyInfo : inboundArmies.values())
        {
            if (armyInfo.getOwner().equals(aiPlayer))
            {
                available = available + armyInfo.getSoldiers();
            }
            else
            {
                available = available - armyInfo.getSoldiers();
            }
        }
        return MathUtils.ceil(settlement.getSoldiers());
    }

    float getSoldierSpawnUntilReached(float targetDistanceInTicks)
    {
        return targetDistanceInTicks / settlement.getSize().getSpawnIntervalInSeconds();
    }

    float getBattleSoldierSpawn(float soldiers, SettlementSize settlementSize, float battleProcessingInterval)
    {
        float minBattleTime = soldiers * battleProcessingInterval;

        return minBattleTime / settlementSize.getSpawnIntervalInSeconds();
    }

    public boolean isOwnedByPlayer()
    {
        return settlement.getOwner().equals(aiPlayer);
    }
}