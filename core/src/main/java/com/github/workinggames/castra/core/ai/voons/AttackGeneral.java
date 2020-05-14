package com.github.workinggames.castra.core.ai.voons;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.github.workinggames.castra.core.AttackSource;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.ai.AiUtils;
import com.github.workinggames.castra.core.model.Player;

public class AttackGeneral
{
    private final AiUtils aiUtils;
    private final Player aiPlayer;

    public AttackGeneral(AiUtils aiUtils, Player aiPlayer)
    {
        this.aiUtils = aiUtils;
        this.aiPlayer = aiPlayer;
    }

    public Array<Attack> getNeutralAttackOptions(
        ArrayMap<Integer, SettlementInfo> settlementInfoBySettlementId, int invest)
    {
        Array<Attack> attackOptions = new Array<>();
        Array<SettlementInfo> settlementInfos = settlementInfoBySettlementId.values().toArray();
        for (SettlementInfo settlementInfo : settlementInfos)
        {
            if (settlementInfo.getSettlement().getOwner().isNeutral() && !alreadyWinning(settlementInfo))
            {
                float requiredSoldiers = settlementInfo.getDefenders() + 1;

                Array<AttackSource> attackSources = getAttackSourcesWithoutSoldierSpawn(settlementInfoBySettlementId,
                    settlementInfo,
                    requiredSoldiers);

                if (requiredSoldiers <= invest)
                {
                    addAttackToOptions(attackOptions, settlementInfo, attackSources, requiredSoldiers);
                }
            }
        }
        return attackOptions;
    }

    public Array<Attack> getOpponentAttackOptions(
        ArrayMap<Integer, SettlementInfo> settlementInfoBySettlementId, int soldiersAvailable)
    {
        Array<Attack> attackOptions = new Array<>();
        Array<SettlementInfo> settlementInfos = settlementInfoBySettlementId.values().toArray();
        for (SettlementInfo settlementInfo : settlementInfos)
        {
            Player owner = settlementInfo.getSettlement().getOwner();
            if (!owner.equals(aiPlayer) && !owner.isNeutral() && !alreadyWinning(settlementInfo))
            {
                float requiredSoldiers = settlementInfo.getDefenders() +
                    1 +
                    settlementInfo.getBattleSoldierSpawn(settlementInfo.getDefenders(),
                        settlementInfo.getSettlement().getSize());
                Array<AttackSource> attackSources = getAttackSourcesWithSoldierSpawn(settlementInfoBySettlementId,
                    settlementInfo,
                    requiredSoldiers,
                    false);
                if (requiredSoldiers <= soldiersAvailable)
                {
                    addAttackToOptions(attackOptions, settlementInfo, attackSources, requiredSoldiers);
                }
            }
        }
        return attackOptions;
    }

    // this one needs more detailed timing, otherwise it might send the soldiers to early and battle neutrals and not the opponent
    public Array<Attack> getOpponentArmyOptions(
        ArrayMap<Integer, SettlementInfo> settlementInfoBySettlementId, int soldiersAvailable)
    {
        Array<Attack> attackOptions = new Array<>();
        Array<SettlementInfo> settlementInfos = settlementInfoBySettlementId.values().toArray();
        for (SettlementInfo settlementInfo : settlementInfos)
        {
            int inboundPlayerSoldiers = 0;
            int inboundOpponentSoldiers = 0;
            for (ArmyInfo armyInfo : settlementInfo.getInboundArmies().values())
            {
                if (armyInfo.getOwner().equals(aiPlayer))
                {
                    inboundPlayerSoldiers = inboundPlayerSoldiers + armyInfo.getSoldiers();
                }
                else
                {
                    inboundOpponentSoldiers = inboundOpponentSoldiers + armyInfo.getSoldiers();
                }
            }

            if (inboundOpponentSoldiers > 0 &&
                !alreadyWinning(settlementInfo, inboundPlayerSoldiers, inboundOpponentSoldiers))
            {
                float requiredSoldiers;
                Array<AttackSource> attackSources;

                if (settlementInfo.getSettlement().getOwner().isNeutral())
                {
                    // to make it simple for now, first army to arrive will battle all defenders
                    if (settlementInfo.getInboundArmies().firstValue().getOwner().equals(aiPlayer))
                    {
                        requiredSoldiers = Math.abs(Math.abs(settlementInfo.getDefenders() - inboundPlayerSoldiers) -
                            inboundOpponentSoldiers);
                    }
                    else
                    {
                        requiredSoldiers = Math.abs(Math.abs(settlementInfo.getDefenders() - inboundOpponentSoldiers) -
                            inboundPlayerSoldiers);
                    }
                    // add one for safety
                    requiredSoldiers++;
                    attackSources = getAttackSourcesWithoutSoldierSpawn(settlementInfoBySettlementId,
                        settlementInfo,
                        requiredSoldiers);
                }
                else
                {
                    if (settlementInfo.isOwnedByPlayer())
                    {
                        requiredSoldiers = settlementInfo.getDefenders() + inboundPlayerSoldiers -
                            inboundOpponentSoldiers;
                    }
                    else
                    {
                        requiredSoldiers = settlementInfo.getDefenders() + inboundOpponentSoldiers -
                            inboundPlayerSoldiers;
                    }
                    // add one for safety
                    requiredSoldiers++;
                    attackSources = getAttackSourcesWithSoldierSpawn(settlementInfoBySettlementId,
                        settlementInfo,
                        requiredSoldiers,
                        settlementInfo.isOwnedByPlayer());
                }

                if (requiredSoldiers <= soldiersAvailable)
                {
                    addAttackToOptions(attackOptions, settlementInfo, attackSources, requiredSoldiers);
                }
            }
        }
        return attackOptions;
    }

