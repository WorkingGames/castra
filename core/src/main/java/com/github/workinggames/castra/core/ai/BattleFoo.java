package com.github.workinggames.castra.core.ai;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.utils.Array;

@Getter
@RequiredArgsConstructor
public class BattleFoo
{
    private final Array<ArmyFoo> armyFoos;
    private final float battleStartedTimestamp;
}