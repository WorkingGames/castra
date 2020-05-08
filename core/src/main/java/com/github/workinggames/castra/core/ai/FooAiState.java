package com.github.workinggames.castra.core.ai;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

enum FooAiState implements State<FooAi>
{
    ATTACK()
        {
            @Override
            public void enter(FooAi entity)
            {
                entity.attack();
            }
        },
    DEFEND()
        {
            @Override
            public void enter(FooAi entity)
            {
                entity.defend();
            }
        },
    WAIT;

    @Override
    public void enter(FooAi entity)
    {
    }

    @Override
    public void update(FooAi entity)
    {
    }

    @Override
    public void exit(FooAi entity)
    {
    }

    @Override
    public boolean onMessage(FooAi entity, Telegram telegram)
    {
        return false;
    }
}