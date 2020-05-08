package com.github.workinggames.castra.core.ai;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

enum SimpleAiState implements State<SimpleAli>
{
    ATTACK()
        {
            @Override
            public void enter(SimpleAli entity)
            {
                entity.attack();
            }
        },
    WAIT;

    @Override
    public void enter(SimpleAli entity)
    {
    }

    @Override
    public void update(SimpleAli entity)
    {
    }

    @Override
    public void exit(SimpleAli entity)
    {
    }

    @Override
    public boolean onMessage(SimpleAli entity, Telegram telegram)
    {
        return false;
    }
}