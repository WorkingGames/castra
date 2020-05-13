package com.github.workinggames.castra.core.ai.voons;

import lombok.Getter;

import com.badlogic.gdx.ai.Timepiece;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.github.workinggames.castra.core.AttackSource;
import com.github.workinggames.castra.core.action.MoveAlongAction;
import com.github.workinggames.castra.core.actor.Army;
import com.github.workinggames.castra.core.actor.Battle;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.ai.AiUtils;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.stage.World;

public class GameInfo
{
    private final boolean isOpponentArmyDetailsVisible;
    private final boolean isOpponentSettlementDetailsVisible;
    private final ArrayMap<Integer, SettlementInfo> settlementInfoBySettlementId = new ArrayMap<>();
    private final Player aiPlayer;
    private final Timepiece timepiece;
    private final AiUtils aiUtils;

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
        aiUtils = new AiUtils(world);

        for (Settlement settlement : world.getSettlements())
        {
            SettlementInfo foo = new SettlementInfo(settlement, aiPlayer);
            Array<Settlement> others = new Array<>(world.getSettlements());
            others.removeValue(settlement, true);
            for (Settlement otherSettlement : others)
            {
                float distance = world.getPaths().get(settlement, otherSettlement).getDistance();
                float seconds = distance / MoveAlongAction.PIXEL_PER_SECOND;
                foo.getSettlementDistancesInTicks().put(otherSettlement.getId(), seconds);
            }
            // initial strength is always known, even from opponent
            foo.setDefenders(foo.getSettlement().getSoldiers());
            settlementInfoBySettlementId.put(settlement.getId(), foo);
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

    public Array<Attack> getNeutralAttackOptions(int invest)
    {
        Array<Attack> attackOptions = new Array<>();
        Array<Settlement> sources = aiUtils.getOwnedSettlements(aiPlayer);
        Array<SettlementInfo> settlementFoos = settlementInfoBySettlementId.values().toArray();
        for (SettlementInfo foo : settlementFoos)
        {
            if (foo.getSettlement().getOwner().isNeutral() && !alreadyWinning(foo))
            {
                int requestedSoldiers = 0;
                Array<AttackSource> attackSources = new Array<>();
                float requiredSoldiers = foo.getDefenders() + 1;
                sources.sort(new SettlementDistanceComparator(foo));
                // Settlements with least distance first
                for (Settlement source : sources)
                {
                    int availableSoldiers = settlementInfoBySettlementId.get(source.getId()).getAvailableSoldiers();
                    if (availableSoldiers >= requiredSoldiers)
                    {
                        attackSources.add(new AttackSource(source.getId(),
                            MathUtils.ceil(requiredSoldiers - requestedSoldiers)));
                        break;
                    }
                    else
                    {
                        attackSources.add(new AttackSource(source.getId(), availableSoldiers));
                        requestedSoldiers = requestedSoldiers + availableSoldiers;
                    }
                }
                if (requiredSoldiers <= invest)
                {
                    Attack attack = new Attack(attackSources, foo.getSettlement().getId());
                    float breakEvenInSeconds = foo.getBreakEvenInSeconds(requiredSoldiers);
                    attack.setBreakEvenInSeconds(breakEvenInSeconds);
                    attackOptions.add(attack);
                }
            }
        }
        return attackOptions;
    }

    public Array<Attack> getOpponentAttackOptions()
    {
        Array<Attack> attackOptions = new Array<>();
        Array<Settlement> sources = aiUtils.getOwnedSettlements(aiPlayer);
        Array<SettlementInfo> settlementFoos = settlementInfoBySettlementId.values().toArray();
        for (SettlementInfo foo : settlementFoos)
        {
            Player owner = foo.getSettlement().getOwner();
            if (!owner.equals(aiPlayer) && !owner.isNeutral() && !alreadyWinning(foo))
            {
                int requestedSoldiers = 0;
                Array<AttackSource> attackSources = new Array<>();

                float requiredSoldiers = foo.getDefenders() +
                    1 +
                    foo.getBattleSoldierSpawn(foo.getDefenders(), foo.getSettlement().getSize());
                sources.sort(new SettlementDistanceComparator(foo));
                // Settlements with least distance first
                for (Settlement source : sources)
                {
                    float soldierSpawnUntilReached = foo.getSoldierSpawnUntilReached(foo.getSettlementDistancesInTicks()
                        .get(source.getId()));
                    int availableSoldiers = settlementInfoBySettlementId.get(source.getId()).getAvailableSoldiers();
                    if (availableSoldiers >= requiredSoldiers + soldierSpawnUntilReached)
                    {
                        attackSources.add(new AttackSource(source.getId(),
                            MathUtils.ceil(requiredSoldiers - requestedSoldiers + soldierSpawnUntilReached)));
                        break;
                    }
                    else
                    {
                        attackSources.add(new AttackSource(source.getId(), availableSoldiers));
                        requiredSoldiers = requiredSoldiers + soldierSpawnUntilReached;
                        requestedSoldiers = requestedSoldiers + availableSoldiers;
                    }
                }
                if (requiredSoldiers <= soldiersAvailable)
                {
                    Attack attack = new Attack(attackSources, foo.getSettlement().getId());
                    float breakEvenInSeconds = foo.getBreakEvenInSeconds(requiredSoldiers);
                    attack.setBreakEvenInSeconds(breakEvenInSeconds);
                    attackOptions.add(attack);
                }
            }
        }
        return attackOptions;
    }

    private boolean alreadyWinning(SettlementInfo foo)
    {
        // this should be improved later on, by including battle times and army travel times etc.
        int inboundPlayerSoldiers = 0;
        int inboundOpponentSoldiers = 0;
        int battlingPlayerSoldiers = 0;
        int battlingOpponentSoldiers = 0;
        for (ArmyInfo armyfoo : foo.getInboundArmies().values())
        {
            if (armyfoo.getOwner().equals(aiPlayer))
            {
                inboundPlayerSoldiers = inboundPlayerSoldiers + armyfoo.getSoldiers();
            }
            else
            {
                inboundOpponentSoldiers = inboundOpponentSoldiers + armyfoo.getSoldiers();
            }
        }
        for (BattleInfo battlefoo : foo.getBattles().values())
        {
            if (battlefoo.getArmyFoos().first().getOwner().equals(aiPlayer))
            {
                for (ArmyInfo armyInfo : battlefoo.getArmyFoos())
                {
                    battlingPlayerSoldiers = battlingPlayerSoldiers + armyInfo.getSoldiers();
                }
            }
            else
            {
                for (ArmyInfo armyInfo : battlefoo.getArmyFoos())
                {
                    battlingOpponentSoldiers = battlingOpponentSoldiers + armyInfo.getSoldiers();
                }
            }
        }
        return inboundPlayerSoldiers + battlingPlayerSoldiers > inboundOpponentSoldiers + battlingOpponentSoldiers;
    }

    void soldierSpawned(int settlementId)
    {
        SettlementInfo foo = settlementInfoBySettlementId.get(settlementId);
        foo.setDefenders(foo.getDefenders() + 1);
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
        SettlementInfo sourceFoo = settlementInfoBySettlementId.get(army.getSource().getId());
        sourceFoo.setDefenders(sourceFoo.getDefenders() - soldierEstimate);

        ArmyInfo armyInfo = new ArmyInfo(army.getId(),
            soldierEstimate,
            army.getOwner(),
            army.getArmySize(),
            army.getPath().getDistance(),
            timepiece.getTime());
        SettlementInfo targetFoo = settlementInfoBySettlementId.get(army.getTarget().getId());
        targetFoo.getInboundArmies().put(army.getId(), armyInfo);
    }

    void battleJoined(Army army)
    {
        SettlementInfo settlementInfo = settlementInfoBySettlementId.get(army.getTarget().getId());
        ArmyInfo armyInfo;
        int armyId = army.getId();
        armyInfo = settlementInfo.getInboundArmies().get(armyId);
        settlementInfo.getInboundArmies().removeKey(armyId);

        Array<BattleInfo> battleFoos = settlementInfo.getBattles().values().toArray();
        for (BattleInfo battleInfo : battleFoos)
        {
            if (battleInfo.getArmyFoos().first().getOwner().equals(army.getOwner()))
            {
                battleInfo.getArmyFoos().add(armyInfo);
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