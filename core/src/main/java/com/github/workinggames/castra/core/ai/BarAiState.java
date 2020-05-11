package com.github.workinggames.castra.core.ai;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

enum BarAiState implements State<BarAi>
{
    ATTACK()
        {
            @Override
            public void enter(BarAi entity)
            {
                entity.attack();
            }
        },
    WAIT();

    @Override
    public void enter(BarAi entity)
    {
    }

    @Override
    public void update(BarAi entity)
    {
    }

    @Override
    public void exit(BarAi entity)
    {
    }

    @Override
    public boolean onMessage(BarAi entity, Telegram telegram)
    {
        return true;
    }
}