package com.github.workinggames.castra.core.ai.voons;

import lombok.Getter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.Timepiece;
import com.badlogic.gdx.math.MathUtils;
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
    private static final int ESTIMATE_ERROR_MARGIN = 10;

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
            SettlementInfo settlementInfo = new SettlementInfo(settlement, aiPlayer);
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
            // armies and battle forecast
            int defenders = settlementInfo.getDefenders();
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
            int sourceSoldiers = source.getDefenders();

            int minArmySize;
            int maxArmySize;
            if (army.getArmySize().equals(ArmySize.SMALL))
            {
                minArmySize = ArmySize.SMALL.getMinimumSoldiers();
                maxArmySize = ArmySize.MEDIUM.getMinimumSoldiers() - 1;
            }
            else if (army.getArmySize().equals(ArmySize.MEDIUM))
            {
                minArmySize = ArmySize.MEDIUM.getMinimumSoldiers();
                maxArmySize = ArmySize.LARGE.getMinimumSoldiers() - 1;
            }
            else
            {
                minArmySize = ArmySize.LARGE.getMinimumSoldiers();
                if (sourceSoldiers > minArmySize)
                {
                    maxArmySize = sourceSoldiers;
                }
                else
                {
                    // seems like the source estimate was wrong, let's update the estimate with an error margin
                    maxArmySize = MathUtils.random(minArmySize, minArmySize + ESTIMATE_ERROR_MARGIN);
                    source.setDefenders(maxArmySize);
                    Gdx.app.log("AI",
                        "updated settlement defender estimate: " +
                            maxArmySize +
                            " actual defenders: " +
                            army.getSource().getSoldiers());
                }
            }

            /*
             * if this army is attacking a neutral settlement, the attacker knows exactly how many soldiers are
             * defending and it's more likely that he's attacking to win and not just weaken it.
             */
            if (army.getTarget().getOwner().isNeutral() &&
                maxArmySize > defendingSoldiers &&
                minArmySize < defendingSoldiers)
            {
                minArmySize = defendingSoldiers;
            }
            soldierEstimate = MathUtils.random(minArmySize, maxArmySize);
            Gdx.app.log("AI", "army estimate: " + soldierEstimate + " actual size: " + army.getSoldiers());
        }
        source.setDefenders(source.getDefenders() - soldierEstimate);

        ArmyInfo armyInfo = new ArmyInfo(army.getId(),
            soldierEstimate,
            army.getOwner(),
            army.getArmySize(),
            army.getPath().getDistance(),
            timepiece.getTime(),
            gameConfiguration.getArmyTravelSpeedInPixelPerSecond());
        target.getInboundArmies().put(army.getId(), armyInfo);
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
                battleInfo.getArmyInfos().add(armyInfo);
                break;
            }
        }
    }

    public void battleStarted(Battle battle)
    {
        SettlementInfo settlementInfo = settlementInfoBySettlementId.get(battle.getArmy().getTarget().getId());

        ArmyInfo armyInfo;
        int armyId = battle.getArmy().getId();
        armyInfo = settlementInfo.getInboundArmies().get(armyId);
        settlementInfo.getInboundArmies().removeKey(armyId);
        BattleInfo battleInfo = new BattleInfo(Array.with(armyInfo),
            settlementInfo.getSettlement().getOwner(),
            timepiece.getTime());
        settlementInfo.getBattles().put(armyId, battleInfo);
    }

    public void battleEnded(Battle battle)
    {
        SettlementInfo settlementInfo = settlementInfoBySettlementId.get(battle.getArmy().getTarget().getId());
        int armyId = battle.getArmy().getId();

        BattleInfo battleInfo = settlementInfo.getBattles().get(armyId);
        settlementInfo.getBattles().removeKey(armyId);

        int defenderEstimate;
        // we know the actual defender count if the settlement details are visible or the settlement is owned by the player or neutral
        if (gameConfiguration.isOpponentSettlementDetailsVisible() ||
            settlementInfo.isOwnedByPlayer() ||
            settlementInfo.getSettlement().getOwner().isNeutral())
        {
            defenderEstimate = settlementInfo.getSettlement().getSoldiers();
        }
        // there was a fight and we don't know the actual details, time for estimating
        else
        {
            int defenders = settlementInfo.getDefenders();
            int attackers = 0;
            for (ArmyInfo armyInfo : battleInfo.getArmyInfos())
            {
                attackers = attackers + armyInfo.getSoldiers();
            }

            // reinforce
            if (battleInfo.getDefender().equals(settlementInfo.getSettlement().getOwner()))
            {
                defenderEstimate = defenders + attackers;
            }
            // owner changed
            else if (!battleInfo.getDefender().equals(settlementInfo.getSettlement().getOwner()))
            {
                defenderEstimate = attackers - defenders;
            }
            // attacker lost
            else if (attackers < defenders)
            {
                defenderEstimate = defenders - attackers;
            }
            else
            {
                // obviously a previous estimate was wrong, otherwise the settlement had been taken over.
                // so let's randomize the remaining defender estimate
                defenderEstimate = MathUtils.random(0, ESTIMATE_ERROR_MARGIN);
            }
        }
        settlementInfo.setDefenders(defenderEstimate);
        Gdx.app.log("AI",
            "defender estimate: " + defenderEstimate + " actual size: " + settlementInfo.getSettlement().getSoldiers());
    }
}