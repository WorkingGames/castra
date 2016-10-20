package de.incub8.castra.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import com.badlogic.gdx.ai.DefaultTimepiece;
import com.badlogic.gdx.ai.Timepiece;
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
    private final Timepiece timepiece;

    public World(Array<Player> players, Array<Settlement> settlements, Paths paths)
    {
        this.players = players;
        this.settlements = settlements;
        this.paths = paths;
        armies = new Array<>();
        battles = new Array<>();
        timepiece = new DefaultTimepiece();
    }

    public void createArmy(Settlement origin, Settlement destination)
    {
        int count = origin.getSoldiers() * origin.getOwner().getSendTroopPercentage() / 100;
        if (count > 0)
        {
            Array<GridPoint2> path = paths.get(origin, destination);
            Army army = new Army(count, origin.getOwner(), destination, path);
            armies.add(army);
            origin.removeSoldiers(count);
        }
    }
}