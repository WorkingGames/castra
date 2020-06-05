package com.github.workinggames.castra.core.ai.simple;

import lombok.Getter;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.ai.Ai;
import com.github.workinggames.castra.core.ai.AiUtils;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.stage.World;

public class SimpleAi implements Ai
{
    private static final float MINIMUM_IDLE_TIME = 1;
    private static final float MAXIMUM_IDLE_TIME = 4;
    private static final int MINIMUM_TROOP_PERCENTAGE = 20;
    private static final int MAXIMUM_TROOP_PERCENTAGE = 80;

    @Getter
    private final StateMachine<SimpleAi, SimpleAiState> stateMachine;
    private final World world;
    private final AiUtils aiUtils;
    private float nextActionTime;
    private final Player player;

    public SimpleAi(World world, Player aiPlayer)
    {
        this.world = world;
        this.player = aiPlayer;
        aiUtils = new AiUtils(world);
        stateMachine = new DefaultStateMachine<>(this, SimpleAiState.ATTACK);
        nextActionTime = 1;
    }

    public void update()
    {
        float time = world.getTimepiece().getTime();
        if (time >= nextActionTime)
        {
            stateMachine.changeState(SimpleAiState.ATTACK);
            nextActionTime = MathUtils.random(time + MINIMUM_IDLE_TIME, time + MAXIMUM_IDLE_TIME);
        }
    }

    public void attack()
    {
        Settlement origin = randomOrigin();
        Settlement destination = randomDestination();
        if (origin != null && destination != null)
        {
            player.setSendTroopPercentage(MathUtils.random(MINIMUM_TROOP_PERCENTAGE, MAXIMUM_TROOP_PERCENTAGE));
            world.createArmy(origin, destination);
        }
        stateMachine.changeState(SimpleAiState.WAIT);
    }

    @Override
    public void fake()
    {
        // not supported
    }

    private Settlement randomOrigin()
    {
        Settlement origin = null;
        Array<Settlement> owned = aiUtils.getOwnedSettlements(player);
        if (owned.size > 0)
        {
            origin = owned.get(MathUtils.random(0, owned.size - 1));
        }
        return origin;
    }

    private Settlement randomDestination()
    {
        Settlement destination = null;
        Array<Settlement> targets = new Array<>();
        targets.addAll(aiUtils.getOpponentSettlements(player));
        targets.addAll(aiUtils.getNeutralSettlements());
        if (targets.size > 0)
        {
            destination = targets.get(MathUtils.random(0, targets.size - 1));
        }
        return destination;
    }
}