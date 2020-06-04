package com.github.workinggames.castra.core.ai.voons;

import lombok.Getter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.Timepiece;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.github.workinggames.castra.core.actor.Army;
import com.github.workinggames.castra.core.actor.Battle;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.model.ArmySize;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.stage.GameConfiguration;
import com.github.workinggames.castra.core.stage.World;

public class GameInfo
{
    private static final int ESTIMATE_ERROR_MARGIN = 5;

    private final GameConfiguration gameConfiguration;
    private final Player aiPlayer;
    private final Timepiece timepiece;

    @Getter
    private final ArrayMap<Integer, SettlementInfo> settlementInfoBySettlementId = new ArrayMap<>();

    @Getter
    private int opponentArmyEstimate;

    @Getter
    private int soldiersAvailable;

    public GameInfo(World world, Player aiPlayer)
    {
        timepiece = world.getTimepiece();
        this.aiPlayer = aiPlayer;
        this.gameConfiguration = world.getGameConfiguration();
        opponentArmyEstimate = gameConfiguration.getStartingSoldiers();
        soldiersAvailable = gameConfiguration.getStartingSoldiers();

        for (Settlement settlement : world.getSettlements())
        {
            SettlementInfo settlementInfo = new SettlementInfo(settlement, aiPlayer, gameConfiguration);
            Array<Settlement> others = new Array<>(world.getSettlements());
            others.removeValue(settlement, true);
            for (Settlement otherSettlement : others)
            {
                float distance = world.getPaths().get(settlement, otherSettlement).getDistance();
                float seconds = distance / gameConfiguration.getArmyTravelSpeedInPixelPerSecond();
                settlementInfo.getSettlementDistancesInTicks().put(otherSettlement.getId(), seconds);
            }
            // initial strength is always known, even from opponent
            settlementInfo.setDefenders(settlementInfo.getSettlement().getSoldiers());
            settlementInfoBySettlementId.put(settlement.getId(), settlementInfo);
        }
    }

    public void soldierRemoved(Battle battle)
    {
        int targetSettlementId = battle.getArmy().getTarget().getId();
        SettlementInfo settlementInfo = settlementInfoBySettlementId.get(targetSettlementId);
        BattleInfo battleInfo = settlementInfo.getBattles().get(battle.getArmy().getId());

        // only opponent battles need to be re-estimated
        if (!battleInfo.getArmyInfos().first().getOwner().equals(aiPlayer))
        {
            battleInfo.setActualSoldiersAttacking(battleInfo.getActualSoldiersAttacking() + 1);
            if (battleInfo.getEstimatedSoldiers() < battleInfo.getActualSoldiersAttacking())
            {
                fixEstimationError(1, battleInfo.getArmyInfos());
            }
        }
    }

    public void defenderAdded(Battle battle)
    {
        int targetSettlementId = battle.getArmy().getTarget().getId();
        SettlementInfo settlementInfo = settlementInfoBySettlementId.get(targetSettlementId);
        settlementInfo.setDefenders(settlementInfo.getDefenders() + 1);
    }

    public void defenderRemoved(Battle battle)
    {
        int targetSettlementId = battle.getArmy().getTarget().getId();
        SettlementInfo settlementInfo = settlementInfoBySettlementId.get(targetSettlementId);
        settlementInfo.setDefenders(settlementInfo.getDefenders() - 1);
    }

    public void updateSettlementInfos()
    {
        opponentArmyEstimate = 0;
        soldiersAvailable = 0;

        for (SettlementInfo settlementInfo : settlementInfoBySettlementId.values().toArray())
        {
            if (settlementInfo.isOwnedByPlayer())
            {
                soldiersAvailable = soldiersAvailable + settlementInfo.getDefenders();
            }
            else if (!settlementInfo.getSettlement().getOwner().isNeutral())
            {
                opponentArmyEstimate = opponentArmyEstimate + settlementInfo.getDefenders();
            }

            int defenders = settlementInfo.getDefenders();
            // army processing
            for (ArmyInfo armyInfo : settlementInfo.getInboundArmies().values())
            {
                // reinforce
                if (armyInfo.getOwner().equals(settlementInfo.getSettlement().getOwner()))
                {
                    if (armyInfo.getOwner().equals(aiPlayer))
                    {
                        soldiersAvailable = soldiersAvailable + armyInfo.getSoldiers();
                    }
                    else
                    {
                        opponentArmyEstimate = opponentArmyEstimate + armyInfo.getSoldiers();
                    }
                }
                // attack and winning
                else if (defenders < armyInfo.getSoldiers())
                {
                    if (armyInfo.getOwner().equals(aiPlayer))
                    {
                        soldiersAvailable = soldiersAvailable + armyInfo.getSoldiers() - defenders;
                        if (!settlementInfo.getSettlement().getOwner().isNeutral())
                        {
                            opponentArmyEstimate = opponentArmyEstimate - defenders;
                        }
                    }
                    else
                    {
                        opponentArmyEstimate = opponentArmyEstimate + armyInfo.getSoldiers() - defenders;
                        if (!settlementInfo.getSettlement().getOwner().isNeutral())
                        {
                            soldiersAvailable = soldiersAvailable - defenders;
                        }
                    }
                }
            }
        }
    }

