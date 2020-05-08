package com.github.workinggames.castra.core.ai;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

enum FooAiState implements State<FooAli>
{
    ATTACK()
        {
            @Override
            public void enter(FooAli entity)
            {
                entity.attack();
            }
        },
    DEFEND()
        {
            @Override
            public void enter(FooAli entity)
            {
                entity.defend();
            }
        },
    WAIT;

    @Override
    public void enter(FooAli entity)
    {
    }

    @Override
    public void update(FooAli entity)
    {
    }

    @Override
    public void exit(FooAli entity)
    {
    }

    @Override
    public boolean onMessage(FooAli entity, Telegram telegram)
    {
        return false;
    }
}