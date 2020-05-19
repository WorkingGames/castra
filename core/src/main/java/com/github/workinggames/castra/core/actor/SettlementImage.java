package com.github.workinggames.castra.core.actor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

@Getter
@RequiredArgsConstructor
public class SettlementImage
{
    private final Image image;
    private final AnimatedImage highlight;
    private final AnimatedImage flags;
}