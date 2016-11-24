package de.incub8.castra.core.stage;

import java.util.Iterator;

import lombok.Getter;

import com.badlogic.gdx.ai.DefaultTimepiece;
import com.badlogic.gdx.ai.Timepiece;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.incub8.castra.core.actor.Army;
import de.incub8.castra.core.actor.ArmySplit;
import de.incub8.castra.core.actor.Battle;
import de.incub8.castra.core.actor.Settlement;
import de.incub8.castra.core.font.FontProvider;
import de.incub8.castra.core.model.Paths;
import de.incub8.castra.core.model.Player;
import de.incub8.castra.core.model.PlayerColor;
import de.incub8.castra.core.model.PlayerType;
import de.incub8.castra.core.model.SettlementSize;
import de.incub8.castra.core.pathfinding.LinePath;

public class World extends Stage
{
    private final TextureAtlas textureAtlas;
    private final FontProvider fontProvider;
    private final ActorComparator actorComparator;

    @Getter
    private final Player humanPlayer;

    @Getter
    private final Player aiPlayer;

    @Getter
    private final Array<Settlement> settlements;

    @Getter
    private final Paths paths;

    @Getter
    private final Array<Army> armies;

    @Getter
    private final Array<Battle> battles;

    @Getter
    private final Timepiece timepiece;

    @Getter
    private final ArmySplit armySplit;

    public World(Viewport viewport, TextureAtlas textureAtlas, FontProvider fontProvider)
    {
        super(viewport);
        this.textureAtlas = textureAtlas;
        this.fontProvider = fontProvider;
        actorComparator = new ActorComparator();

        humanPlayer = new Player(
            new PlayerColor(new Color(0x4d7afdff), new Color(0x023adaff)), "Bob", PlayerType.HUMAN);
        aiPlayer = new Player(new PlayerColor(new Color(0xda0205ff), new Color(0x6d0103ff)), "AI", PlayerType.AI);
        settlements = new Array<>();
        paths = new Paths();
        armies = new Array<>();
        battles = new Array<>();
        timepiece = new DefaultTimepiece();

        armySplit = new ArmySplit(textureAtlas, fontProvider, humanPlayer);
        addActor(armySplit);
    }

    @Override
    public void act(float deltaTime)
    {
        timepiece.update(deltaTime);
        processArmies();
        super.act(deltaTime);
        getActors().sort(actorComparator);
    }

    public void createSettlement(SettlementSize size, int x, int y, int soldiers, Player owner)
    {
        Settlement settlement = new Settlement(size, x, y, soldiers, owner, textureAtlas, fontProvider);
        addActor(settlement);
        settlements.add(settlement);
    }

    public void createArmy(Settlement origin, Settlement destination)
    {
        int count = origin.getSoldiers() * origin.getOwner().getSendTroopPercentage() / 100;
        if (count > 0)
        {
            LinePath path = paths.get(origin, destination);
            Army army = new Army(count, origin.getOwner(), destination, path, textureAtlas, fontProvider);
            addActor(army);
            armies.add(army);
            origin.removeSoldiers(count);
        }
    }

    private void processArmies()
    {
        Iterator<Army> armyIterator = armies.iterator();
        while (armyIterator.hasNext())
        {
            Army army = armyIterator.next();
            if (army.isAtTarget())
            {
                joinOrCreateBattle(army);
                army.remove();
                armyIterator.remove();
            }
        }
    }

    private void joinOrCreateBattle(Army army)
    {
        boolean joinedBattle = false;
        for (Battle battle : getBattles())
        {
            Settlement battleTarget = battle.getArmy().getTarget();
            if (battleTarget.equals(army.getTarget()))
            {
                Player battleOwner = battle.getArmy().getOwner();
                if (battleOwner.equals(army.getOwner()))
                {
                    battle.getArmy().addSoldiers(army.getSoldiers());
                    joinedBattle = true;
                    break;
                }
            }
        }
        if (!joinedBattle)
        {
            Battle battle = new Battle(army, textureAtlas);
            addActor(battle);
            battles.add(battle);
        }
    }
}