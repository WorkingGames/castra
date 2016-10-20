package de.incub8.castra.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.incub8.castra.core.Castra;
import de.incub8.castra.core.renderer.Background;

public class MainMenuScreen extends ScreenAdapter
{
    private final Castra game;
    private final Stage stage;
    private Background background;
    private final BitmapFont font;
    private final Label.LabelStyle labelStyle;

    public MainMenuScreen(Castra game)
    {
        this.game = game;

        stage = new Stage(game.getViewport());

        background = new Background();

        font = new BitmapFont();
        labelStyle = new Label.LabelStyle(font, Color.WHITE);

        stage.addActor(createLabel("Welcome to Charge", 600, 350));
        stage.addActor(createLabel("Tap anywhere to begin!", 600, 400));
    }

    private Label createLabel(String text, int x, int y)
    {
        Label label = new Label(text, labelStyle);
        label.setPosition(x, y);
        return label;
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        background.render(stage.getBatch());
        stage.getBatch().end();

        stage.act(delta);

        stage.draw();

        if (Gdx.input.isTouched())
        {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void dispose()
    {
        font.dispose();
        background.dispose();
        stage.dispose();
    }
}