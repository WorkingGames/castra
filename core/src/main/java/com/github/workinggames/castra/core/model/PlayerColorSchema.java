package com.github.workinggames.castra.core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.graphics.Color;

@RequiredArgsConstructor
public enum PlayerColorSchema
{
    RED(new PlayerColor(new Color(0xda0205ff), new Color(0x6d0103ff))),
    BLUE(new PlayerColor(new Color(0x4d7afdff), new Color(0x023adaff))),
    GREEN(new PlayerColor(new Color(0x32cd32ff), new Color(0x006400ff))),
    YELLOW(new PlayerColor(new Color(0xffff00ff), new Color(0xffd700ff))),
    PURPLE(new PlayerColor(new Color(0x9370dbff), new Color(0x8a2be2ff))),
    BLACK(new PlayerColor(new Color(0x383838ff), new Color(0x171717ff)));

    @Getter
    private final PlayerColor playerColor;
}