package com.github.workinggames.castra.core.ai;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.github.workinggames.castra.core.actor.Army;

@Data
@AllArgsConstructor
public class ArmyInfo
{
    private Army army;
    private int soldierEstimate;
}