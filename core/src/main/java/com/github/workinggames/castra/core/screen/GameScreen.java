package com.github.workinggames.castra.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.actor.Army;
import com.github.workinggames.castra.core.actor.Settlement;
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
    private boolean gameEnded = false;

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
        if (gameScreenDrawn && !gameEnded)
        {
            Player winner = getWinner();
            if (!gameStarted)
            {
                soldierSpawner.startSpawn();
                battleProcessor.startBattles(game.getGameConfiguration().getGameSpeed().getBattleProcessingInterval());
                game.getStatisticsEventCreator().gameStarted(this.world);
                gameStarted = true;
                // starting the game takes some time and it is not part of the actual game time, so skip the next delta
                skipNextDeltaForPlayTime = true;
            }
            else if (winner != null)
            {
                gameOver(winner);
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
        }

        world.act(delta);
        world.draw();
        gameScreenDrawn = true;
    }

    public void gameOver(Player winner)
    {
        gameEnded = true;
        freezeWorld();
        float playTime = world.getTimepiece().getTime();
        int score = ScoreUtility.getGameScore(world, winner, playTime);
        game.getStatisticsEventCreator().gameEnded(world, winner, playTime, score);
        addGameEndedOverlay(playTime, score);
    }

    private void freezeWorld()
    {
        soldierSpawner.stopSpawn();
        battleProcessor.stopBattles();
        for (Army army : world.getArmies())
        {
            army.clearActions();
        }
        for (Settlement settlement : world.getSettlements())
        {
            settlement.clearListeners();
        }
        world.getArmySplit().clearListeners();
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

    private Player getWinner()
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
        return winner;
    }

    private void addGameEndedOverlay(float playTime, int score)
    {
        Image overlay = new Image(game.getSkin().newDrawable("white", Color.valueOf("A9A9A940")));
        overlay.setSize(game.getViewport().getWorldWidth(), game.getViewport().getWorldHeight());
        world.addActor(overlay);

        String message = game.getGameConfiguration().getPlayer1().getName() + " Won!";
        if (!victoryCondition.player1Won())
        {
            message = game.getGameConfiguration().getPlayer2().getName() + " Won!";
        }
        message = message + " in " + MathUtils.ceil(playTime) + " seconds, getting a score of " + score;

        Label label = new Label(message, game.getSkin());
        label.setPosition(Screens.getCenterX(label), Screens.getRelativeY(60));
        world.addActor(label);

        TextButton mainMenuButton = new TextButton("Main Menu", game.getSkin());
        mainMenuButton.getLabel().setFontScale(0.95f);
        mainMenuButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.input.vibrate(50);
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });
        mainMenuButton.setPosition(Screens.getCenterX(mainMenuButton), Screens.getRelativeY(50));
        world.addActor(mainMenuButton);
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