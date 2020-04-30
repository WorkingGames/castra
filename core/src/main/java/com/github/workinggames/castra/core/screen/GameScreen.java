package com.github.workinggames.castra.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.ai.SimpleAi;
import com.github.workinggames.castra.core.input.ArmySplitInputProcessor;
import com.github.workinggames.castra.core.task.BattleProcessor;
import com.github.workinggames.castra.core.task.SoldierSpawner;
import com.github.workinggames.castra.core.stage.World;
import com.github.workinggames.castra.core.worldbuilding.WorldInitializer;

public class GameScreen extends ScreenAdapter
{
    private final Castra game;
    private final World worldStage;

    private final SoldierSpawner soldierSpawner;
    private final BattleProcessor battleProcessor;
    private final VictoryCondition victoryCondition;
    private final SimpleAi simpleAi;
    private final ArmySplitInputProcessor armySplitInputProcessor;

    public GameScreen(Castra game)
    {
        this.game = game;

        worldStage = new World(game.getViewport(), game.getTextureAtlas(), game.getFontProvider());
        game.getInputMultiplexer().addProcessor(worldStage);

        armySplitInputProcessor = new ArmySplitInputProcessor(worldStage.getHumanPlayer(), worldStage.getArmySplit());
        game.getInputMultiplexer().addProcessor(armySplitInputProcessor);

        long seed = MathUtils.random(978234L);
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
        worldStage.getBatch().draw(
            game.getTextureAtlas().findRegion("Background256").getTexture(),
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