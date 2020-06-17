package com.github.workinggames.castra.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.ui.GameInstructions;
import com.github.workinggames.castra.core.ui.GameOptions;
import com.github.workinggames.castra.core.ui.PlayerOptions;
import com.github.workinggames.castra.core.ui.PlayerOptionsGroup;
import com.github.workinggames.castra.core.ui.Skins;

public class MainMenuScreen extends ScreenAdapter
{
    private final Castra game;
    private final Stage stage;
    private final TextButton startGameButton;
    private final TextButton gameOptionsButton;
    private final TextButton gameInstructionsButton;
    private final GameOptions gameOptions;

    private PlayerOptions player1Options = null;
    private PlayerOptions player2Options = null;

    public MainMenuScreen(Castra game)
    {
        this.game = game;

        stage = new Stage(game.getViewport());
        game.getInputMultiplexer().addProcessor(stage);

        Skins.initialize(game);

        LoadingScreen loadingScreen = new LoadingScreen(game);
        gameOptions = new GameOptions(game);
        startGameButton = new TextButton("Start game", game.getSkin());
        gameOptionsButton = new TextButton("Game options", game.getSkin());
        gameInstructionsButton = new TextButton("Game instructions", game.getSkin());

        addBackground();
        addTitle(game);
        addStartGameButton(loadingScreen);
        addGameOptions();
        addPlayerOptions();
        addGameInstructions();
    }

    private void addBackground()
    {
        stage.addActor(Screens.toBackground(game.getTextureAtlas().findRegion("Bricks").getTexture(),
            game.getViewport()));
    }

    private void addTitle(Castra game)
    {
        Drawable drawable = game.getSkin().newDrawable("white", Color.BLACK);
        Image titleBox = new Image(drawable);

        Label title = new Label("CHARGE!", game.getSkin(), "title");
        title.setPosition(Screens.getCenterX(title), Screens.getRelativeY(90));
        titleBox.setSize(title.getWidth() + 30, title.getHeight() + 30);
        titleBox.setPosition(title.getX() - 15, title.getY() - 15);

        stage.addActor(titleBox);
        stage.addActor(title);
    }

    private void addStartGameButton(LoadingScreen loadingScreen)
    {
        startGameButton.getLabel().setFontScale(0.95f);
        startGameButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.input.vibrate(50);
                game.setScreen(loadingScreen);
                dispose();
            }
        });
        startGameButton.setPosition(Screens.getCenterX(startGameButton), Screens.getRelativeY(60));
        stage.addActor(startGameButton);
    }

    private void addGameOptions()
    {
        gameOptions.setSize(game.getViewport().getWorldWidth() - 200, game.getViewport().getWorldHeight() - 250);
        gameOptions.setPosition(Screens.getCenterX(gameOptions), Screens.getRelativeY(5));
        gameOptions.setVisible(false);
        gameOptions.getCloseOptionsButton().addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.input.vibrate(50);
                gameOptions.getCloseOptionsButton().setChecked(false);
                gameOptions.setVisible(false);
                gameOptionsButton.setVisible(true);
                startGameButton.setVisible(true);
                player1Options.setVisible(true);
                player2Options.setVisible(true);
            }
        });
        stage.addActor(gameOptions);

        gameOptionsButton.getLabel().setFontScale(0.95f);
        gameOptionsButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.input.vibrate(50);
                gameOptionsButton.setChecked(false);
                gameOptions.setVisible(true);
                gameOptionsButton.setVisible(false);
                startGameButton.setVisible(false);
                player1Options.setVisible(false);
                player2Options.setVisible(false);
            }
        });
        gameOptionsButton.setPosition(Screens.getCenterX(gameOptionsButton), Screens.getRelativeY(52));
        stage.addActor(gameOptionsButton);
    }

    private void addPlayerOptions()
    {
        PlayerOptionsGroup playerOptionsGroup = new PlayerOptionsGroup(game);
        player1Options = playerOptionsGroup.getPlayer1Options();
        player1Options.setPosition(Screens.getRelativeX(30), Screens.getRelativeY(20));
        stage.addActor(player1Options);

        player2Options = playerOptionsGroup.getPlayer2Options();
        player2Options.setPosition(Screens.getRelativeX(70), Screens.getRelativeY(20));
        stage.addActor(player2Options);
    }

    private void addGameInstructions()
    {
        gameInstructionsButton.setPosition(Screens.getCenterX(gameInstructionsButton), Screens.getRelativeY(46));
        stage.addActor(gameInstructionsButton);

        GameInstructions gameInstructions = new GameInstructions(game);
        ScrollPane gameInstructionsPanel = new ScrollPane(gameInstructions, game.getSkin());
        gameInstructionsPanel.setVisible(false);
        gameInstructionsPanel.setZIndex(0);
        gameInstructionsPanel.setSize(Screens.getRelativeX(90), Screens.getRelativeY(85));
        gameInstructionsPanel.setPosition(Screens.getCenterX(gameInstructionsPanel), Screens.getRelativeY(2));
        stage.addActor(gameInstructionsPanel);

        gameInstructionsButton.getLabel().setFontScale(0.95f);
        gameInstructionsButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.input.vibrate(50);
                gameInstructionsButton.setChecked(false);
                gameInstructionsPanel.setVisible(true);
            }
        });

        gameInstructions.getCloseInstructionsButton().addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                gameInstructionsPanel.setVisible(false);
                gameInstructions.getCloseInstructionsButton().setChecked(false);
            }
        });
    }

    @Override
    public void render(float delta)
    {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose()
    {
        game.getInputMultiplexer().removeProcessor(stage);
        stage.dispose();
    }
}