package com.github.workinggames.castra.core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.graphics.Color;

@RequiredArgsConstructor
public enum PlayerColorSchema
{
    RED(new PlayerColor(new Color(0xFF0000ff))),
    BLUE(new PlayerColor(new Color(0x0000FFff))),
    GREEN(new PlayerColor(new Color(0x7CFC00ff))),
    YELLOW(new PlayerColor(new Color(0xffff00ff))),
    PURPLE(new PlayerColor(new Color(0x800080ff))),
    ORANGE(new PlayerColor(new Color(0xFFA500ff))),
    CYAN(new PlayerColor(new Color(0x00FFFFff))),
    PINK(new PlayerColor(new Color(0xFFC0CBff))),
    DARK_GREY(new PlayerColor(new Color(0x383838ff))),
    LIGHT_BLUE(new PlayerColor(new Color(0x00BFFFff))),
    DARK_BLUE(new PlayerColor(new Color(0x00008Bff))),
    DARK_GREEN(new PlayerColor(new Color(0x006400ff))),
    KHAKI(new PlayerColor(new Color(0xF0E68Cff))),
    NEUTRAL(new PlayerColor(new Color(0xc8c8c8ff)));

    @Getter
    private final PlayerColor playerColor;
}