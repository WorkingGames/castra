package com.github.workinggames.castra.core.ai;

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
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.stage.World;

public class Quux
{
    private final boolean isOpponentArmyDetailsVisible;
    private final boolean isOpponentSettlementDetailsVisible;
    private final ArrayMap<Integer, SettlementFoo> fooMap = new ArrayMap<>();
    private final Player aiPlayer;
    private final Timepiece timepiece;
    private final AiUtils aiUtils;

    @Getter
    private int opponentArmyEstimate = 100;

    @Getter
    private int soldiersAvailable = 100;

    public Quux(World world, Player aiPlayer)
    {
        isOpponentArmyDetailsVisible = world.getGameConfiguration().isOpponentArmyDetailsVisible();
        isOpponentSettlementDetailsVisible = world.getGameConfiguration().isOpponentSettlementDetailsVisible();
        timepiece = world.getTimepiece();
        this.aiPlayer = aiPlayer;
        aiUtils = new AiUtils(world);

        for (Settlement settlement : world.getSettlements())
        {
            SettlementFoo foo = new SettlementFoo(settlement, aiPlayer);
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
            fooMap.put(settlement.getId(), foo);
        }
    }

    public void updateSettlementFoo()
    {
        opponentArmyEstimate = 0;
        soldiersAvailable = 0;

        for (SettlementFoo settlementFoo : fooMap.values().toArray())
        {
            if (settlementFoo.isOwnedByPlayer())
            {
                soldiersAvailable = soldiersAvailable + settlementFoo.getDefenders();
            }
            else if (!settlementFoo.getSettlement().getOwner().isNeutral())
            {
                opponentArmyEstimate = opponentArmyEstimate + settlementFoo.getDefenders();
            }
            // armies and battle forecast
            int defenders = settlementFoo.getDefenders();
            for (ArmyFoo armyFoo : settlementFoo.getInboundArmies().values())
            {
                // reinforce
                if (armyFoo.getOwner().equals(settlementFoo.getSettlement().getOwner()))
                {
                    if (armyFoo.getOwner().equals(aiPlayer))
                    {
                        soldiersAvailable = soldiersAvailable + armyFoo.getSoldiers();
                    }
                    else
                    {
                        opponentArmyEstimate = opponentArmyEstimate + armyFoo.getSoldiers();
                    }
                }
                // attack and winning
                else if (defenders < armyFoo.getSoldiers())
                {
                    if (armyFoo.getOwner().equals(aiPlayer))
                    {
                        soldiersAvailable = soldiersAvailable + armyFoo.getSoldiers() - defenders;
                        if (!settlementFoo.getSettlement().getOwner().isNeutral())
                        {
                            opponentArmyEstimate = opponentArmyEstimate - defenders;
                        }
                    }
                    else
                    {
                        opponentArmyEstimate = opponentArmyEstimate + armyFoo.getSoldiers() - defenders;
                        if (!settlementFoo.getSettlement().getOwner().isNeutral())
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
        return fooMap.get(settlementId).getSettlement();
    }

    public Array<Attack> getAttackOptions(int invest)
    {
        Array<Attack> attackOptions = new Array<>();
        Array<Settlement> sources = aiUtils.getOwnedSettlements(aiPlayer);
        Array<SettlementFoo> settlementFoos = fooMap.values().toArray();
        for (SettlementFoo foo : settlementFoos)
        {
            // only attack targets
            if (!foo.getSettlement().getOwner().equals(aiPlayer) && !alreadyWinning(foo))
            {
                int requestedSoldiers = 0;
                Array<AttackSource> attackSources = new Array<>();

                float requiredSoldiers = foo.getDefenders();
                if (!foo.getSettlement().getOwner().isNeutral())
                {
                    requiredSoldiers = requiredSoldiers +
                        foo.getBattleSoldierSpawn(foo.getDefenders(), foo.getSettlement().getSize());
                }
                sources.sort(new SettlementDistanceComparator(foo));
                // Settlements with least distance first
                for (Settlement source : sources)
                {
                    float soldierSpawnUntilReached = foo.getSoldierSpawnUntilReached(foo.getSettlementDistancesInTicks()
                        .get(source.getId()));
                    int availableSoldiers = fooMap.get(source.getId()).getAvailableSoldiers();
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

    private boolean alreadyWinning(SettlementFoo foo)
    {
        // this should be improved later on, by including battle times and army travel times etc.
        int inboundPlayerSoldiers = 0;
        int inboundOpponentSoldiers = 0;
        int battlingPlayerSoldiers = 0;
        int battlingOpponentSoldiers = 0;
        for (ArmyFoo armyfoo : foo.getInboundArmies().values())
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
        for (BattleFoo battlefoo : foo.getBattles().values())
        {
            if (battlefoo.getArmyFoos().first().getOwner().equals(aiPlayer))
            {
                for (ArmyFoo armyFoo : battlefoo.getArmyFoos())
                {
                    battlingPlayerSoldiers = battlingPlayerSoldiers + armyFoo.getSoldiers();
                }
            }
            else
            {
                for (ArmyFoo armyFoo : battlefoo.getArmyFoos())
                {
                    battlingOpponentSoldiers = battlingOpponentSoldiers + armyFoo.getSoldiers();
                }
            }
        }
        return inboundPlayerSoldiers + battlingPlayerSoldiers > inboundOpponentSoldiers + battlingOpponentSoldiers;
    }

    void soldierSpawned(int settlementId)
    {
        SettlementFoo foo = fooMap.get(settlementId);
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
        SettlementFoo sourceFoo = fooMap.get(army.getSource().getId());
        sourceFoo.setDefenders(sourceFoo.getDefenders() - soldierEstimate);

        ArmyFoo armyFoo = new ArmyFoo(army.getId(),
            soldierEstimate,
            army.getOwner(),
            army.getArmySize(),
            army.getPath().getDistance(),
            timepiece.getTime());
        SettlementFoo targetFoo = fooMap.get(army.getTarget().getId());
        targetFoo.getInboundArmies().put(army.getId(), armyFoo);
    }

    void battleJoined(Army army)
    {
        SettlementFoo settlementFoo = fooMap.get(army.getTarget().getId());
        ArmyFoo armyFoo;
        int armyId = army.getId();
        armyFoo = settlementFoo.getInboundArmies().get(armyId);
        settlementFoo.getInboundArmies().removeKey(armyId);

        Array<BattleFoo> battleFoos = settlementFoo.getBattles().values().toArray();
        for (BattleFoo battleFoo : battleFoos)
        {
            if (battleFoo.getArmyFoos().first().getOwner().equals(army.getOwner()))
            {
                battleFoo.getArmyFoos().add(armyFoo);
                break;
            }
        }
    }

    public void battleStarted(Battle battle)
    {
        SettlementFoo settlementFoo = fooMap.get(battle.getArmy().getTarget().getId());

        ArmyFoo armyFoo;
        int armyId = battle.getArmy().getId();
        armyFoo = settlementFoo.getInboundArmies().get(armyId);
        settlementFoo.getInboundArmies().removeKey(armyId);
        BattleFoo battleFoo = new BattleFoo(Array.with(armyFoo), timepiece.getTime());
        settlementFoo.getBattles().put(armyId, battleFoo);
    }

    public void battleEnded(Battle battle)
    {
        SettlementFoo settlementFoo = fooMap.get(battle.getArmy().getTarget().getId());
        int armyId = battle.getArmy().getId();

        BattleFoo battleFoo = settlementFoo.getBattles().get(armyId);
        settlementFoo.getBattles().removeKey(armyId);
        if (isOpponentSettlementDetailsVisible || settlementFoo.isOwnedByPlayer())
        {
            settlementFoo.setDefenders(settlementFoo.getSettlement().getSoldiers());
        }
        else
        {
            // TODO estimate the new defender count, maybe use the actual time the battle lasted to get a closer estimate
        }
    }
}