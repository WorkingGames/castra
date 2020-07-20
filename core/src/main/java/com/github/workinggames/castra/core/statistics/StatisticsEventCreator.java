package com.github.workinggames.castra.core.statistics;

import java.util.Date;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.github.workinggames.castra.core.actor.Army;
import com.github.workinggames.castra.core.actor.Battle;
import com.github.workinggames.castra.core.actor.Settlement;
import com.github.workinggames.castra.core.model.Player;
import com.github.workinggames.castra.core.stage.GameConfiguration;
import com.github.workinggames.castra.core.stage.World;
import com.github.workinggames.castra.core.statistics.event.ArmyDeployed;
import com.github.workinggames.castra.core.statistics.event.ArmyDto;
import com.github.workinggames.castra.core.statistics.event.BattleEnded;
import com.github.workinggames.castra.core.statistics.event.BattleJoined;
import com.github.workinggames.castra.core.statistics.event.BattleStarted;
import com.github.workinggames.castra.core.statistics.event.GameCanceled;
import com.github.workinggames.castra.core.statistics.event.GameEnded;
import com.github.workinggames.castra.core.statistics.event.GameStarted;
import com.github.workinggames.castra.core.statistics.event.PlayerDto;
import com.github.workinggames.castra.core.statistics.event.SettlementDto;
import com.github.workinggames.castra.core.task.VortexEventSender;

@RequiredArgsConstructor
public class StatisticsEventCreator
{
    private final VortexEventSender vortexEventSender;
    private final TimestampFormatter timestampFormatter;

    public void gameStarted(World world)
    {
        GameConfiguration gameConfiguration = world.getGameConfiguration();
        GameStarted.Attributes attributes = new GameStarted.Attributes(getGameId(world),
            new PlayerDto(gameConfiguration.getPlayer1()),
            new PlayerDto(gameConfiguration.getPlayer2()),
            gameConfiguration.getSeed(),
            Gdx.app.getType(),
            getSettlementDtos(world));
        GameStarted gameStarted = new GameStarted(attributes, getTimestamp());

        vortexEventSender.send(gameStarted);
    }

    public void gameEnded(World world, Player player, int playTime, int score, boolean playerWon)
    {
        GameEnded.Attributes attributes = new GameEnded.Attributes(getGameId(world),
            new PlayerDto(player),
            getSettlementDtos(world),
            playTime,
            score,
            playerWon);
        GameEnded gameEnded = new GameEnded(attributes, getTimestamp());

        vortexEventSender.send(gameEnded);
    }

    public void gameCanceled(World world)
    {
        GameCanceled.Attributes attributes = new GameCanceled.Attributes(getGameId(world),
            world.getGameConfiguration().getSeed(),
            getSettlementDtos(world),
            getArmyDtos(world),
            MathUtils.floor(world.getTimepiece().getTime()));
        GameCanceled gameCanceled = new GameCanceled(attributes, getTimestamp());

        vortexEventSender.send(gameCanceled);
    }

    public void armyDeployed(World world, Army army)
    {
        ArmyDeployed.Attributes attributes = new ArmyDeployed.Attributes(getGameId(world),
            new PlayerDto(army.getOwner()),
            army.getId(),
            army.getSoldiers(),
            new SettlementDto(army.getSource()),
            new SettlementDto(army.getTarget()),
            army.getPath().getDistance());
        ArmyDeployed armyDeployed = new ArmyDeployed(attributes, getTimestamp());

        vortexEventSender.send(armyDeployed);
    }

    public void battleStarted(World world, Battle battle)
    {
        Army army = battle.getArmy();
        BattleStarted.Attributes attributes = new BattleStarted.Attributes(getGameId(world),
            new PlayerDto(army.getOwner()),
            army.getId(),
            army.getSoldiers(),
            new SettlementDto(army.getTarget()));
        BattleStarted battleStarted = new BattleStarted(attributes, getTimestamp());

        vortexEventSender.send(battleStarted);
    }

    public void battleJoined(World world, Battle battle, Army army)
    {
        BattleJoined.Attributes attributes = new BattleJoined.Attributes(getGameId(world),
            new PlayerDto(army.getOwner()),
            battle.getArmy().getSoldiers(),
            army.getId(),
            army.getSoldiers(),
            new SettlementDto(army.getTarget()));
        BattleJoined battleJoined = new BattleJoined(attributes, getTimestamp());

        vortexEventSender.send(battleJoined);
    }

    public void battleEnded(String gameId, Army army, boolean captured)
    {
        BattleEnded.Attributes attributes = new BattleEnded.Attributes(gameId,
            new PlayerDto(army.getOwner()),
            new SettlementDto(army.getTarget()),
            captured);
        BattleEnded battleEnded = new BattleEnded(attributes, getTimestamp());

        vortexEventSender.send(battleEnded);
    }

    private String getTimestamp()
    {
        Date now = new Date(TimeUtils.millis());
        return timestampFormatter.getTimestamp(now);
    }

    private String getGameId(World world)
    {
        return world.getGameId();
    }

    private Array<SettlementDto> getSettlementDtos(World world)
    {
        Array<SettlementDto> settlementDtos = new Array<>();
        for (Settlement settlement : world.getSettlements())
        {
            settlementDtos.add(new SettlementDto(settlement));
        }
        return settlementDtos;
    }

    private Array<ArmyDto> getArmyDtos(World world)
    {
        Array<ArmyDto> armyDtos = new Array<>();
        for (Army army : world.getArmies())
        {
            armyDtos.add(new ArmyDto(army));
        }
        return armyDtos;
    }
}