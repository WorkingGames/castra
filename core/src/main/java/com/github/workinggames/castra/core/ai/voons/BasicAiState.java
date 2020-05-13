package com.github.workinggames.castra.core.ai.voons;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

enum BasicAiState implements State<BasicAi>
{
    ATTACK()
        {
            @Override
            public void enter(BasicAi entity)
            {
                entity.attack();
            }
        },
    WAIT();

    @Override
    public void enter(BasicAi entity)
    {
    }

    @Override
    public void update(BasicAi entity)
    {
    }

    @Override
    public void exit(BasicAi entity)
    {
    }

    @Override
    public boolean onMessage(BasicAi entity, Telegram telegram)
    {
        return true;
    }
}