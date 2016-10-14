package de.incub8.castra.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import com.badlogic.gdx.utils.Array;

@Getter
@ToString
@EqualsAndHashCode
public class World
{
    private final Array<Player> players;
    private final Array<Settlement> settlements;
    private final Paths paths;
    private final Array<Army> armies;
    private final Array<Battle> battles;
    private long startTime;

    public World(Array<Player> players, Array<Settlement> settlements)
    {
        this.players = players;
        this.settlements = settlements;
        paths = new Paths();
        armies = new Array<>();
        battles = new Array<>();
        startTime = 0;
    }
}