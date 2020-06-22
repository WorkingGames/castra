package com.github.workinggames.castra.core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.graphics.Color;

@Getter
@RequiredArgsConstructor
public enum PlayerColorSchema
{
    YELLOW("Yellow", new PlayerColor(new Color(0xffff00ff)), Color.BLACK),
    ORANGE("Orange", new PlayerColor(new Color(0xFFA500ff)), Color.WHITE),
    RED("Red", new PlayerColor(new Color(0xFF0000ff)), Color.WHITE),
    PURPLE("Purple", new PlayerColor(new Color(0x800080ff)), Color.WHITE),
    CYAN("Cyan", new PlayerColor(new Color(0x00FFFFff)), Color.BLACK),
    BLUE("Blue", new PlayerColor(new Color(0x0000FFff)), Color.WHITE),
    GREEN("Green", new PlayerColor(new Color(0x006400ff)), Color.WHITE),
    KHAKI("Khaki", new PlayerColor(new Color(0xF0E68Cff)), Color.BLACK),
    NEUTRAL("Gray", new PlayerColor(new Color(0xc8c8c8ff)), Color.BLACK);

    private final String label;
    private final PlayerColor playerColor;
    private final Color fontColor;

    public static PlayerColorSchema fromLabel(String label)
    {
        return PlayerColorSchema.valueOf(label.toUpperCase());
    }
}