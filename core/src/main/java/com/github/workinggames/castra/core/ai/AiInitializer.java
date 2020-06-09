package com.github.workinggames.castra.core.ai;

import lombok.RequiredArgsConstructor;

import com.github.workinggames.castra.core.ai.simple.SimpleAi;
import com.github.workinggames.castra.core.ai.voons.BillyAi;
import com.github.workinggames.castra.core.ai.voons.FrankyAi;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.model.PlayerType;
import com.github.workinggames.castra.core.stage.GameConfiguration;
import com.github.workinggames.castra.core.stage.World;
import com.github.workinggames.castra.core.task.AsyncInitializer;

@RequiredArgsConstructor
public class AiInitializer implements AsyncInitializer
{
    private final World world;

    private boolean finished;

    public void initialize()
    {
        GameConfiguration gameConfiguration = world.getGameConfiguration();
        if (gameConfiguration.getPlayer1().getType().equals(PlayerType.AI))
        {
            world.setAi1(initialize(world, gameConfiguration.getPlayer1()));
        }
        if (gameConfiguration.getPlayer2().getType().equals(PlayerType.AI))
        {
            world.setAi2(initialize(world, gameConfiguration.getPlayer2()));
        }

        finished = true;
    }

    private Ai initialize(World world, Player aiPlayer)
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
                    result = new BillyAi(world, aiPlayer);
                    break;
                case FRANKY:
                    result = new FrankyAi(world, aiPlayer);
                    break;
            }
        }
        return result;
    }

    @Override
    public boolean isFinished()
    {
        return finished;
    }
}