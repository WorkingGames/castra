package com.github.workinggames.castra.core.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.screen.Screens;
import com.github.workinggames.castra.core.stage.World;
import com.github.workinggames.castra.core.statistics.ScoreUtility;

public class GameOverMenu extends Group
{
    private static final float LABEL_BEGIN = 135;
    private static final float VALUE_BEGIN = 320;
    private static final float SCORE_BEGIN = 500;

    private final int playTime;
    private final int soldiers;
    private final float soldierSpawnPerSecond;
    private final boolean humanLost;
    private final Label timeValueLabel;
    private final Label timeScoreLabel;
    private final Label soldierValueLabel;
    private final Label soldierScoreLabel;
    private final Label totalScoreValueLabel;

    private int currentTimeValue = 0;
    private int timeScore = 0;
    private int currentSoldierValue = 0;
    private int soldierScore = 0;

    public GameOverMenu(Castra game, Player winner, World world)
    {
        playTime = MathUtils.floor(world.getTimepiece().getTime());
        soldierSpawnPerSecond = ScoreUtility.getMapSoldierSpawnPerSecond(world);

        TextureAtlas textureAtlas = game.getTextureAtlas();
        Skin skin = game.getSkin();
        Screens screens = new Screens(game.getViewport());
        setSize(697, 519);
        setPosition(screens.getCenterX(this), screens.getRelativeY(30));

        Image gameOverMenu = new Image(textureAtlas.findRegion("GameOverMenu"));
        addActor(gameOverMenu);
        Image banners = new Image(textureAtlas.findRegion("GameOverBanners"));
        banners.setColor(winner.getColorSchema().getPlayerColor().getPrimaryColor());
        addActor(banners);

        String message;
        if (world.getGameConfiguration().getPlayer1().isHuman() || world.getGameConfiguration().getPlayer2().isHuman())
        {
            if (winner.isHuman())
            {
                message = "You Won!";
                soldiers = ScoreUtility.getSoldierCount(world, winner);
                humanLost = false;
                game.getStatisticsEventCreator()
                    .gameEnded(world,
                        winner,
                        playTime,
                        ScoreUtility.getWinningGameScore(world, winner, playTime),
                        true);
                game.getAudioManager().playVictorySound();
            }
            else
            {
                message = "You Lost!";
                soldiers = 0;
                humanLost = true;
                Player looser;
                if (winner.isPlayerOne())
                {
                    looser = world.getGameConfiguration().getPlayer2();
                }
                else
                {
                    looser = world.getGameConfiguration().getPlayer1();
                }
                game.getStatisticsEventCreator()
                    .gameEnded(world, looser, playTime, ScoreUtility.getLostTimeScore(playTime), false);
                game.getAudioManager().playDefeatSound();
            }
        }
        else
        {
            message = winner.getName() + " Won!";
            soldiers = ScoreUtility.getSoldierCount(world, winner);
            humanLost = false;
            game.getAudioManager().playVictorySound();
        }

        Label winnerLabel = new Label(message, skin, "VinqueLarge", winner.getColorSchema().getFontColor());
        winnerLabel.setPosition(getWidth() / 2 - winnerLabel.getWidth() / 2, 476);
        addActor(winnerLabel);

        Label timeLabel = new Label("Time", skin, "VinqueLarge", Color.WHITE);
        timeLabel.setPosition(LABEL_BEGIN, 360);
        addActor(timeLabel);
        timeValueLabel = new Label("0s", skin, "VinqueLarge", Color.WHITE);
        timeValueLabel.setPosition(VALUE_BEGIN, 360);
        addActor(timeValueLabel);
        timeScoreLabel = new Label("0", skin, "VinqueLarge", Color.WHITE);
        timeScoreLabel.setPosition(SCORE_BEGIN, 360);
        addActor(timeScoreLabel);

        Label soldierLabel = new Label("Soldiers", skin, "VinqueLarge", Color.WHITE);
        soldierLabel.setPosition(LABEL_BEGIN, 310);
        addActor(soldierLabel);
        soldierValueLabel = new Label("0", skin, "VinqueLarge", Color.WHITE);
        soldierValueLabel.setPosition(VALUE_BEGIN, 310);
        addActor(soldierValueLabel);
        soldierScoreLabel = new Label("0", skin, "VinqueLarge", Color.WHITE);
        soldierScoreLabel.setPosition(SCORE_BEGIN, 310);
        addActor(soldierScoreLabel);

        Pixmap pixmap = new Pixmap(500, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Image separator = new Image(new Texture(pixmap));
        separator.setPosition(100, 275);
        addActor(separator);

        Label totalScoreLabel = new Label("Total Score:", skin, "VinqueLarge", Color.WHITE);
        totalScoreLabel.setPosition(LABEL_BEGIN, 220);
        addActor(totalScoreLabel);
        totalScoreValueLabel = new Label("0", skin, "VinqueLarge", Color.WHITE);
        totalScoreValueLabel.setPosition(SCORE_BEGIN, 220);
        totalScoreValueLabel.setVisible(false);
        addActor(totalScoreValueLabel);
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);
        if (currentTimeValue < playTime)
        {
            currentTimeValue++;
            timeValueLabel.setText(currentTimeValue + "s");
            if (humanLost)
            {
                timeScore = ScoreUtility.getLostTimeScore(currentTimeValue);
            }
            else
            {
                timeScore = ScoreUtility.getWinningTimeScore(currentTimeValue);
            }
            timeScoreLabel.setText(timeScore);
        }
        if (currentSoldierValue < soldiers)
        {
            currentSoldierValue++;
            soldierValueLabel.setText(currentSoldierValue);
            soldierScore = ScoreUtility.getSoldierScore(currentSoldierValue, soldierSpawnPerSecond);
            soldierScoreLabel.setText(soldierScore);
        }

        if (currentTimeValue == playTime && currentSoldierValue == soldiers)
        {
            totalScoreValueLabel.setText(timeScore + soldierScore);
            totalScoreValueLabel.setVisible(true);
        }
    }
}