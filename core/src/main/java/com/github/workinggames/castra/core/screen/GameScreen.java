package com.github.workinggames.castra.core.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.input.ArmySplitInputProcessor;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.stage.World;
import com.github.workinggames.castra.core.statistics.ScoreUtility;
import com.github.workinggames.castra.core.task.BattleProcessor;
import com.github.workinggames.castra.core.task.SoldierSpawner;

public class GameScreen extends ScreenAdapter
{
    private final Castra game;
    private final World world;
    private final SoldierSpawner soldierSpawner;
    private final BattleProcessor battleProcessor;
    private final VictoryCondition victoryCondition;
    private final ArmySplitInputProcessor armySplitInputProcessor;

    private boolean gameStarted = false;
    private boolean skipNextDeltaForPlayTime = false;
    private boolean gameScreenDrawn = false;

    public GameScreen(Castra game, World world)
    {
        this.game = game;
        this.world = world;

        if (game.getGameConfiguration().getPlayer1().isHuman())
        {
            armySplitInputProcessor = new ArmySplitInputProcessor(game.getGameConfiguration().getPlayer1(),
                this.world.getArmySplit());
        }
        else if (game.getGameConfiguration().getPlayer2().isHuman())
        {
            armySplitInputProcessor = new ArmySplitInputProcessor(game.getGameConfiguration().getPlayer2(),
                this.world.getArmySplit());
        }
        else
        {
            armySplitInputProcessor = null;
        }
        if (armySplitInputProcessor != null)
        {
            game.getInputMultiplexer().addProcessor(armySplitInputProcessor);
        }

        soldierSpawner = new SoldierSpawner(this.world.getSettlements());
        battleProcessor = new BattleProcessor(this.world.getBattles(),
            this.world.getGameId(),
            game.getStatisticsEventCreator());
        victoryCondition = new VictoryCondition(this.world);
    }

    @Override
    public void render(float delta)
    {
        if (gameScreenDrawn)
        {
            if (!gameStarted)
            {
                soldierSpawner.startSpawn();
                battleProcessor.startBattles(game.getGameConfiguration().getGameSpeed().getBattleProcessingInterval());
                game.getStatisticsEventCreator().gameStarted(this.world);
                gameStarted = true;
                // starting the game takes some time and it is not part of the actual game time, so skip the next delta
                skipNextDeltaForPlayTime = true;
            }
            else if (!skipNextDeltaForPlayTime)
            {
                // if the game was paused, the delta will be including the pause, so we should skip it
                world.getTimepiece().update(delta);
            }
            else
            {
                skipNextDeltaForPlayTime = false;
            }
            checkGameOver();
        }

        world.act(delta);
        world.draw();
        gameScreenDrawn = true;
    }

    @Override
    public void pause()
    {
        super.pause();
        soldierSpawner.stopSpawn();
        battleProcessor.stopBattles();
    }

    @Override
    public void resume()
    {
        skipNextDeltaForPlayTime = true;
        super.resume();
        soldierSpawner.startSpawn();
        battleProcessor.startBattles(game.getGameConfiguration().getGameSpeed().getBattleProcessingInterval());
    }

    private void checkGameOver()
    {
        Player winner = null;
        if (victoryCondition.player1Won())
        {
            winner = world.getGameConfiguration().getPlayer1();
        }
        else if (victoryCondition.player1Lost())
        {
            winner = world.getGameConfiguration().getPlayer2();
        }

        if (winner != null)
        {
            float playTime = world.getTimepiece().getTime();
            int score = ScoreUtility.getGameScore(world, winner, playTime);
            game.getStatisticsEventCreator().gameEnded(world, winner, playTime, score);
            game.setScreen(new GameOverScreen(game, victoryCondition.player1Won(), playTime, score));
            dispose();
        }
    }

    @Override
    public void dispose()
    {
        battleProcessor.dispose();
        soldierSpawner.dispose();
        if (armySplitInputProcessor != null)
        {
            game.getInputMultiplexer().removeProcessor(armySplitInputProcessor);
        }
        MessageManager.getInstance().clear();
    }
}