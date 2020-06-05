package com.github.workinggames.castra.core.ai.voons;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.ai.AiUtils;
import com.github.workinggames.castra.core.model.ArmySize;
import com.github.workinggames.castra.core.model.Player;

public class FakeGeneral
{
    private static final float MINIMUM_TICK_DISTANCE_FOR_FAKING = 2.5f;
    private static final int ESTIMATION_ERROR_MARGIN = 3;

    private final AiUtils aiUtils;
    private final Player aiPlayer;

    // The FakeGeneral only makes sense if the army details and settlement details are hidden
    public FakeGeneral(AiUtils aiUtils, Player aiPlayer)
    {
        this.aiUtils = aiUtils;
        this.aiPlayer = aiPlayer;
    }

    public Array<Attack> getNeutralAttackOptions(ArrayMap<Integer, SettlementInfo> settlementInfoBySettlementId)
    {
        Array<Attack> attackOptions = new Array<>();
        Array<SettlementInfo> settlementInfos = settlementInfoBySettlementId.values().toArray();
        for (SettlementInfo settlementInfo : settlementInfos)
        {
            Settlement settlement = settlementInfo.getSettlement();
            if (settlement.getOwner().isNeutral() &&
                settlementInfo.getDefenders() < ArmySize.MEDIUM.getMinimumSoldiers() - 1 &&
                noInboundArmies(settlementInfo))
            {
                AttackSource attackSource = getAttackSourceWithNiceDistance(settlementInfo);
                addAttackToOptions(attackOptions, settlementInfo, attackSource);
            }
        }
        return attackOptions;
    }

    private boolean noInboundArmies(SettlementInfo settlementInfo)
    {
        return settlementInfo.getInboundArmies().values().toArray().isEmpty();
    }

    public Array<Attack> getOpponentAttackOptions(ArrayMap<Integer, SettlementInfo> settlementInfoBySettlementId)
    {
        Array<Attack> attackOptions = new Array<>();
        Array<SettlementInfo> settlementInfos = settlementInfoBySettlementId.values().toArray();
        for (SettlementInfo settlementInfo : settlementInfos)
        {
            Player owner = settlementInfo.getSettlement().getOwner();
            if (!owner.equals(aiPlayer) &&
                !owner.isNeutral() &&
                // let's add some error margin on top, as the defenders are only an estimate
                settlementInfo.getDefenders() < ArmySize.MEDIUM.getMinimumSoldiers() - 1 - ESTIMATION_ERROR_MARGIN &&
                !noInboundArmies(settlementInfo))
            {
                AttackSource attackSource = getAttackSourceWithNiceDistance(settlementInfo);
                addAttackToOptions(attackOptions, settlementInfo, attackSource);
            }
        }
        return attackOptions;
    }

    private AttackSource getAttackSourceWithNiceDistance(SettlementInfo targetInfo)
    {
        AttackSource attackSource = null;
        for (Settlement source : getSources(targetInfo))
        {
            if (source.getSoldiers() >= 1 &&
                targetInfo.getSettlementDistancesInTicks().get(source.getId()) > MINIMUM_TICK_DISTANCE_FOR_FAKING)
            {
                attackSource = new AttackSource(source.getId(), 1);
            }
        }
        return attackSource;
    }

    private Array<Settlement> getSources(SettlementInfo target)
    {
        Array<Settlement> sources = aiUtils.getOwnedSettlements(aiPlayer);
        sources.removeValue(target.getSettlement(), true);
        sources.shuffle();
        return sources;
    }

    private void addAttackToOptions(Array<Attack> attackOptions, SettlementInfo targetInfo, AttackSource attackSource)
    {
        if (attackSource != null)
        {
            Array<AttackSource> attackSources = new Array<>();
            attackSources.add(attackSource);
            Attack attack = new Attack(attackSources, targetInfo.getSettlement().getId());
            float breakEvenInSeconds = targetInfo.getBreakEvenInSeconds(1);
            attack.setBreakEvenInSeconds(breakEvenInSeconds);
            attackOptions.add(attack);
        }
    }
}