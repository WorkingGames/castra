package com.github.workinggames.castra.core.ai.voons;

import lombok.Getter;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.github.workinggames.castra.core.ai.Ai;
import com.github.workinggames.castra.core.ai.AiUtils;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.stage.World;

public class BasicAi implements Ai, Telegraph
{
    private static final int FIRST_ACTION_TIME = 1;
    private static final int MAXIMUM_SOLDIER_INVEST_IN_NEUTRAL = 5;
    private static final float MINIMUM_IDLE_TIME = 0.5f;
    private static final float MAXIMUM_IDLE_TIME = 1.5f;

    @Getter
    private final StateMachine<Ai, AiState> stateMachine;
    private final World world;
    private final MessagingHandler messagingHandler;
    private final GameInfo gameInfo;
    private final AttackGeneral attackGeneral;

    private float nextActionTime;

    public BasicAi(World world, Player aiPlayer)
    {
        this.world = world;
        stateMachine = new DefaultStateMachine<>(this, AiState.WAIT);
        gameInfo = new GameInfo(world, aiPlayer);
        messagingHandler = new MessagingHandler(this, gameInfo);
        attackGeneral = new AttackGeneral(new AiUtils(world), aiPlayer, world.getGameConfiguration());
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
            stateMachine.changeState(AiState.ATTACK);
            nextActionTime = time + MathUtils.random(MINIMUM_IDLE_TIME, MAXIMUM_IDLE_TIME);
        }
    }

    public void attack()
    {
        int opponentArmyEstimate = gameInfo.getOpponentArmyEstimate();
        int soldiersAvailable = gameInfo.getSoldiersAvailable();
        /* don't be the player which looses all his soldiers to neutral armies, only invest the defined value if
         * there is a neutral settlements with less soldiers in reach.
         */
        int invest = soldiersAvailable - opponentArmyEstimate + MAXIMUM_SOLDIER_INVEST_IN_NEUTRAL;
        ArrayMap<Integer, SettlementInfo> settlementInfos = gameInfo.getSettlementInfoBySettlementId();
        Array<Attack> attackOptions = attackGeneral.getOpponentAttackOptions(settlementInfos, soldiersAvailable);
        if (invest > 0)
        {
            attackOptions.addAll(attackGeneral.getNeutralAttackOptions(settlementInfos, invest));
        }
        attackOptions.sort();

        if (!attackOptions.isEmpty())
        {
            Attack attack = attackOptions.first();
            createArmies(attack);
        }
        else
        {
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
}