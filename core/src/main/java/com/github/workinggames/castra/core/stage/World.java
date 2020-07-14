package com.github.workinggames.castra.core.stage;

import java.util.Iterator;

import lombok.Getter;
import lombok.Setter;

import com.badlogic.gdx.ai.DefaultTimepiece;
import com.badlogic.gdx.ai.Timepiece;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.workinggames.castra.core.actor.ActorCreator;
import com.github.workinggames.castra.core.actor.Army;
import com.github.workinggames.castra.core.actor.ArmySplit;
import com.github.workinggames.castra.core.actor.Battle;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.ai.Ai;
import com.github.workinggames.castra.core.ai.voons.MessageType;
import com.github.workinggames.castra.core.audio.AudioManager;
import com.github.workinggames.castra.core.font.FontProvider;
import com.github.workinggames.castra.core.model.Paths;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.model.PlayerType;
import com.github.workinggames.castra.core.model.SettlementSize;
import com.github.workinggames.castra.core.pathfinding.LinePath;
import com.github.workinggames.castra.core.screen.Screens;
import com.github.workinggames.castra.core.statistics.StatisticsEventCreator;

public class World extends Stage
{
    @Getter
    private final String gameId = IdGenerator.random();

    @Getter
    private final TextureAtlas textureAtlas;

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
    private ArmySplit armySplit;

    @Getter
    private final GameConfiguration gameConfiguration;

    private final MessageManager messageManager = MessageManager.getInstance();
    private final ActorCreator actorCreator;
    private final StatisticsEventCreator statisticsEventCreator;

    @Setter
    private Ai ai1 = null;

    @Setter
    private Ai ai2 = null;

    public World(
        Viewport viewport,
        TextureAtlas textureAtlas,
        FontProvider fontProvider,
        GameConfiguration gameConfiguration,
        StatisticsEventCreator statisticsEventCreator,
        AudioManager audioManager)
    {
        super(viewport);
        this.textureAtlas = textureAtlas;
        this.gameConfiguration = gameConfiguration;
        this.statisticsEventCreator = statisticsEventCreator;

        settlements = new Array<>();
        paths = new Paths();
        armies = new Array<>();
        battles = new Array<>();
        timepiece = new DefaultTimepiece();

        Screens screens = new Screens(viewport);
        addActor(screens.toBackground(textureAtlas.findRegion("Background1").getTexture()));

        if (gameConfiguration.getPlayer1().getType().equals(PlayerType.HUMAN))
        {
            armySplit = new ArmySplit(textureAtlas, fontProvider, gameConfiguration.getPlayer1(), audioManager);
            addActor(armySplit);
        }
        else if (gameConfiguration.getPlayer2().getType().equals(PlayerType.HUMAN))
        {
            armySplit = new ArmySplit(textureAtlas, fontProvider, gameConfiguration.getPlayer2(), audioManager);
            addActor(armySplit);
        }

        actorCreator = new ActorCreator(gameConfiguration, textureAtlas, fontProvider);
    }

    @Override
    public void act(float deltaTime)
    {
        processArmies();
        super.act(deltaTime);

        if (ai1 != null)
        {
            ai1.update();
        }
        if (ai2 != null)
        {
            ai2.update();
        }
    }

    public void createSettlement(SettlementSize size, int x, int y, int soldiers, Player owner)
    {
        Settlement settlement = actorCreator.createSettlement(size, x, y, soldiers, owner);
        addActor(settlement);
        settlements.add(settlement);
    }

    public void createArmy(Settlement source, Settlement target)
    {
        int count = source.getSoldiers() * source.getOwner().getSendTroopPercentage() / 100;
        if (count > 0)
        {
            createArmy(source, target, count);
        }
    }

    public void createArmy(Settlement source, Settlement target, int soldiers)
    {
        if (soldiers > 0 && source.getSoldiers() >= soldiers)
        {
            LinePath path = paths.get(source, target);
            Army army = actorCreator.createArmy(source, target, path, soldiers);
            addActor(army);
            armies.add(army);
            source.removeSoldiers(soldiers);

            messageManager.dispatchMessage(0, null, null, MessageType.ARMY_CREATED, army);
            statisticsEventCreator.armyDeployed(this, army);
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
                    statisticsEventCreator.battleJoined(this, battle, army);

                    battle.getArmy().addSoldiers(army.getSoldiers());
                    joinedBattle = true;
                    messageManager.dispatchMessage(0, null, null, MessageType.BATTLE_JOINED, army);
                    break;
                }
            }
        }
        if (!joinedBattle)
        {
            Battle battle = actorCreator.createBattle(army);
            addActor(battle);
            battles.add(battle);

            messageManager.dispatchMessage(0, null, null, MessageType.BATTLE_STARTED, battle);
            statisticsEventCreator.battleStarted(this, battle);
        }
    }
}