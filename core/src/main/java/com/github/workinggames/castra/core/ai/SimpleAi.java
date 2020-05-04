package com.github.workinggames.castra.core.ai;

import lombok.Getter;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.stage.World;

public class SimpleAi
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

    public SimpleAi(World world)
    {
        this.world = world;
        aiUtils = new AiUtils(world);
        stateMachine = new DefaultStateMachine<>(this, SimpleAiState.ATTACK);
        nextActionTime = 0;
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
            world.getAiPlayer()
                .setSendTroopPercentage(MathUtils.random(MINIMUM_TROOP_PERCENTAGE, MAXIMUM_TROOP_PERCENTAGE));
            world.createArmy(origin, destination);
        }
        stateMachine.changeState(SimpleAiState.WAIT);
    }

    private Settlement randomOrigin()
    {
        Settlement origin = null;
        Array<Settlement> owned = aiUtils.getOwnedSettlements();
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
        targets.addAll(aiUtils.getPlayerSettlements());
        targets.addAll(aiUtils.getNeutralSettlements());
        if (targets.size > 0)
        {
            destination = targets.get(MathUtils.random(0, targets.size - 1));
        }
        return destination;
    }
}