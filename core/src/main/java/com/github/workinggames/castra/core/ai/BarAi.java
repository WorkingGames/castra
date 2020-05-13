package com.github.workinggames.castra.core.ai;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.Array;
import com.github.workinggames.castra.core.AttackSource;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.stage.World;

@Slf4j
public class BarAi implements Ai, Telegraph
{
    private static final int FIRST_ACTION_TIME = 1;
    private static final int MAXIMUM_SOLDIER_INVEST = 5;
    private static final float ACTION_SPEED = 0.5f;

    @Getter
    private final StateMachine<BarAi, BarAiState> stateMachine;
    private final World world;
    private final MessagingHandler messagingHandler;
    private final Quux quux;
    private float nextActionTime;

    public BarAi(World world, Player aiPlayer)
    {
        this.world = world;
        stateMachine = new DefaultStateMachine<>(this, BarAiState.WAIT);
        quux = new Quux(world, aiPlayer);
        messagingHandler = new MessagingHandler(this, quux);
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
        if (time >= nextActionTime)
        {
            quux.updateSettlementFoo();
            stateMachine.changeState(BarAiState.ATTACK);
            nextActionTime = nextActionTime + ACTION_SPEED;
        }
    }

    public void attack()
    {
        /* don't be the player which looses all his soldiers to neutral armies, only invest the defined value if
         * there is a neutral settlements with less soldiers in reach.
         */
        int opponentArmyEstimate = quux.getOpponentArmyEstimate();
        int soldiersAvailable = quux.getSoldiersAvailable();
        log.info("OpponentArmyEstimate: {}, SoldiersAvailable: {}", opponentArmyEstimate, soldiersAvailable);
        // TODO this will sadly lead to no action at all if the opponent is stronger, maybe switch to defending
        int invest = soldiersAvailable - opponentArmyEstimate + MAXIMUM_SOLDIER_INVEST;
        if (invest > 0)
        {
            Array<Attack> attackOptions = quux.getAttackOptions(invest);
            attackOptions.sort();
            if (!attackOptions.isEmpty())
            {
                log.info(attackOptions.toString(", "));
                Attack attack = attackOptions.first();
                createArmies(attack);
            }
            else
            {
                stateMachine.changeState(BarAiState.WAIT);
            }
        }
        else
        {
            stateMachine.changeState(BarAiState.WAIT);
        }
    }

    private void createArmies(Attack attack)
    {
        for (AttackSource source : attack.getAttackSources())
        {
            world.createArmy(quux.getSettlement(source.getSettlementId()),
                quux.getSettlement(attack.getTargetSettlementId()),
                source.getSoldiers());
        }
    }
}