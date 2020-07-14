package com.github.workinggames.castra.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.ai.AiInitializer;
import com.github.workinggames.castra.core.input.DragDropInitializer;
import com.github.workinggames.castra.core.pathfinding.PathInitializer;
import com.github.workinggames.castra.core.stage.World;
import com.github.workinggames.castra.core.task.InitializerTask;
import com.github.workinggames.castra.core.worldbuilding.SettlementInitializer;

public class LoadingScreen extends ScreenAdapter
{
    private final Castra game;
    private final Stage stage;
    private final Screens screens;

    private World world;
    private SettlementInitializer settlementInitializer;
    private PathInitializer pathInitializer;
    private DragDropInitializer dragDropInitializer;
    private AiInitializer aiInitializer;

    private boolean settlementInitializerStarted;
    private boolean pathInitializerStarted;
    private boolean dragDropInitializerStarted;
    private boolean aiInitializerStarted;
    private boolean loadingScreenDrawn = false;

    public LoadingScreen(Castra game)
    {
        this.game = game;
        stage = new Stage(game.getViewport());
        screens = new Screens(game.getViewport());

        addBackground();

        Label loadingLabel = new Label("Loading...", game.getSkin());
        loadingLabel.setPosition(screens.getCenterX(loadingLabel), screens.getRelativeY(50));
        stage.addActor(loadingLabel);
    }

    private void addBackground()
    {
        stage.addActor(screens.toBackground(game.getTextureAtlas().findRegion("Bricks").getTexture()));
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            Gdx.input.vibrate(50);
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
        stage.act(delta);
        stage.draw();

        GameScreen gameScreen = null;
        // let's initialize once after the loading screen is displayed
        if (world == null && loadingScreenDrawn)
        {
            Viewport viewport = game.getViewport();
            TextureAtlas textureAtlas = game.getTextureAtlas();

            // if a game was already played we need to reset them to the initial value
            game.getGameConfiguration().getPlayer1().setSendTroopPercentage(50);
            game.getGameConfiguration().getPlayer2().setSendTroopPercentage(50);

            world = new World(viewport,
                textureAtlas,
                game.getFontProvider(),
                game.getGameConfiguration(),
                game.getStatisticsEventCreator(),
                game.getAudioManager());
            game.getInputMultiplexer().addProcessor(world);

            settlementInitializer = new SettlementInitializer(world);
            pathInitializer = new PathInitializer(viewport, textureAtlas, world);
            dragDropInitializer = new DragDropInitializer(world, game.getAudioManager());
            aiInitializer = new AiInitializer(world);
        }

        if (world != null && !settlementInitializerStarted)
        {
            Timer.post(new InitializerTask(settlementInitializer));
            settlementInitializerStarted = true;
        }
        if (world != null && settlementInitializer.isFinished() && !dragDropInitializerStarted)
        {
            Timer.post(new InitializerTask(dragDropInitializer));
            dragDropInitializerStarted = true;
        }
        if (world != null && settlementInitializer.isFinished() && !pathInitializerStarted)
        {
            Timer.post(new InitializerTask(pathInitializer));
            pathInitializerStarted = true;
        }
        if (world != null &&
            settlementInitializer.isFinished() &&
            pathInitializer.isFinished() &&
            !aiInitializerStarted)
        {
            gameScreen = new GameScreen(game, world);
            aiInitializer.initialize();
            aiInitializerStarted = true;
        }

        if (world != null &&
            settlementInitializer.isFinished() &&
            pathInitializer.isFinished() &&
            dragDropInitializer.isFinished() &&
            aiInitializer.isFinished())
        {
            game.getAudioManager().stopMainMenuMusic();
            game.setScreen(gameScreen);
            stage.dispose();
        }

        loadingScreenDrawn = true;
    }

    @Override
    public void pause()
    {
        super.pause();
    }

    @Override
    public void resume()
    {
        super.resume();
    }

    @Override
    public void dispose()
    {
        game.getInputMultiplexer().removeProcessor(stage);
        stage.dispose();
    }
}