package com.github.workinggames.castra.core.ai.voons;

import lombok.Getter;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.ai.Ai;
import com.github.workinggames.castra.core.ai.AiUtils;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.stage.World;

public class FrankyAi implements Ai, Telegraph
{
    private static final int FIRST_ACTION_TIME = 1;
    private static final int MAXIMUM_SOLDIER_INVEST_IN_NEUTRAL = 30;
    private static final int MINIMUM_TURNS_WITHOUT_ATTACKING_NEUTRAL = 15;
    private static final int MAXIMUM_TURNS_WITHOUT_ATTACKING_NEUTRAL = 30;
    private static final float MINIMUM_IDLE_TIME = 0.5f;
    private static final float MAXIMUM_IDLE_TIME = 1.5f;
    private static final float FAKING_CHANCE = 0.33f;

    @Getter
    private final StateMachine<Ai, AiState> stateMachine;
    private final World world;
    private final MessagingHandler messagingHandler;
    private final GameInfo gameInfo;
    private final AttackGeneral attackGeneral;
    private final FakeGeneral fakeGeneral;
    private final AiUtils aiUtils;
    private final Player aiPlayer;

    private float nextActionTime;
    private boolean attacked;
    private final int turnsBeforeLoosingTemper = MathUtils.random(MINIMUM_TURNS_WITHOUT_ATTACKING_NEUTRAL,
        MAXIMUM_TURNS_WITHOUT_ATTACKING_NEUTRAL);
    private int turnsWithoutAttack = 0;

    public FrankyAi(World world, Player aiPlayer)
    {
        this.world = world;
        this.aiPlayer = aiPlayer;
        stateMachine = new DefaultStateMachine<>(this, AiState.WAIT);
        gameInfo = new GameInfo(world, aiPlayer);
        messagingHandler = new MessagingHandler(this, gameInfo);
        aiUtils = new AiUtils(world);
        attackGeneral = new AttackGeneral(aiUtils, aiPlayer, world.getGameConfiguration());
        fakeGeneral = new FakeGeneral(aiUtils, aiPlayer);
        nextActionTime = FIRST_ACTION_TIME;
    }

    @Override
    public boolean handleMessage(Telegram msg)
    {
        return messagingHandler.handleMessage(msg);
    }

    public void update()
    {
        float time = world.getTimepiece().getTime();

        gameInfo.updateSettlementInfos();

        if (time >= nextActionTime)
        {
            // only fake when it makes sense (army details hidden) and by chance
            boolean faking = !world.getGameConfiguration().isOpponentArmyDetailsVisible() &&
                MathUtils.randomBoolean(FAKING_CHANCE);

            // To prevent a stare off contest when facing the same Ai, it will loose its temper and will once attack a neutral settlement to break the tie
            if (!attacked && turnsWithoutAttack >= turnsBeforeLoosingTemper)
            {
                if (faking)
                {
                    fakeNeutral();
                }
                else
                {
                    attackNeutral();
                }
            }
            else
            {
                // it only makes sense to fake attacking an opponent if he has more than one settlement
                if (faking && aiUtils.getOpponentSettlements(aiPlayer).size > 1)
                {
                    stateMachine.changeState(AiState.FAKE);
                }
                else
                {
                    stateMachine.changeState(AiState.ATTACK);
                }
            }
            nextActionTime = time + MathUtils.random(MINIMUM_IDLE_TIME, MAXIMUM_IDLE_TIME);
        }
    }

    public void attack()
    {
        ArrayMap<Integer, SettlementInfo> settlementInfos = gameInfo.getSettlementInfoBySettlementId();
        // This Ai won't bother with neutral Settlements and only attack the opponent and run after his armies
        int soldiersAvailable = gameInfo.getSoldiersAvailable();
        Array<Attack> attackOptions = attackGeneral.getOpponentAttackOptions(settlementInfos, soldiersAvailable);
        attackOptions.addAll(attackGeneral.getOpponentBattleOptions(settlementInfos, soldiersAvailable));
        attackOptions.addAll(attackGeneral.getOpponentArmyOptions(settlementInfos, soldiersAvailable));
        attackOptions.sort();

        if (!attackOptions.isEmpty())
        {
            Attack attack = attackOptions.first();
            // finish the opponent if he has only one settlement and is clearly outnumbered
            if (attackOptions.size == 1 && soldiersAvailable > gameInfo.getOpponentArmyEstimate() + 30)
            {
                finishOpponent(attack);
            }
            else
            {
                createArmies(attack);
            }
            attacked = true;
        }
        else
        {
            turnsWithoutAttack++;
            stateMachine.changeState(AiState.WAIT);
        }
    }

    public void attackNeutral()
    {
        Array<Attack> attackOptions = attackGeneral.getNeutralAttackOptions(gameInfo.getSettlementInfoBySettlementId(),
            MAXIMUM_SOLDIER_INVEST_IN_NEUTRAL);
        attackOptions.sort();

        if (!attackOptions.isEmpty())
        {
            Attack attack = attackOptions.first();
            createArmies(attack);
            attacked = true;
        }
    }

    public void fakeNeutral()
    {
        Array<Attack> attackOptions = fakeGeneral.getNeutralAttackOptions(gameInfo.getSettlementInfoBySettlementId());
        attackOptions.shuffle();

        if (!attackOptions.isEmpty())
        {
            Attack attack = attackOptions.first();
            createArmies(attack);
            attacked = true;
        }
    }

    public void fake()
    {
        Array<Attack> attackOptions = fakeGeneral.getOpponentAttackOptions(gameInfo.getSettlementInfoBySettlementId());
        attackOptions.shuffle();

        if (!attackOptions.isEmpty())
        {
            Attack attack = attackOptions.first();
            createArmies(attack);
            attacked = true;
            // faking only makes sense if the real attack is deployed as fast as allowed
            nextActionTime = world.getTimepiece().getTime() + MINIMUM_IDLE_TIME;
            stateMachine.changeState(AiState.ATTACK);
        }
        else
        {
            turnsWithoutAttack++;
            stateMachine.changeState(AiState.WAIT);
        }
    }

    private void createArmies(Attack attack)
    {
        for (AttackSource source : attack.getAttackSources())
        {
            world.createArmy(gameInfo.getSettlement(source.getSettlementId()),
                gameInfo.getSettlement(attack.getTargetSettlementId()),
                source.getSoldiers());
        }
    }

    private void finishOpponent(Attack attack)
    {
        for (AttackSource source : attack.getAttackSources())
        {
            Settlement sourceSettlement = gameInfo.getSettlement(source.getSettlementId());
            int dispatchSoldiers = source.getSoldiers();
            if (sourceSettlement.getSoldiers() > dispatchSoldiers)
            {
                dispatchSoldiers = sourceSettlement.getSoldiers();
            }
            world.createArmy(sourceSettlement,
                gameInfo.getSettlement(attack.getTargetSettlementId()),
                dispatchSoldiers);
        }
    }
}