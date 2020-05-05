package com.github.workinggames.castra.core.screen;

import lombok.extern.slf4j.Slf4j;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.ai.SimpleAi;
import com.github.workinggames.castra.core.input.ArmySplitInputProcessor;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.stage.World;
import com.github.workinggames.castra.core.task.BattleProcessor;
import com.github.workinggames.castra.core.task.SoldierSpawner;
import com.github.workinggames.castra.core.worldbuilding.WorldInitializer;

@Slf4j
public class GameScreen extends ScreenAdapter
{
    private final Castra game;
    private final World worldStage;

    private final SoldierSpawner soldierSpawner;
    private final BattleProcessor battleProcessor;
    private final VictoryCondition victoryCondition;
    private final SimpleAi simpleAi;
    private final ArmySplitInputProcessor armySplitInputProcessor;

    public GameScreen(Castra game, long seed, Player player1, Player player2)
    {
        this.game = game;
        log.info("Starting game with seed: " + seed);
        worldStage = new World(game.getViewport(),
            game.getTextureAtlas(),
            game.getFontProvider(),
            seed,
            player1,
            player2);
        game.getInputMultiplexer().addProcessor(worldStage);

        armySplitInputProcessor = new ArmySplitInputProcessor(worldStage.getPlayer1(), worldStage.getArmySplit());
        game.getInputMultiplexer().addProcessor(armySplitInputProcessor);

        new WorldInitializer(game.getViewport(), game.getTextureAtlas(), seed).initialize(worldStage);

        soldierSpawner = new SoldierSpawner(worldStage.getSettlements());
        soldierSpawner.startSpawn();
        battleProcessor = new BattleProcessor(worldStage.getBattles());
        battleProcessor.startBattles();

        victoryCondition = new VictoryCondition(worldStage);
        simpleAi = new SimpleAi(worldStage);
    }

    @Override
    public void render(float delta)
    {
        simpleAi.update();
        draw(delta);
        checkGameOver();
    }

    private void draw(float delta)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        worldStage.getBatch().begin();
        worldStage.getBatch().setColor(Color.WHITE);
        worldStage.getBatch()
            .draw(game.getTextureAtlas().findRegion("Background256").getTexture(),
                0,
                0,
                0,
                0,
                (int) game.getViewport().getWorldWidth(),
                (int) game.getViewport().getWorldHeight());
        worldStage.getBatch().end();

        worldStage.act(delta);

        worldStage.draw();
    }

    private void checkGameOver()
    {
        if (victoryCondition.playerWon())
        {
            game.setScreen(new GameOverScreen(game, true));
            dispose();
        }
        else if (victoryCondition.playerLost())
        {
            game.setScreen(new GameOverScreen(game, false));
            dispose();
        }
    }

    @Override
    public void dispose()
    {
        battleProcessor.dispose();
        soldierSpawner.dispose();
        game.getInputMultiplexer().removeProcessor(worldStage);
        game.getInputMultiplexer().removeProcessor(armySplitInputProcessor);
        worldStage.dispose();
    }
}