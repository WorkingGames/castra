package com.github.workinggames.castra.core.ai.voons;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.utils.Array;
import com.github.workinggames.castra.core.model.Player;

@Getter
@RequiredArgsConstructor
public class BattleInfo
{
    private final Array<ArmyInfo> armyInfos;
    private final Player defender;
    private final float battleStartedTimestamp;
}