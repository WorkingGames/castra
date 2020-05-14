package com.github.workinggames.castra.core.ai.voons;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.github.workinggames.castra.core.ai.Ai;

enum AiState implements State<Ai>
{
    ATTACK()
        {
            @Override
            public void enter(Ai entity)
            {
                entity.attack();
            }
        },
    WAIT();

    @Override
    public void enter(Ai entity)
    {
    }

    @Override
    public void update(Ai entity)
    {
    }

    @Override
    public void exit(Ai entity)
    {
    }

    @Override
    public boolean onMessage(Ai entity, Telegram telegram)
    {
        return true;
    }
}