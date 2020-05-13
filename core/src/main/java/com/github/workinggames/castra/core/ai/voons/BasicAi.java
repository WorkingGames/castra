package com.github.workinggames.castra.core.ai.voons;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.Array;
import com.github.workinggames.castra.core.AttackSource;
import com.github.workinggames.castra.core.ai.Ai;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.stage.World;

@Slf4j
public class BasicAi implements Ai, Telegraph
{
    private static final int FIRST_ACTION_TIME = 1;
    private static final int MAXIMUM_SOLDIER_INVEST_IN_NEUTRAL = 5;
    private static final float ACTION_SPEED = 0.5f;

    @Getter
    private final StateMachine<BasicAi, BasicAiState> stateMachine;
    private final World world;
    private final MessagingHandler messagingHandler;
    private final GameInfo gameInfo;
    private float nextActionTime;

    public BasicAi(World world, Player aiPlayer)
    {
        this.world = world;
        stateMachine = new DefaultStateMachine<>(this, BasicAiState.WAIT);
        gameInfo = new GameInfo(world, aiPlayer);
        messagingHandler = new MessagingHandler(this, gameInfo);
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
            stateMachine.changeState(BasicAiState.ATTACK);
            nextActionTime = nextActionTime + ACTION_SPEED;
        }
    }

    public void attack()
    {
        /* don't be the player which looses all his soldiers to neutral armies, only invest the defined value if
         * there is a neutral settlements with less soldiers in reach.
         */
        int opponentArmyEstimate = gameInfo.getOpponentArmyEstimate();
        int soldiersAvailable = gameInfo.getSoldiersAvailable();
        log.info("OpponentArmyEstimate: {}, SoldiersAvailable: {}", opponentArmyEstimate, soldiersAvailable);
        int invest = soldiersAvailable - opponentArmyEstimate + MAXIMUM_SOLDIER_INVEST_IN_NEUTRAL;
        Array<Attack> attackOptions = gameInfo.getOpponentAttackOptions();
        if (invest > 0)
        {
            attackOptions.addAll(gameInfo.getNeutralAttackOptions(invest));
        }
        attackOptions.sort();

        if (!attackOptions.isEmpty())
        {
            log.info(attackOptions.toString(", "));
            Attack attack = attackOptions.first();
            createArmies(attack);
        }
        else
        {
            stateMachine.changeState(BasicAiState.WAIT);
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