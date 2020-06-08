package com.github.workinggames.castra.core.stage;

import lombok.experimental.UtilityClass;

import com.badlogic.gdx.math.MathUtils;

@UtilityClass
public class IdGenerator
{
    private final String
        CHARACTERS_FOR_RANDOM_STRING
        = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final int LENGTH = 20;

    public String random()
    {
        StringBuilder result = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++)
        {
            int characterIndex = MathUtils.random(CHARACTERS_FOR_RANDOM_STRING.length() - 1);
            result.append(CHARACTERS_FOR_RANDOM_STRING.charAt(characterIndex));
        }
        return result.toString();
    }
}