package com.github.workinggames.castra.core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.graphics.Color;

@RequiredArgsConstructor
public enum PlayerColorSchema
{
    RED(new PlayerColor(new Color(0xFF0000ff), new Color(0x6d0103ff))),
    BLUE(new PlayerColor(new Color(0x0000FFff), new Color(0x023adaff))),
    GREEN(new PlayerColor(new Color(0x7CFC00ff), new Color(0x006400ff))),
    YELLOW(new PlayerColor(new Color(0xffff00ff), new Color(0xffd700ff))),
    PURPLE(new PlayerColor(new Color(0x800080ff), new Color(0x8a2be2ff))),
    ORANGE(new PlayerColor(new Color(0xFFA500ff), new Color(0x171717ff))),
    CYAN(new PlayerColor(new Color(0x00FFFFff), new Color(0x171717ff))),
    PINK(new PlayerColor(new Color(0xFFC0CBff), new Color(0x171717ff))),
    DARK_GREY(new PlayerColor(new Color(0x383838ff), new Color(0x171717ff))),
    LIGHT_BLUE(new PlayerColor(new Color(0x00BFFFff), new Color(0x171717ff))),
    DARK_BLUE(new PlayerColor(new Color(0x00008Bff), new Color(0x171717ff))),
    DARK_GREEN(new PlayerColor(new Color(0x006400ff), new Color(0x171717ff))),
    KHAKI(new PlayerColor(new Color(0xF0E68Cff), new Color(0x171717ff))),
    NEUTRAL(new PlayerColor(new Color(0xc8c8c8ff), new Color(0x4b4b4bff)));

    @Getter
    private final PlayerColor playerColor;
}