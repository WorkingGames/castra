package com.github.workinggames.castra.core.screen;

import lombok.extern.slf4j.Slf4j;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
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

@Slf4j
public class LoadingScreen extends ScreenAdapter
{
    private static final float LOADING_DELAY = 0.3f;
    private final Castra game;
    private final Stage stage;
    private final LoadingState loadingState;
    private final Texture backgroundTexture;

    private World world;
    private SettlementInitializer settlementInitializer;
    private FluffInitializer fluffInitializer;
    private PathInitializer pathInitializer;
    private DragDropInitializer dragDropInitializer;
    private AiInitializer aiInitializer;

    public LoadingScreen(Castra game)
    {
        this.game = game;
        stage = new Stage(game.getViewport());
        // TODO add actual loading page

        backgroundTexture = game.getTextureAtlas().findRegion("Bricks").getTexture();
        backgroundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        Label loadingLabel = new Label("Loading...", game.getSkin());
        loadingLabel.setPosition(Screens.getCenterX(loadingLabel), Screens.getRelativeY(50));
        stage.addActor(loadingLabel);

        loadingState = LoadingState.getInstance();
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Batch stageBatch = stage.getBatch();
        stageBatch.begin();
        stageBatch.setColor(Color.WHITE);
        stageBatch.draw(backgroundTexture,
            0,
            0,
            0,
            0,
            (int) game.getViewport().getWorldWidth(),
            (int) game.getViewport().getWorldHeight());
        stageBatch.end();

        stage.act(delta);
        stage.draw();

        // let's initialize once after the loading screen is displayed
        if (world == null)
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

        if (world != null && !loadingState.isSettlementsInitialized())
        {
            Timer.post(new InitializerTask(settlementInitializer));
        }
        if (loadingState.isSettlementsInitialized() && !loadingState.isFluffInitialized())
        {
            Timer.schedule(new InitializerTask(fluffInitializer), LOADING_DELAY);
        }
        if (loadingState.isSettlementsInitialized() && !loadingState.isDragDropInitialized())
        {
            Timer.schedule(new InitializerTask(dragDropInitializer), LOADING_DELAY);
        }
        if (loadingState.isSettlementsInitialized() && !loadingState.isPathInitialized())
        {
            Timer.schedule(new InitializerTask(pathInitializer), LOADING_DELAY);
        }
        if (loadingState.isSettlementsInitialized() && loadingState.isPathInitialized())
        {
            aiInitializer.initialize();
        }

        if (loadingState.isSettlementsInitialized() &&
            loadingState.isFluffInitialized() &&
            loadingState.isPathInitialized() &&
            loadingState.isDragDropInitialized() &&
            loadingState.isAiInitialized())
        {
            game.setScreen(new GameScreen(game, world));
            stage.dispose();
        }
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