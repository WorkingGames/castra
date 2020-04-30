package com.github.workinggames.castra.core.input;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.InputAdapter;
import com.github.workinggames.castra.core.actor.ArmySplit;
import com.github.workinggames.castra.core.model.Player;

@RequiredArgsConstructor
public class ArmySplitInputProcessor extends InputAdapter
{
    private static final int STEP_SIZE = 5;

    private final Player player;
    private final ArmySplit armySplit;

    @Override
    public boolean scrolled(int amount)
    {
        int currentPercentage = player.getSendTroopPercentage();
        // upward scrolling results in a negative amount, so we change the sign by multiplying with -1
        int addPercentage = STEP_SIZE * amount * -1;
        int percentage = currentPercentage + addPercentage;

        // to keep our bounds
        percentage = Math.max(5, percentage);
        percentage = Math.min(100, percentage);

        player.setSendTroopPercentage(percentage);
        armySplit.updateLabel();
        return true;
    }
}