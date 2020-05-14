package com.github.workinggames.castra.core.ai;

import com.github.workinggames.castra.core.ai.simple.SimpleAi;
import com.github.workinggames.castra.core.ai.voons.BasicAi;
import com.github.workinggames.castra.core.ai.voons.ConfrontingAi;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.stage.World;

public class AiInitializer
{
    public Ai initialize(World world, Player aiPlayer)
    {
        Ai result = null;
        if (aiPlayer.getAiType() != null)
        {
            switch (aiPlayer.getAiType())
            {
                case RANDY:
                    result = new SimpleAi(world, aiPlayer);
                    break;
                case BILLY:
                    result = new BasicAi(world, aiPlayer);
                    break;
                case FRANK:
                    result = new ConfrontingAi(world, aiPlayer);
                    break;
            }
        }
        return result;
    }
}