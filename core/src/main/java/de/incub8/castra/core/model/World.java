package de.incub8.castra.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import com.badlogic.gdx.math.GridPoint2;
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

    public World(Array<Player> players, Array<Settlement> settlements, Paths paths)
    {
        this.players = players;
        this.settlements = settlements;
        this.paths = paths;
        armies = new Array<>();
        battles = new Array<>();
        startTime = 0;
    }

    public void createArmy(Settlement origin, Settlement destination)
    {
        int count = origin.getSoldiers() * origin.getOwner().getSendTroopPercentage() / 100;
        Array<GridPoint2> path = paths.get(origin, destination);
        Army army = new Army(count, origin.getOwner(), destination, path);
        armies.add(army);
        origin.removeSoldiers(count);
    }
}