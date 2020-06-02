package com.github.workinggames.castra.core.ai.voons;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.badlogic.gdx.utils.Array;
import com.github.workinggames.castra.core.model.Player;

@Getter
@RequiredArgsConstructor
public class BattleInfo
{
    private final Array<ArmyInfo> armyInfos;
    private final Player defender;
    private final float battleStartedTimestamp;

    @Setter
    private int estimatedSoldiers = 0;

    @Setter
    private int actualSoldiersAttacking = 0;
}