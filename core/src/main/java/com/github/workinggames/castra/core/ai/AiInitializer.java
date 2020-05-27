package com.github.workinggames.castra.core.ai;

import lombok.RequiredArgsConstructor;

import com.github.workinggames.castra.core.ai.simple.SimpleAi;
import com.github.workinggames.castra.core.ai.voons.BasicAi;
import com.github.workinggames.castra.core.ai.voons.ConfrontingAi;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.model.PlayerType;
import com.github.workinggames.castra.core.screen.LoadingState;
import com.github.workinggames.castra.core.stage.GameConfiguration;
import com.github.workinggames.castra.core.stage.World;
import com.github.workinggames.castra.core.task.AsyncInitializer;

@RequiredArgsConstructor
public class AiInitializer implements AsyncInitializer
{
    private final World world;

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
        LoadingState.getInstance().setAiInitialized(true);
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