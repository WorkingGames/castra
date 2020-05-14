package com.github.workinggames.castra.core.ai.voons;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.utils.Array;

@Getter
@RequiredArgsConstructor
public class BattleInfo
{
    private final Array<ArmyInfo> armyInfos;
    private final float battleStartedTimestamp;
}