    public Settlement getSettlement(int settlementId)
    {
        return settlementInfoBySettlementId.get(settlementId).getSettlement();
    }

    void soldierSpawned(int settlementId)
    {
        SettlementInfo settlementInfo = settlementInfoBySettlementId.get(settlementId);
        settlementInfo.setDefenders(settlementInfo.getDefenders() + 1);
    }

    void armyCreated(Army army)
    {
        int soldierEstimate;
        SettlementInfo target = settlementInfoBySettlementId.get(army.getTarget().getId());
        SettlementInfo source = settlementInfoBySettlementId.get(army.getSource().getId());
        int sourceSoldiers = source.getDefenders();
        // there can't be negative number of soldiers and if an army was created, there has to be at least 1 soldier
        if (sourceSoldiers < 0)
        {
            sourceSoldiers = 1;
        }
        /*
         * if the army details are visible we know the soldier count, if the
         * settlement details are visible, we know exactly the soldier count after the army left,
         *  so basically it's the same and we can take the actual value.
         */
        if (army.getOwner().equals(aiPlayer) ||
            gameConfiguration.isOpponentArmyDetailsVisible() ||
            gameConfiguration.isOpponentSettlementDetailsVisible())
        {
            soldierEstimate = army.getSoldiers();
        }
        else
        {
            int defendingSoldiers = target.getDefenders();

            int minArmySize;
            int maxArmySize = sourceSoldiers;
            // let's get soldier min/max based on army size first
            if (army.getArmySize().equals(ArmySize.SMALL))
            {
                minArmySize = ArmySize.SMALL.getMinimumSoldiers();
                maxArmySize = Math.min(ArmySize.MEDIUM.getMinimumSoldiers() - 1, maxArmySize);
            }
            else if (army.getArmySize().equals(ArmySize.MEDIUM))
            {
                minArmySize = ArmySize.MEDIUM.getMinimumSoldiers();
                maxArmySize = Math.min(ArmySize.LARGE.getMinimumSoldiers() - 1, maxArmySize);
            }
            else
            {
                minArmySize = ArmySize.LARGE.getMinimumSoldiers();
            }

            // check if the source estimate was wrong
            if (sourceSoldiers < minArmySize)
            {
                sourceSoldiers = MathUtils.random(minArmySize, minArmySize + ESTIMATE_ERROR_MARGIN);
            }
            // can only happen for large settlements which had less than minimum defenders when setting max
            if (maxArmySize < minArmySize)
            {
                maxArmySize = sourceSoldiers;
            }

            /*
             * if this army is attacking a neutral settlement, the attacker knows exactly how many soldiers are
             * defending and it's more likely that he's attacking to win and not just weaken it.
             */
            if (army.getTarget().getOwner().isNeutral() &&
                maxArmySize > defendingSoldiers &&
                minArmySize < defendingSoldiers &&
                defendingSoldiers < sourceSoldiers)
            {
                minArmySize = defendingSoldiers;
            }

            soldierEstimate = MathUtils.random(minArmySize, maxArmySize);
            if (gameConfiguration.isDebugAI() && !army.getOwner().equals(aiPlayer))
            {
                addDebugLabel(army, aiPlayer, soldierEstimate);
            }
        }
        source.setDefenders(sourceSoldiers - soldierEstimate);

        ArmyInfo armyInfo = new ArmyInfo(army.getId(),
            soldierEstimate,
            army.getOwner(),
            army.getArmySize(),
            army.getPath().getDistance(),
            timepiece.getTime(),
            gameConfiguration.getArmyTravelSpeedInPixelPerSecond(),
            source.getSettlement().getId());
        target.getInboundArmies().put(army.getId(), armyInfo);
    }

