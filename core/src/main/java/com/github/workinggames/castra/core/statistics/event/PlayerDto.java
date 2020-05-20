package com.github.workinggames.castra.core.statistics.event;

import com.github.workinggames.castra.core.ai.AiType;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.model.PlayerColorSchema;
import com.github.workinggames.castra.core.model.PlayerType;

public class PlayerDto
{
    private final PlayerType type;
    private final AiType aiType;
    private final PlayerColorSchema playerColorSchema;
    private final String name;

    public PlayerDto(Player player)
    {
        type = player.getType();
        aiType = player.getAiType();
        name = player.getName();
        playerColorSchema = player.getColorSchema();
    }
}