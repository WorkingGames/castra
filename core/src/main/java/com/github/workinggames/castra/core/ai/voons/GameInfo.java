package com.github.workinggames.castra.core.ai.voons;

import lombok.Getter;

import com.badlogic.gdx.ai.Timepiece;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.github.workinggames.castra.core.action.MoveAlongAction;
import com.github.workinggames.castra.core.actor.Army;
import com.github.workinggames.castra.core.actor.Battle;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.stage.World;

public class GameInfo
{
    private final boolean isOpponentArmyDetailsVisible;
    private final boolean isOpponentSettlementDetailsVisible;
    private final Player aiPlayer;
    private final Timepiece timepiece;

    @Getter
    private final ArrayMap<Integer, SettlementInfo> settlementInfoBySettlementId = new ArrayMap<>();

    @Getter
    private int opponentArmyEstimate = 100;

    @Getter
    private int soldiersAvailable = 100;

    public GameInfo(World world, Player aiPlayer)
    {
        isOpponentArmyDetailsVisible = world.getGameConfiguration().isOpponentArmyDetailsVisible();
        isOpponentSettlementDetailsVisible = world.getGameConfiguration().isOpponentSettlementDetailsVisible();
        timepiece = world.getTimepiece();
        this.aiPlayer = aiPlayer;

        for (Settlement settlement : world.getSettlements())
        {
            SettlementInfo settlementInfo = new SettlementInfo(settlement, aiPlayer);
            Array<Settlement> others = new Array<>(world.getSettlements());
            others.removeValue(settlement, true);
            for (Settlement otherSettlement : others)
            {
                float distance = world.getPaths().get(settlement, otherSettlement).getDistance();
                float seconds = distance / MoveAlongAction.PIXEL_PER_SECOND;
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
        int soldierEstimate = 0;
        if (army.getOwner().equals(aiPlayer) || isOpponentArmyDetailsVisible)
        {
            soldierEstimate = army.getSoldiers();
        }
        else
        {
            // TODO guess the soldier count based on army size, target soldiers and source soldiers
        }
        SettlementInfo source = settlementInfoBySettlementId.get(army.getSource().getId());
        source.setDefenders(source.getDefenders() - soldierEstimate);

        ArmyInfo armyInfo = new ArmyInfo(army.getId(),
            soldierEstimate,
            army.getOwner(),
            army.getArmySize(),
            army.getPath().getDistance(),
            timepiece.getTime());
        SettlementInfo target = settlementInfoBySettlementId.get(army.getTarget().getId());
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
        BattleInfo battleInfo = new BattleInfo(Array.with(armyInfo), timepiece.getTime());
        settlementInfo.getBattles().put(armyId, battleInfo);
    }

    public void battleEnded(Battle battle)
    {
        SettlementInfo settlementInfo = settlementInfoBySettlementId.get(battle.getArmy().getTarget().getId());
        int armyId = battle.getArmy().getId();

        BattleInfo battleInfo = settlementInfo.getBattles().get(armyId);
        settlementInfo.getBattles().removeKey(armyId);
        if (isOpponentSettlementDetailsVisible || settlementInfo.isOwnedByPlayer())
        {
            settlementInfo.setDefenders(settlementInfo.getSettlement().getSoldiers());
        }
        else
        {
            // TODO estimate the new defender count, maybe use the actual time the battle lasted to get a closer estimate
        }
    }
}