package com.github.workinggames.castra.core.screen;

import com.badlogic.gdx.ScreenAdapter;
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
import com.github.workinggames.castra.core.worldbuilding.FluffInitializer;
import com.github.workinggames.castra.core.worldbuilding.SettlementInitializer;

public class LoadingScreen extends ScreenAdapter
{
    private final Castra game;
    private final Stage stage;

    private World world;
    private SettlementInitializer settlementInitializer;
    private FluffInitializer fluffInitializer;
    private PathInitializer pathInitializer;
    private DragDropInitializer dragDropInitializer;
    private AiInitializer aiInitializer;

    private boolean settlementInitializerStarted;
    private boolean fluffInitializerStarted;
    private boolean pathInitializerStarted;
    private boolean dragDropInitializerStarted;
    private boolean aiInitializerStarted;
    private boolean loadingScreenDrawn = false;

    public LoadingScreen(Castra game)
    {
        this.game = game;
        stage = new Stage(game.getViewport());

        addBackground();

        Label loadingLabel = new Label("Loading...", game.getSkin());
        loadingLabel.setPosition(Screens.getCenterX(loadingLabel), Screens.getRelativeY(50));
        stage.addActor(loadingLabel);
    }

    private void addBackground()
    {
        stage.addActor(Screens.toBackground(game.getTextureAtlas().findRegion("Bricks").getTexture(),
            game.getViewport()));
    }

    @Override
    public void render(float delta)
    {
        stage.act(delta);
        stage.draw();

        GameScreen gameScreen = null;
        // let's initialize once after the loading screen is displayed
        if (world == null && loadingScreenDrawn)
        {
            Viewport viewport = game.getViewport();
            TextureAtlas textureAtlas = game.getTextureAtlas();

            world = new World(viewport,
                textureAtlas,
                game.getFontProvider(),
                game.getGameConfiguration(),
                game.getStatisticsEventCreator());
            game.getInputMultiplexer().addProcessor(world);

            settlementInitializer = new SettlementInitializer(world, viewport);
            fluffInitializer = new FluffInitializer(world, viewport);
            pathInitializer = new PathInitializer(viewport, textureAtlas, world);
            dragDropInitializer = new DragDropInitializer(world);
            aiInitializer = new AiInitializer(world);
        }

        if (world != null && !settlementInitializerStarted)
        {
            Timer.post(new InitializerTask(settlementInitializer));
            settlementInitializerStarted = true;
        }
        if (world != null && settlementInitializer.isFinished() && !fluffInitializerStarted)
        {
            Timer.post(new InitializerTask(fluffInitializer));
            fluffInitializerStarted = true;
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
            fluffInitializer.isFinished() &&
            pathInitializer.isFinished() &&
            dragDropInitializer.isFinished() &&
            aiInitializer.isFinished())
        {
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