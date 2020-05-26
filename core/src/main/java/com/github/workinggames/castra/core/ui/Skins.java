package com.github.workinggames.castra.core.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
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
        skin.add("menuLarge", game.getFontProvider().getMenuLarge());
        skin.add("menuMedium", game.getFontProvider().getMenuMedium());

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = game.getFontProvider().getTitle();
        titleStyle.fontColor = Color.FIREBRICK;
        skin.add("title", titleStyle);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("menuLarge");
        skin.add("default", textButtonStyle);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = skin.getFont("menuMedium");
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.cursor = skin.newDrawable("white", Color.WHITE);
        textFieldStyle.background = skin.newDrawable("white", Color.LIGHT_GRAY);
        textFieldStyle.messageFont = skin.getFont("menuMedium");
        textFieldStyle.messageFontColor = Color.WHITE;
        skin.add("default", textFieldStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("menuMedium");
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        List.ListStyle listStyle = new List.ListStyle();
        listStyle.font = skin.getFont("menuMedium");
        listStyle.selection = skin.newDrawable("white", Color.GOLD);
        skin.add("default", listStyle);

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.background = skin.newDrawable("white", Color.LIGHT_GRAY);
        skin.add("default", scrollPaneStyle);

        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle();
        selectBoxStyle.font = skin.getFont("menuMedium");
        selectBoxStyle.fontColor = Color.WHITE;
        selectBoxStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
        selectBoxStyle.backgroundOpen = skin.newDrawable("white", Color.LIGHT_GRAY);
        selectBoxStyle.listStyle = listStyle;
        selectBoxStyle.scrollStyle = scrollPaneStyle;
        skin.add("default", selectBoxStyle);

        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle();
        skin.add("default", imageButtonStyle);

        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.font = skin.getFont("menuMedium");
        checkBoxStyle.fontColor = Color.WHITE;
        checkBoxStyle.checkboxOff = new TextureRegionDrawable(game.getTextureAtlas()
            .findRegion("uncheckedBox")
            .getTexture());
        checkBoxStyle.checkboxOn = new TextureRegionDrawable(game.getTextureAtlas()
            .findRegion("checkedBox")
            .getTexture());
        skin.add("default", checkBoxStyle);
    }
}