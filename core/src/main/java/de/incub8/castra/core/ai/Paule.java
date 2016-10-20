package de.incub8.castra.core.ai;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

public enum Paule implements State<Horst>
{
    ATTACK()
        {
            @Override
            public void enter(Horst entity)
            {
                entity.attack();
            }
        },
    WAIT;

    @Override
    public void enter(Horst entity)
    {
    }

    @Override
    public void update(Horst entity)
    {
    }

    @Override
    public void exit(Horst entity)
    {
    }

    @Override
    public boolean onMessage(Horst entity, Telegram telegram)
    {
        return false;
    }
}