    private void addDebugLabel(Army army, Player aiPlayer, int soldierEstimate)
    {
        BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/SoldierCount.fnt"),
            Gdx.files.internal("fonts/SoldierCount.png"),
            false);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font,
            aiPlayer.getColorSchema().getPlayerColor().getPrimaryColor());
        Label armyEstimate = new Label("" + soldierEstimate, labelStyle);
        armyEstimate.setPosition(army.getOriginX(), army.getOriginY());
        armyEstimate.setVisible(true);
        army.addActor(armyEstimate);
    }

    void battleJoined(Army army)
    {
        SettlementInfo settlementInfo = settlementInfoBySettlementId.get(army.getTarget().getId());
        ArmyInfo armyInfo;
        int armyId = army.getId();
        armyInfo = settlementInfo.getInboundArmies().get(armyId);
        settlementInfo.getInboundArmies().removeKey(armyId);

        Array<BattleInfo> battleInfos = settlementInfo.getBattles().values().toArray();
        for (BattleInfo battleInfo : battleInfos)
        {
            if (battleInfo.getArmyInfos().first().getOwner().equals(army.getOwner()))
            {
                battleInfo.setEstimatedSoldiers(battleInfo.getEstimatedSoldiers() + armyInfo.getSoldiers());
                battleInfo.getArmyInfos().add(armyInfo);
                break;
            }
        }
    }

    public void battleStarted(Battle battle)
    {
        int settlementId = battle.getArmy().getTarget().getId();
        SettlementInfo settlementInfo = settlementInfoBySettlementId.get(settlementId);

        ArmyInfo armyInfo;
        int armyId = battle.getArmy().getId();
        armyInfo = settlementInfo.getInboundArmies().get(armyId);
        settlementInfo.getInboundArmies().removeKey(armyId);
        BattleInfo battleInfo = new BattleInfo(Array.with(armyInfo),
            settlementInfo.getSettlement().getOwner(),
            timepiece.getTime());
        battleInfo.setEstimatedSoldiers(armyInfo.getSoldiers());
        settlementInfo.getBattles().put(armyId, battleInfo);
    }

    public void battleEnded(Battle battle)
    {
        SettlementInfo settlementInfo = settlementInfoBySettlementId.get(battle.getArmy().getTarget().getId());
        int armyId = battle.getArmy().getId();

        BattleInfo battleInfo = settlementInfo.getBattles().get(armyId);
        settlementInfo.getBattles().removeKey(armyId);

        // let's update the defender count if the settlement details are visible or the settlement is owned by the player or neutral
        int defenderEstimate;
        if (gameConfiguration.isOpponentSettlementDetailsVisible() ||
            settlementInfo.isOwnedByPlayer() ||
            settlementInfo.getSettlement().getOwner().isNeutral())
        {
            defenderEstimate = settlementInfo.getSettlement().getSoldiers();
            settlementInfo.setDefenders(defenderEstimate);
        }
        // there was a fight and we don't know the actual details, time for estimating
        else
        {
            // we only need to check opponent army estimates
            if (!battle.getArmy().getOwner().equals(aiPlayer))
            {
                int estimationError = battleInfo.getActualSoldiersAttacking() - battleInfo.getEstimatedSoldiers();
                // only battles which were shorter than expected remain to be fixed
                if (estimationError < 0)
                {
                    fixEstimationError(estimationError, battleInfo.getArmyInfos());
                }
            }
        }
    }

    private void fixEstimationError(int estimationError, Array<ArmyInfo> armyInfos)
    {
        // let's try fixing the estimation and proceed greedily
        int fixedSoldiers = 0;
        int errorAmount = Math.abs(estimationError);
        while (fixedSoldiers != errorAmount)
        {
            for (ArmyInfo armyInfo : armyInfos)
            {
                if (fixedSoldiers == errorAmount)
                {
                    break;
                }

                SettlementInfo settlementInfo = settlementInfoBySettlementId.get(armyInfo.getSourceSettlementId());
                int defenders = settlementInfo.getDefenders();

                if (estimationError < 0)
                {
                    // the soldier estimate was too high
                    int minimumSoldiers = armyInfo.getArmySize().getMinimumSoldiers();
                    int correction = Math.min(armyInfo.getSoldiers() - minimumSoldiers, errorAmount - fixedSoldiers);
                    fixedSoldiers = fixedSoldiers + correction;
                    defenders = defenders + correction;
                }
                else
                {
                    // the soldier estimate was too low
                    Integer maximumSoldiers = null;
                    if (armyInfo.getArmySize().equals(ArmySize.SMALL))
                    {
                        maximumSoldiers = ArmySize.MEDIUM.getMinimumSoldiers() - 1;
                    }
                    else if (armyInfo.getArmySize().equals(ArmySize.MEDIUM))
                    {
                        maximumSoldiers = ArmySize.LARGE.getMinimumSoldiers() - 1;
                    }
                    int correction = errorAmount;
                    if (maximumSoldiers != null)
                    {
                        correction = Math.min(maximumSoldiers - armyInfo.getSoldiers(), errorAmount - fixedSoldiers);
                    }
                    fixedSoldiers = fixedSoldiers + correction;
                    // let's keep defenders at least at 0
                    defenders = Math.max(0, defenders - correction);
                }
                settlementInfo.setDefenders(defenders);
            }
        }
    }
}