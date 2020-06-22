package com.github.workinggames.castra.core.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.workinggames.castra.core.Castra;

public class Skins
{
    public static void initialize(Castra game)
    {
        Skin skin = game.getSkin();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        skin.add("default", game.getFontProvider().getDefaultFont());
        skin.add("Vinque", game.getFontProvider().getVinque());
        skin.add("VinqueLarge", game.getFontProvider().getVinqueLarge());

        Label.LabelStyle defaultStyle = new Label.LabelStyle();
        defaultStyle.font = game.getFontProvider().getVinque();
        defaultStyle.fontColor = Color.WHITE;
        skin.add("default", defaultStyle);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        Image regularButtonLarge = new Image(game.getTextureAtlas().findRegion("Button"));
        Image mouseOverButtonLarge = new Image(game.getTextureAtlas().findRegion("MouseOverButton"));
        Image clickedButtonLarge = new Image(game.getTextureAtlas().findRegion("ClickedButton"));
        textButtonStyle.up = regularButtonLarge.getDrawable();
        textButtonStyle.down = clickedButtonLarge.getDrawable();
        textButtonStyle.checked = clickedButtonLarge.getDrawable();
        textButtonStyle.over = mouseOverButtonLarge.getDrawable();
        textButtonStyle.font = game.getFontProvider().getVinque();
        skin.add("default", textButtonStyle);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        Drawable inputBackground = new Image(game.getTextureAtlas().findRegion("InputBackground")).getDrawable();
        textFieldStyle.background = inputBackground;
        textFieldStyle.fontColor = Color.BLACK;
        textFieldStyle.font = skin.getFont("Vinque");
        Pixmap cursorPixmap = new Pixmap(1, 30, Pixmap.Format.RGBA8888);
        cursorPixmap.setColor(Color.BLACK);
        cursorPixmap.fill();
        textFieldStyle.cursor = new Image(new Texture(cursorPixmap)).getDrawable();
        skin.add("default", textFieldStyle);

        List.ListStyle listStyle = new List.ListStyle();
        listStyle.font = skin.getFont("Vinque");
        listStyle.fontColorUnselected = Color.BLACK;
        listStyle.fontColorSelected = Color.BLACK;
        listStyle.selection = new Image(game.getTextureAtlas().findRegion("SelectedShade")).getDrawable();
        skin.add("default", listStyle);

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.background = new Image(game.getTextureAtlas().findRegion("AiTypeSelectBox")).getDrawable();
        skin.add("default", scrollPaneStyle);

        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle();
        selectBoxStyle.background = inputBackground;
        selectBoxStyle.fontColor = Color.BLACK;
        selectBoxStyle.font = skin.getFont("Vinque");
        selectBoxStyle.backgroundOpen = inputBackground;
        selectBoxStyle.backgroundOver = inputBackground;
        selectBoxStyle.scrollStyle = scrollPaneStyle;
        selectBoxStyle.listStyle = listStyle;
        skin.add("default", selectBoxStyle);

        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
        skin.add("default", imageButtonStyle);

        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.font = skin.getFont("Vinque");
        checkBoxStyle.fontColor = Color.WHITE;
        checkBoxStyle.checkboxOff = new TextureRegionDrawable(game.getTextureAtlas()
            .findRegion("uncheckedBox")
            .getTexture());
        checkBoxStyle.checkboxOn = new TextureRegionDrawable(game.getTextureAtlas()
            .findRegion("checkedBox")
            .getTexture());
        skin.add("default", checkBoxStyle);

        pixmap = new Pixmap(1, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture sliderTexture = new Texture(pixmap);
        sliderTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        Drawable sliderBar = new TextureRegionDrawable(sliderTexture);
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.knob = new TextureRegionDrawable(game.getTextureAtlas().findRegion("sliderKnob").getTexture());
        sliderStyle.knobBefore = sliderBar;
        sliderStyle.knobAfter = sliderBar;
        sliderStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
        skin.add("default-horizontal", sliderStyle);
    }
}