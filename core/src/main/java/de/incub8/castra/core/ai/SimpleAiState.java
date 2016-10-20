package de.incub8.castra.core.ai;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

public enum SimpleAiState implements State<SimpleAi>
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