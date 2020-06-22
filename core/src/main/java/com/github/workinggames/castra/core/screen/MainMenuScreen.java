package com.github.workinggames.castra.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.workinggames.castra.core.Castra;
import com.github.workinggames.castra.core.actor.AnimatedImage;
import com.github.workinggames.castra.core.texture.AnimationUtil;
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
    private final TextButton quitGameButton;
    private final GameOptions gameOptions;
    private final Image mainMenu;
    private final Screens screens;

    private PlayerOptions player1Options = null;
    private PlayerOptions player2Options = null;
    private AnimationUtil animationUtil = new AnimationUtil();

    public MainMenuScreen(Castra game)
    {
        this.game = game;
        stage = new Stage(game.getViewport());
        game.getInputMultiplexer().clear();
        game.getInputMultiplexer().addProcessor(stage);
        screens = new Screens(game.getViewport());

        Skins.initialize(game);

        LoadingScreen loadingScreen = new LoadingScreen(game);
        mainMenu = new Image(game.getTextureAtlas().findRegion("MainMenu"));
        gameOptions = new GameOptions(game);
        startGameButton = new TextButton("Start Game", game.getSkin());
        gameOptionsButton = new TextButton("Game Options", game.getSkin());
        gameInstructionsButton = new TextButton("Game Manual", game.getSkin());
        quitGameButton = new TextButton("Quit Game", game.getSkin());

        addBackground();
        addTitle(game);
        addPlayerOptions();
        addMainMenu(loadingScreen);

        game.getAudioManager().playMainMenuMusic();
    }

    private void addBackground()
    {
        stage.addActor(screens.toBackground(game.getTextureAtlas().findRegion("Bricks").getTexture()));
    }

    private void addTitle(Castra game)
    {
        Image title = new Image(game.getTextureAtlas().findRegion("Title"));
        title.setPosition(screens.getCenterX(title), screens.getRelativeY(85));
        stage.addActor(title);

        Animation<TextureRegion> torchAnimation = animationUtil.createAnimation(game.getTextureAtlas()
            .findRegion("Torch")
            .getTexture(), 4, 1, 0.2f);

        AnimatedImage torch1 = new AnimatedImage(torchAnimation);
        torch1.setPosition(screens.getRelativeX(31), screens.getRelativeY(83));
        stage.addActor(torch1);

        AnimatedImage torch2 = new AnimatedImage(torchAnimation);
        torch2.setPosition(screens.getRelativeX(63), screens.getRelativeY(83));
        stage.addActor(torch2);
    }

    private void addMainMenu(LoadingScreen loadingScreen)
    {
        mainMenu.setPosition(screens.getCenterX(mainMenu), screens.getRelativeY(20));
        stage.addActor(mainMenu);
        addStartGameButton(loadingScreen);
        addGameOptions();
        addQuitGameButton();
        addGameInstructions();
    }

    private void addStartGameButton(LoadingScreen loadingScreen)
    {
        startGameButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.input.vibrate(50);
                game.getAudioManager().playChargeSound();
                game.setScreen(loadingScreen);
                dispose();
            }
        });
        startGameButton.setPosition(screens.getCenterX(startGameButton), screens.getRelativeY(64));
        startGameButton.padBottom(7);
        stage.addActor(startGameButton);
    }

    private void addGameOptions()
    {
        gameOptions.setVisible(false);
        gameOptions.getCloseOptionsButton().addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.input.vibrate(50);
                game.getAudioManager().playClickSound();
                gameOptions.getCloseOptionsButton().setChecked(false);
                gameOptions.setVisible(false);
                mainMenu.setVisible(true);
                gameOptionsButton.setVisible(true);
                startGameButton.setVisible(true);
                quitGameButton.setVisible(true);
                gameInstructionsButton.setVisible(true);
                player1Options.setVisible(true);
                player2Options.setVisible(true);
            }
        });
        stage.addActor(gameOptions);

        gameOptionsButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.input.vibrate(50);
                game.getAudioManager().playClickSound();
                gameOptions.setVisible(true);
                mainMenu.setVisible(false);
                gameOptionsButton.setChecked(false);
                gameOptionsButton.setVisible(false);
                gameInstructionsButton.setVisible(false);
                startGameButton.setVisible(false);
                quitGameButton.setVisible(false);
                player1Options.setVisible(false);
                player2Options.setVisible(false);
            }
        });
        gameOptionsButton.setPosition(screens.getCenterX(gameOptionsButton), screens.getRelativeY(52));
        gameOptionsButton.padBottom(7);
        stage.addActor(gameOptionsButton);
    }

    private void addGameInstructions()
    {
        gameInstructionsButton.setPosition(screens.getCenterX(gameInstructionsButton), screens.getRelativeY(40));
        gameInstructionsButton.padBottom(7);
        stage.addActor(gameInstructionsButton);

        GameInstructions gameInstructions = new GameInstructions(game);
        Image background = new Image(game.getTextureAtlas().findRegion("Parchment"));
        background.setSize(screens.getRelativeX(95), screens.getRelativeY(80));
        background.setPosition(screens.getCenterX(background), screens.getRelativeY(2));
        background.setVisible(false);
        stage.addActor(background);

        ScrollPane.ScrollPaneStyle scrollPaneStyle = game.getSkin().get(ScrollPane.ScrollPaneStyle.class);
        scrollPaneStyle.background = null;
        ScrollPane gameInstructionsPanel = new ScrollPane(gameInstructions, scrollPaneStyle);
        gameInstructionsPanel.setVisible(false);
        gameInstructionsPanel.setZIndex(0);
        gameInstructionsPanel.setSize(screens.getRelativeX(93), screens.getRelativeY(72));
        gameInstructionsPanel.setPosition(screens.getCenterX(gameInstructionsPanel), screens.getRelativeY(6));
        gameInstructionsPanel.setScrollingDisabled(true, false);
        gameInstructionsPanel.setOverscroll(false, false);
        stage.addActor(gameInstructionsPanel);

        gameInstructionsButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.input.vibrate(50);
                game.getAudioManager().playClickSound();
                gameInstructionsPanel.setScrollY(0);
                gameInstructionsButton.setChecked(false);
                background.setVisible(true);
                gameInstructionsPanel.setVisible(true);
            }
        });

        gameInstructions.getCloseInstructionsButton().addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.input.vibrate(50);
                game.getAudioManager().playClickSound();
                background.setVisible(false);
                gameInstructionsPanel.setVisible(false);
                gameInstructions.getCloseInstructionsButton().setChecked(false);
            }
        });
    }

    private void addQuitGameButton()
    {
        quitGameButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.input.vibrate(50);
                game.getAudioManager().playClickSound();
                Gdx.app.exit();
            }
        });
        quitGameButton.setPosition(screens.getCenterX(quitGameButton), screens.getRelativeY(28));
        quitGameButton.padBottom(7);
        stage.addActor(quitGameButton);
    }

    private void addPlayerOptions()
    {
        PlayerOptionsGroup playerOptionsGroup = new PlayerOptionsGroup(game);
        player1Options = playerOptionsGroup.getPlayer1Options();
        stage.addActor(player1Options);

        player2Options = playerOptionsGroup.getPlayer2Options();
        stage.addActor(player2Options);
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            Gdx.input.vibrate(50);
            Gdx.app.exit();
        }
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