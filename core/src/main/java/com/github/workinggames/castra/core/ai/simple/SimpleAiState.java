package com.github.workinggames.castra.core.ai.simple;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

enum SimpleAiState implements State<SimpleAi>
{
    ATTACK()
        {
            @Override
            public void enter(SimpleAi entity)
            {
                entity.attack();
            }
        },
    WAIT;

    @Override
    public void enter(SimpleAi entity)
    {
    }

    @Override
    public void update(SimpleAi entity)
    {
    }

    @Override
    public void exit(SimpleAi entity)
    {
    }

    @Override
    public boolean onMessage(SimpleAi entity, Telegram telegram)
    {
        return false;
    }
}