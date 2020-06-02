package com.github.workinggames.castra.core.screen;

import lombok.extern.slf4j.Slf4j;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.input.ArmySplitInputProcessor;
import com.github.workinggames.castra.core.stage.World;
import com.github.workinggames.castra.core.task.BattleProcessor;
import com.github.workinggames.castra.core.task.SoldierSpawner;

@Slf4j
public class GameScreen extends ScreenAdapter
{
    private final Castra game;
    private final World world;
    private final SoldierSpawner soldierSpawner;
    private final BattleProcessor battleProcessor;
    private final VictoryCondition victoryCondition;
    private final ArmySplitInputProcessor armySplitInputProcessor;
    private final Texture backgroundTexture;

    private boolean gameStarted = false;

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

        backgroundTexture = game.getTextureAtlas().findRegion("Background256").getTexture();
    }

    @Override
    public void render(float delta)
    {
        if (!gameStarted)
        {
            soldierSpawner.startSpawn();
            battleProcessor.startBattles(game.getGameConfiguration().getBattleProcessingInterval());
            game.getStatisticsEventCreator().gameStarted(this.world);
            gameStarted = true;
        }
        draw(delta);
        checkGameOver();
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
        super.resume();
        soldierSpawner.startSpawn();
        battleProcessor.startBattles(game.getGameConfiguration().getBattleProcessingInterval());
    }

    private void draw(float delta)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.getBatch().begin();
        world.getBatch().setColor(Color.WHITE);
        world.getBatch()
            .draw(backgroundTexture,
                0,
                0,
                0,
                0,
                (int) game.getViewport().getWorldWidth(),
                (int) game.getViewport().getWorldHeight());
        world.getBatch().end();

        if (game.getGameState().equals(GameState.RUNNING))
        {
            world.act(delta);
        }
        world.draw();
    }

    private void checkGameOver()
    {
        if (victoryCondition.player1Won())
        {
            game.getStatisticsEventCreator().gameEnded(world, world.getGameConfiguration().getPlayer1());
            game.setScreen(new GameOverScreen(game, true));
            dispose();
        }
        else if (victoryCondition.player1Lost())
        {
            game.getStatisticsEventCreator().gameEnded(world, world.getGameConfiguration().getPlayer2());
            game.setScreen(new GameOverScreen(game, false));
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