    private Array<AttackSource> getAttackSourcesWithoutSoldierSpawn(
        ArrayMap<Integer, SettlementInfo> settlementInfoBySettlementId,
        SettlementInfo settlementInfo,
        float requiredSoldiers)
    {
        Array<AttackSource> attackSources = new Array<>();
        int requestedSoldiers = 0;
        for (Settlement source : getSourcesSortedByDistance(settlementInfo))
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
        return attackSources;
    }

    private Array<AttackSource> getAttackSourcesWithSoldierSpawn(
        ArrayMap<Integer, SettlementInfo> settlementInfoBySettlementId,
        SettlementInfo settlementInfo,
        float requiredSoldiers,
        boolean defending)
    {
        Array<AttackSource> attackSources = new Array<>();
        int requestedSoldiers = 0;
        for (Settlement source : getSourcesSortedByDistance(settlementInfo))
        {
            float
                soldierSpawnUntilReached
                = settlementInfo.getSoldierSpawnUntilReached(settlementInfo.getSettlementDistancesInTicks()
                .get(source.getId()));
            int availableSoldiers = settlementInfoBySettlementId.get(source.getId()).getAvailableSoldiers();
            float relativeRequiredSoldiers;
            if (defending)
            {
                relativeRequiredSoldiers = requiredSoldiers - soldierSpawnUntilReached;
            }
            else
            {
                relativeRequiredSoldiers = requiredSoldiers + soldierSpawnUntilReached;
            }
            if (availableSoldiers >= relativeRequiredSoldiers)
            {
                attackSources.add(new AttackSource(source.getId(),
                    MathUtils.ceil(relativeRequiredSoldiers - requestedSoldiers)));
                break;
            }
            else
            {
                attackSources.add(new AttackSource(source.getId(), availableSoldiers));
                requestedSoldiers = requestedSoldiers + availableSoldiers;
            }
        }
        return attackSources;
    }

    private boolean alreadyWinning(
        SettlementInfo settlementInfo, int inboundPlayerSoldiers, int inboundOpponentSoldiers)
    {
        // this should be improved later on, by including battle times and army travel times etc.
        int battlingPlayerSoldiers = 0;
        int battlingOpponentSoldiers = 0;
        for (BattleInfo battleInfo : settlementInfo.getBattles().values())
        {
            if (battleInfo.getArmyInfos().first().getOwner().equals(aiPlayer))
            {
                for (ArmyInfo armyInfo : battleInfo.getArmyInfos())
                {
                    battlingPlayerSoldiers = battlingPlayerSoldiers + armyInfo.getSoldiers();
                }
            }
            else
            {
                for (ArmyInfo armyInfo : battleInfo.getArmyInfos())
                {
                    battlingOpponentSoldiers = battlingOpponentSoldiers + armyInfo.getSoldiers();
                }
            }
        }
        return inboundPlayerSoldiers + battlingPlayerSoldiers > inboundOpponentSoldiers + battlingOpponentSoldiers;
    }

    private boolean alreadyWinning(SettlementInfo settlementInfo)
    {
        // this should be improved later on, by including battle times and army travel times etc.
        int inboundPlayerSoldiers = 0;
        int inboundOpponentSoldiers = 0;
        for (ArmyInfo armyInfo : settlementInfo.getInboundArmies().values())
        {
            if (armyInfo.getOwner().equals(aiPlayer))
            {
                inboundPlayerSoldiers = inboundPlayerSoldiers + armyInfo.getSoldiers();
            }
            else
            {
                inboundOpponentSoldiers = inboundOpponentSoldiers + armyInfo.getSoldiers();
            }
        }
        return alreadyWinning(settlementInfo, inboundPlayerSoldiers, inboundOpponentSoldiers);
    }

    private Array<Settlement> getSourcesSortedByDistance(SettlementInfo target)
    {
        Array<Settlement> sources = aiUtils.getOwnedSettlements(aiPlayer);
        sources.removeValue(target.getSettlement(), true);
        sources.sort(new SettlementDistanceComparator(target));
        return sources;
    }

    private void addAttackToOptions(
        Array<Attack> attackOptions,
        SettlementInfo settlementInfo,
        Array<AttackSource> attackSources,
        float requiredSoldiers)
    {
        Attack attack = new Attack(attackSources, settlementInfo.getSettlement().getId());
        float breakEvenInSeconds = settlementInfo.getBreakEvenInSeconds(requiredSoldiers);
        attack.setBreakEvenInSeconds(breakEvenInSeconds);
        attackOptions.add(attack);
    }
}